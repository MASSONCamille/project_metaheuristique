package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static tsp.projects.Transformations.*;

public class Evolution extends CompetitorProject {

    private static int NB_INDIVIDUS = 100;
    private static double MUTATION_CHANCE = 0.05;
    private TreeMap<Double, Path> population = new TreeMap<>();
    private ArrayList<Path> alpop = new ArrayList<>();
    private int nbGen = 0;

    public Evolution( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Pierre Henselmann" );
        setMethodName( "Natürliche Selektion" );
    }

    @Override
    public void initialization() {
        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            Path path = new Path( problem.getLength() );
            population.put( evaluation.quickEvaluate( path ), path );
        }
        alpop = populationAsArrayList();
    }

    @Override
    public void loop() {
        reproduce();
//        System.out.println( "génération n°" + ++nbGen );
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
//                if ( p2 == p1 ) System.out.println( "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAH" );
            } while ( p2 == p1 );

            Path[] children = crossing( p1, p2 );
//            Path[] children = simpleCrossover( p1, p2, 2 );
            for ( Path child : children )
                childpop.put( evaluation.quickEvaluate( child ), child );
        }
//        population.putAll( childpop );
//        while ( population.size() > NB_INDIVIDUS )
//            population.pollLastEntry();


        TreeMap<Double, Path> newpop = new TreeMap<>();
        Iterator popit = population.entrySet().iterator();
        Iterator childit = childpop.entrySet().iterator();
        Map.Entry epop = ( Map.Entry ) popit.next();
        Map.Entry echild = ( Map.Entry ) childit.next();

        while ( newpop.size() < NB_INDIVIDUS ) {
            if ( ( ( Double ) epop.getKey() ) > ( ( Double ) echild.getKey() ) ) {
                newpop.put( ( Double ) echild.getKey(), ( Path ) echild.getValue() );
                echild = ( Map.Entry ) childit.next();
            } else {
                newpop.put( ( Double ) epop.getKey(), ( Path ) epop.getValue() );
                epop = ( Map.Entry ) popit.next();
            }
        }
        population = newpop;
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
        for ( int i = 0 ; i < NB_INDIVIDUS * 0.1 ; i++ )
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
//        System.out.println( "sumEval = " + sumEval );
//        System.out.println( "rand = " + rand );
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

}