package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import static tsp.projects.Transformations.*;

public class Evolution extends CompetitorProject {

    private static int NB_INDIVIDUS = 1000;
    private static double MUTATION_CHANCE = 0.1;
    private TreeMap<Double, Path> population = new TreeMap<>();

    public Evolution( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Pierre Henselmann" );
        setMethodName( "Natürliche Selektion" );
    }

    @Override
    public void initialization() {
        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            Path path = new Path( problem.getLength() );
            population.put( evaluation.evaluate( path ), path );
        }
    }

    @Override
    public void loop() {
        reproduction();
        mutatePopulation();
    }

    public void reproduction() {
        TreeMap<Double, Path> newpop = new TreeMap<>();

        while ( newpop.size() < NB_INDIVIDUS ) {
            Path p1 = getFitParent();
            Path p2;
            do {
                p2 = getFitParent();
            } while ( p2 == p1 );

            Path[] children = crossing( p1, p2 );
            //addChildren( children );
            for ( Path child : children )
                newpop.put( evaluation.evaluate( child ), child );
        }

        population = newpop;
    }


    public double getSumEval() {
        double sum = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            sum += 1 / ( entry.getKey() - population.firstEntry().getKey() + 1 );
        return sum;
    }

    public Path getRandomParent() {
        while ( true )
            for ( Map.Entry<Double, Path> entry : population.entrySet() )
                if ( Math.random() < 0.002 )
                    return entry.getValue();
    }

    public Path getParentWeightedByOrder() {
        //...
    }

    public Path getFitParent() {
        double sumEval = getSumEval();
        double rand = Math.random() * sumEval;
//        System.out.println( "sumEval = " + sumEval );
//        System.out.println( "rand = " + rand );
        double weight = 0;
        int n = 0;
        for ( Map.Entry<Double, Path> entry : population./*descendingMap().*/entrySet() ) {
            n++;
            weight += 1 / ( entry.getKey() - population.firstEntry().getKey() + 1 );
            if ( weight >= rand ) {
                //System.out.println( "chose nb " + n + " out of " + population.size() );
                return /*population.remove( entry )*/entry.getValue();
            }
        }
        //ne devrais jamais arriver
        System.out.println( "pb dans getParent" );
        return population.remove( population.firstEntry() );
    }

    public void addChildren( Path[] children ) {
        for ( Path child : children )
            population.put( evaluation.evaluate( child ), child );
    }

    public void mutatePopulation() {
        HashMap<Map.Entry<Double, Path>, Path> replaceList = new HashMap<>();
        for ( Map.Entry<Double, Path> entry : population.entrySet() ) {
            if ( Math.random() < MUTATION_CHANCE ) {
                replaceList.put( entry, mutate( entry.getValue() ) );
            }
        }
        for ( Map.Entry<Map.Entry<Double, Path>, Path> replaceItem : replaceList.entrySet() ) {
            population.remove( replaceItem.getKey() );
            population.put( evaluation.evaluate( replaceItem.getValue() ), replaceItem.getValue() );
        }
    }

    public Path mutate( Path path ) {
        return transformSwapSection( path );
    }

}
