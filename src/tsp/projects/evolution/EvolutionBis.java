package tsp.projects.evolution;

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

        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            population.add(new Individu(new Path(problem.getLength())));
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
            ArrayList<Individu> parents = getFitParents(2);
            Path[] children = crossing(parents.get(0).getPath(), parents.get(1).getPath());
            for (Path path : children) popChildren.add(new Individu(path));
        }

        population.addAll(popChildren);

        population = new ArrayList<>(population.subList(0, NB_INDIVIDUS));
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

}
