package tsp.run;

import tsp.evaluation.Path;

import static tsp.projects.Transformations.crossing;
import static tsp.projects.Transformations.simpleCrossover;

public class Tests {
    public static void main( String[] args ) {
        System.out.println( "start" );
        int length = 8;
        Path p1 = new Path( length );
        Path p2 = new Path( length );

//        Path[] children = crossing( p1, p2 );
        Path[] children = simpleCrossover( p1, p2, 2 );

        System.out.println( "finish" );

        System.out.println( p1 );
        System.out.println( p2 );
        System.out.println( children[ 0 ] );
        System.out.println( children[ 1 ] );
    }
}
