package tsp.projects.evolution;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.*;

import static tsp.projects.Transformations.crossing;
import static tsp.projects.Transformations.transformSwap;

public class EvolutionBis extends DemoProject {

    private static int NB_INDIVIDUS = 100;
    private static double MUTATION_CHANCE = 0.05;
    private static double TOURNAMENT_PROPORTION = 0.1;
    private static double ELITISME_PROPORTION = 0.2;
    private int nbGen = 0;
    private ArrayList<Individu> population;
    public EvolutionBis( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Camille Masson" );
        setMethodName( "Darwin use unSorted map" );
    }

    @Override
    public void initialization() {
        Individu.initEva(this.evaluation);

        population = new ArrayList<>();

        while (population.size() < NB_INDIVIDUS) {
            population.add(new Individu( HillClimbing() ));
        }
    }

    @Override
    public void loop() {
        reproduce();
        mutatePopulation();
        evaluation.evaluate( getBestOnPop(population).getPath() );
        nbGen++;
    }

    public void reproduce() {
        ArrayList<Individu> popChildren = new ArrayList<>();

        while(popChildren.size() < NB_INDIVIDUS){
            ArrayList<Individu> parents = getParentsTournament(2);
            Path[] children = crossing(parents.get(0).getPath(), parents.get(1).getPath());
            for (Path path : children) popChildren.add(new Individu(path));
        }


        Collections.sort(population);
        popChildren.addAll( new ArrayList<>(population.subList(0, (int) Math.round( ELITISME_PROPORTION * NB_INDIVIDUS ))) );

        Collections.sort(popChildren);

        population = new ArrayList<>(popChildren.subList(0, NB_INDIVIDUS));
    }


    public Individu getBestOnPop(ArrayList<Individu> pop){
        Individu bestOne = null;
        for (Individu i : pop) {
            if(bestOne == null ||
                    i.getDistance() > bestOne.getDistance())
                bestOne = i;
        }

        return bestOne;
    }

    public ArrayList<Individu> getNBBestOnPop(ArrayList<Individu> pop, int n){ // si n = 1 favoriser getBestOnPop
        ArrayList<Individu> clonePop = (ArrayList<Individu>) pop.clone();
        Collections.sort(clonePop);
        return new ArrayList<>(clonePop.subList(0, n));
    }

public ArrayList<Individu> getNRandomElem(ArrayList<Individu> pop, int n, ArrayList<Individu> avoid){
        ArrayList<Individu> pop2 = (ArrayList<Individu>) pop.clone();
        if (avoid != null || avoid.size() != 0)
            for (Individu i : avoid)
                pop2.remove(i);

        return getNRandomElem(pop2, n);
    }

    public ArrayList<Individu> getNRandomElem(ArrayList<Individu> pop, int n){
        ArrayList<Individu> clone = (ArrayList<Individu>) pop.clone();

        Collections.shuffle(clone);
        clone = new ArrayList<>(clone.subList(0, n));

        return clone;
    }

    public ArrayList<Individu> getParentsTournament(int nbParent) {
        ArrayList<Individu> parents = new ArrayList<>();

        while (parents.size() < nbParent) {
            ArrayList<Individu> candidates = getNRandomElem(population, (int) Math.round(NB_INDIVIDUS * TOURNAMENT_PROPORTION), parents);
            Individu best = getBestOnPop(candidates);
            parents.add(best);
        }

        return parents;
    }

    public double getSumEval(ArrayList<Individu> pop, Individu best) {
        double sum = 0;
        for ( Individu individu : pop )
            sum += 1 / ( individu.getDistance() - best.getDistance() + 1 );
        return sum;
    }

    public ArrayList<Individu> getFitParents(int nbParent) {
        ArrayList<Individu> clonePop = (ArrayList<Individu>) population.clone();
        ArrayList<Individu> parents = new ArrayList<>();
        Individu best = getBestOnPop(clonePop);

        while (parents.size() < nbParent){
            double rand = Math.random() * getSumEval(clonePop, best);
            double weight = 0;
            int n = 0;
            for ( Individu individu : clonePop ) {
                n++;
                weight += 1 / ( individu.getDistance() - best.getDistance() + 1 );
                if ( weight >= rand ) {
                    parents.add(individu);
                    clonePop.remove(individu);
                    if (individu.equals(best)) best = getBestOnPop(clonePop);
                    break;
                }
                //System.out.println(individu.getDistance().toString());
            }
        }

        return parents;
    }

    public void mutatePopulation() {
        ArrayList<Individu> resPop = new ArrayList<>();
        for ( Individu individu : population ){
            if ( Math.random() < MUTATION_CHANCE )
                resPop.add(new Individu(mutate(individu.getPath())));
            else
                resPop.add(new Individu(individu.getPath()));
        }
        population = resPop;
    }

    public Path mutate( Path path ) {
        return transformSwap( path );
    }



    public Path HillClimbing() {
        int cpt = 0;
        int index;
        int distance = 0;

        Coordinates pointActuel;
        List<Integer> pasVu;
        int [] path = new int[problem.getLength()];
        pasVu = new ArrayList<>();

        Random random = new Random();
        int j = random.nextInt (problem.getLength());
        pointActuel = this.problem.getCoordinates(j);
        path[0] = j;

        for (int i = 0; i < problem.getLength(); i++) {
            if (i != j) {
                pasVu.add(i);
            }
        }

        int minVoisinDistance;
        int minVoisinIndex;

        while (pasVu.size() != 0) {
            cpt++;
            minVoisinIndex = pasVu.get(0);
            minVoisinDistance = (int) pointActuel.distance(this.problem.getCoordinates(pasVu.get(0)));
            index = 0;
            for (int i = 1; i < pasVu.size(); i++) {
                if (minVoisinDistance > pointActuel.distance(this.problem.getCoordinates(pasVu.get(i)))) {
                    minVoisinDistance = (int) pointActuel.distance(this.problem.getCoordinates(pasVu.get(i)));
                    minVoisinIndex = pasVu.get(i);
                    index = i;
                }
            }
            distance += minVoisinDistance;
            path[cpt] = minVoisinIndex;
            pointActuel = this.problem.getCoordinates(minVoisinIndex);
            pasVu.remove(index);
        }
        Path pathFin = new Path(path);
        return pathFin;
    }

}
