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

import static tsp.projects.Transformations.crossing;
import static tsp.projects.Transformations.transformSwapSection;

public class Evolution extends CompetitorProject {

    private static int NB_INDIVIDUS = 10;
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
            population.put( evaluation.quickEvaluate( path ), path );
        }
    }

    @Override
    public void loop() {
        Path p1 = getParent();
        Path p2 = getParent();
        Path[] children = crossing( p1, p2 );
        addChildren( children );
        mutatePopulation();

        evaluation.evaluate( population.firstEntry().getValue() );
    }

    public void reproduction() {

    }

    public double getSumEval() {
        double sum = 0;
        for ( Map.Entry<Double, Path> entry : population.entrySet() )
            sum += 1 / Math.log( entry.getKey() );
        return sum;
    }

    public Path getParent() {
        double sumEval = getSumEval();
        double rand = Math.random() * sumEval;
//        System.out.println( "sumEval = " + sumEval );
//        System.out.println( "rand = " + rand );
        double weight = 0;
        for ( Map.Entry<Double, Path> entry : population./*descendingMap().*/entrySet() ) {
            weight += 1 / Math.log( entry.getKey() );
            if ( weight >= rand )
                return /*population.remove( entry )*/entry.getValue();
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
        HashMap<Map.Entry<Double, Path>, Path> replaceList = new HashMap<>();
        for ( Map.Entry<Double, Path> entry : population.entrySet() ) {
            if ( Math.random() < MUTATION_CHANCE ) {
                replaceList.put( entry, mutate( entry.getValue() ) );
            }
        }
        for ( Map.Entry<Map.Entry<Double, Path>, Path> replaceItem : replaceList.entrySet() ) {
            population.remove( replaceItem.getKey() );
            population.put( evaluation.quickEvaluate( replaceItem.getValue() ), replaceItem.getValue() );
        }
    }

    public Path mutate( Path path ) {
        return transformSwapSection( path );
    }

}
