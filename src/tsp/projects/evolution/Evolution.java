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
    private static double MUTATION_CHANCE = 0.5;
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
            population.put( evaluation.quickEvaluate( path ), path );
        }
    }

    @Override
    public void loop() {
        reproduction();
        mutatePopulation();
        evaluation.evaluate( population.firstEntry().getValue() );
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
                newpop.put( evaluation.quickEvaluate( child ), child );
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
        return null;
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
            population.put( evaluation.quickEvaluate( child ), child );
    }

    public void mutatePopulation() {
        TreeMap<Double, Path> mutated = new TreeMap<>();
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            if ( Math.random() < MUTATION_CHANCE ) {
                Path np = mutate( entry.getValue() );
                mutated.put( evaluation.quickEvaluate(np),np );
            } else
                mutated.put( entry.getKey(), entry.getValue() );

        population = mutated;
    }

    public Path mutate( Path path ) {
        return transformSwapSection( path );
    }

}
