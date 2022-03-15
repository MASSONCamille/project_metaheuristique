package tsp.projects.evolution;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static tsp.projects.Transformations.*;

public class Evolution2 extends CompetitorProject {

    private static int NB_INDIVIDUS = 100;
    private static double MUTATION_CHANCE = 0.05;
    private TreeMap<Double, Path> population = new TreeMap<>();
    private ArrayList<Path> alpop = new ArrayList<>();
    private int nbGen = 0;

    public Evolution2( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Pierre Henselmann" );
        setMethodName( "自然な選択" );
    }

    @Override
    public void initialization() {
        population = new TreeMap<>();
        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            Path path = HillClimbing();
            population.put( evaluation.quickEvaluate( path ), path );
        }
        alpop = populationAsArrayList();
    }

    @Override
    public void loop() {
        reproduce();
        mutatePopulation();
        evaluation.evaluate( population.firstEntry().getValue() );
        alpop = populationAsArrayList();
    }

    public void reproduce() {
        TreeMap<Double, Path> childpop = new TreeMap<>();

        while ( childpop.size() < NB_INDIVIDUS ) {
            Path p1 = getFitParent();
            Path p2;
            do {
                p2 = getFitParent();
            } while ( p2 == p1 );

            Path[] children = crossing( p1, p2 );
            for ( Path child : children )
                childpop.put( evaluation.quickEvaluate( child ), child );
        }


        population.putAll( childpop );
        while ( population.size() > NB_INDIVIDUS )
            population.pollLastEntry();

//        TreeMap<Double, Path> newpop = new TreeMap<>();
//        Iterator popit = population.entrySet().iterator();
//        Iterator childit = childpop.entrySet().iterator();
//        Map.Entry epop = (Map.Entry) popit.next();
//        Map.Entry echild = (Map.Entry) childit.next();
//
//        while ( newpop.size() < NB_INDIVIDUS ) {
//            if (( ( Double ) epop.getKey() ) > ( ( Double ) echild.getKey() )) {
//                newpop.put( ( Double ) echild.getKey(), ( Path ) echild.getValue() );
//                if(childit.hasNext()) echild = ( Map.Entry ) childit.next();
//            } else {
//                newpop.put( ( Double ) epop.getKey(), ( Path ) epop.getValue() );
//                if(popit.hasNext()) epop = ( Map.Entry ) popit.next();
//            }
//        }
//        population = newpop;


    }


    public double getSumEval() {
        double sum = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            sum += 1 / ( entry.getKey() - population.firstEntry().getKey() + 1 );
        return sum;
    }

    public ArrayList populationAsArrayList() {
        ArrayList<Path> result = new ArrayList<>();
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            result.add( entry.getValue() );
        return result;
    }

    public Path getRandomParent() {
//        while ( true )
//            for ( Map.Entry<Double, Path> entry : population.entrySet() )
//                if ( Math.random() < 1.0 / NB_INDIVIDUS )
//                    return entry.getValue();
        return alpop.get( ( ( int ) Math.random() * NB_INDIVIDUS ) );
    }

    public Path getParentTournament() {
        ArrayList<Path> candidates = new ArrayList<>();
        Collections.shuffle( alpop );
        for ( int i = 0 ; i < NB_INDIVIDUS * 0.25 ; i++ )
            candidates.add( alpop.get( i ) );

        Double min = Double.MAX_VALUE;
        int imin = 0;
        for ( int i = 0 ; i < candidates.size() ; i++ ) {
            double eval = evaluation.quickEvaluate( candidates.get( i ) );
            if ( min >= eval ) {
                min = eval;
                imin = i;
            }
        }
        return candidates.get( imin );
    }

    public Path getFitParent() {
        double sumEval = getSumEval();
        double rand = Math.random() * sumEval;
        double weight = 0;
        int n = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() ) {
            n++;
            weight += 1 / ( entry.getKey() - population.firstEntry().getKey() + 1 );
            if ( weight >= rand ) {
                return entry.getValue();
            }
        }
        //ne devrais jamais arriver
        System.out.println( "pb dans getParent" );
        return population.remove( population.firstEntry() );
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



    /* ------------------------------------------------------------------------------------------ */


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