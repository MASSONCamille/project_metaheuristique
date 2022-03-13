package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.*;

import static tsp.projects.Transformations.crossing;
import static tsp.projects.Transformations.transformSwap;

public class EvolutionBis extends CompetitorProject {

    private static int NB_INDIVIDUS = 100;
    private static double MUTATION_CHANCE = 0.05;
    private static double TOURNAMENT_PROPORTION = 0.1;
    private int nbGen = 0;
    private ArrayList<Individu> population;
    private Map.Entry<Double, Path> bestOnPop = null; // ATTENTION a utiliser uniquement lors de la selection si il ce fait sur la generation des parent uniquement

    public EvolutionBis( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Camille Masson" );
        setMethodName( "Darwin use unSorted map" );
    }

    @Override
    public void initialization() {
        Individu.initEva(this.evaluation);

        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            population.add(new Individu(new Path(problem.getLength())));
        }
        bestOnPop = getBestOnPop(population);
    }

    @Override
    public void loop() {
        reproduce();
        mutatePopulation();
        bestOnPop = getBestOnPop(population);
        evaluation.evaluate( bestOnPop.getValue() );
        nbGen++;
    }

    public void reproduce() {

        TreeMap<Double, Path> childpop = new TreeMap<>();

        while ( childpop.size() < NB_INDIVIDUS ) {
            Path p1 = getParentTournament();
            Path p2;
            do {
                p2 = getParentTournament();
            } while ( p2 == p1 );

            Path[] children = crossing( p1, p2 );
            for ( Path child : children )
                childpop.put( evaluation.quickEvaluate( child ), child );
        }
        TreeMap<Double, Path> newpop = new TreeMap<>();
        Iterator popit = population.entrySet().iterator();
        Iterator childit = childpop.entrySet().iterator();
        Map.Entry epop = (Map.Entry)popit.next();
        Map.Entry echild = (Map.Entry)childit.next();

        while (newpop.size() < NB_INDIVIDUS){
            if ( ((Double) epop.getKey()) > ((Double) echild.getKey()) ) {
                newpop.put( (Double)echild.getKey() , (Path)echild.getValue());
                echild = (Map.Entry)childit.next();
            }else{
                newpop.put( (Double)epop.getKey() , (Path)epop.getValue());
                epop = (Map.Entry)popit.next();
            }
        }
        population = newpop;
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
        clone = (ArrayList<Individu>) clone.subList(0, n);

        return clone;
    }

    public double getSumEval() {
        double sum = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            sum += 1 / ( entry.getKey() - bestOnPop.getKey() + 1 );
        return sum;
    }

    public Path getRandomParent() {
        return getNRandomElem(population, 1).get(0);
    }

    public HashMap<Double, Path> getParentTournament(int nbParent) {
        HashMap<Double, Path> parents = new HashMap<>();

        while (parents.size() < nbParent) {
            HashMap<Double, Path> candidates = getNRandomElem(population, (int) Math.round(NB_INDIVIDUS * TOURNAMENT_PROPORTION), parents);
            Map.Entry<Double, Path> best = getBestOnPop(candidates);
            parents.put(best.getKey(), best.getValue());
        }

        return parents;
    }

    public HashMap<Double, Path> getFitParent(int nbParent) {
        HashMap<Double, Path> parents = new HashMap<>();
        double sumEval = getSumEval();
        double rand = Math.random() * sumEval;
        double weight = 0;
        int n = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() ) {
            n++;
            weight += 1 / ( entry.getKey() - bestOnPop.getKey() + 1 );
            if ( weight >= rand ) {
                return entry.getValue();
            }
        }


        return parents;
    }

    public void mutatePopulation() {
        TreeMap<Double, Path> mutated = new TreeMap<>();
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            if ( Math.random() < MUTATION_CHANCE ) {
                Path np = mutate( entry.getValue() );
                mutated.put( evaluation.quickEvaluate( np ), np );
            } else
                mutated.put( entry.getKey(), entry.getValue() );

        population = mutated;
    }

    public Path mutate( Path path ) {
        return transformSwap( path );
    }

}
