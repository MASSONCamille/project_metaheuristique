package tsp.projects;

import tsp.evaluation.Path;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Transformations {

    public static Path transformSwap( Path path ) {
        int rand1 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
        int rand2 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
//        System.out.println( "swapping cities " + rand1 + " and " + rand2 );

        int[] newpath = path.getPath().clone();
        int tmp = newpath[ rand1 ];
        newpath[ rand1 ] = newpath[ rand2 ];
        newpath[ rand2 ] = tmp;

        return new Path( newpath );
    }

    public static Path transformSwapConsecutive( Path path ) {
        int rand1 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length - 1 );
        int[] newpath = path.getPath().clone();
        int tmp = newpath[ rand1 ];
        newpath[ rand1 ] = newpath[ rand1 + 1 ];
        newpath[ rand1 + 1 ] = tmp;

        return new Path( newpath );
    }

    public static Path transformSwapSection( Path path ) {
        int rand1 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
        int rand2 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
        return swapSection( path, rand1, rand2 );
    }

    public static Path swapSection( Path path, int a, int b ) {
        int sectionLength = Math.abs( a - b );
        int min = min( a, b );
        int max = max( a, b );
        int[] newpath = path.getPath().clone();

        for ( int i = 0 ; i <= sectionLength ; i++ )
            newpath[ min + i ] = path.getPath()[ max - i ];

        return new Path( newpath );
    }

    public static Path transformMove( Path path ) {
        int rand1 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
        int rand2 = ThreadLocalRandom.current().nextInt( 0, path.getPath().length );
        int[] newpath = path.getPath().clone();
        for ( int i = 0 ; i < max( rand1, rand2 ) ; i++ ) {
            int tmp = newpath[ min( rand1, rand2 ) + i ];
            newpath[ min( rand1, rand2 ) + i + 1 ] = newpath[ min( rand1, rand2 ) + i ];
            newpath[ min( rand1, rand2 ) + i ] = tmp;
        }
        return new Path( newpath );
    }

    public static Path[] simpleCrossover( Path p1, Path p2, int nbChildren ) {
        Path[] children = new Path[ nbChildren ];
        for ( int i = 0 ; i < nbChildren ; i++ )
            children[ i ] = new Path( new int[ p1.getPath().length ] );

        int[] path1 = p1.getPath();
        int[] path2 = p2.getPath();
        ArrayList<Integer> unused = new ArrayList<>();
        for ( int i = 0 ; i < path1.length ; i++ ) {
            if ( path1[ i ] == path2[ i ] )
                for ( int j = 0 ; j < nbChildren ; j++ )
                    children[ j ].getPath()[ i ] = path1[ i ];
            else {
                for ( int j = 0 ; j < nbChildren ; j++ )
                    children[ j ].getPath()[ i ] = -1;
                unused.add( path1[ i ] );
            }
        }

        for ( int i = 0 ; i < nbChildren ; i++ ) {
            Collections.shuffle( unused );
            int j = 0;
            for ( Integer value : unused ) {
                while ( j < path1.length ) {
                    if ( children[ i ].getPath()[ j ] == -1 ) {
                        children[ i ].getPath()[ j ] = value;
                        j++;
                        break;
                    }
                    j++;
                }
            }
        }

//        for ( int i = 0 ; i < nbChildren ; i++ ) {
//
//        }

        return children;
    }

    public static Path[] orderedCrossover( Path p1, Path p2 ) {
        int length = p1.getPath().length;
        int[] addressList = new int[ length ];
        for ( int i = 0 ; i < length ; i++ )
            addressList[ p2.getPath()[ i ] ] = i;
        return null;
    }

    public static Path[] crossing( Path p1, Path p2 ) {
        Path[] children = new Path[ 2 ];
        int length = p1.getPath().length;
        int[] np1 = new int[ length ];
        int[] np2 = new int[ length ];
        int[] p2reverse = new int[ length ];

        for ( int a = 0 ; a < length ; a++ )
            p2reverse[ p2.getPath()[ a ] ] = a;

        TreeMap<Integer, Integer> l1 = new TreeMap<>();
        TreeMap<Integer, Integer> l2 = new TreeMap<>();

        for ( int i = 0 ; i < length ; i++ ) {
            double rand = Math.random();
            if ( rand < 0.5 ) {
//                System.out.println( "fils1" );
                np1[ i ] = p1.getPath()[ i ];
                np2[ i ] = -1;
                l2.put( p2reverse[ np1[ i ] ], np1[ i ] );
            } else {
//                System.out.println( "fils2" );
                np2[ i ] = p1.getPath()[ i ];
                np1[ i ] = -1;
                l1.put( p2reverse[ np2[ i ] ], np2[ i ] );
            }
        }

        int i = 0;
        for ( Map.Entry<Integer, Integer> entry : l1.entrySet() ) {
            while ( i < length ) {
                if ( np1[ i ] == -1 ) {
                    np1[ i ] = entry.getValue();
                    i++;
                    break;
                }
                i++;
            }
        }

        i = 0;
        for ( Map.Entry<Integer, Integer> entry : l2.entrySet() ) {
            while ( i < length ) {
                if ( np2[ i ] == -1 ) {
                    np2[ i ] = entry.getValue();
                    i++;
                    break;
                }
                i++;
            }
        }

        children[ 0 ] = new Path( np1 );
        children[ 1 ] = new Path( np2 );
        return children;
    }

    public static Path[] CrossoverCours( Path p1, Path p2 ) {
        int length = p1.getPath().length;
        int[] child1 = new int[ length ], child2 = new int[ length ];

        for ( int i = 0 ; i < length ; i++ ) {
            int coin = ThreadLocalRandom.current().nextInt( 0, 2 );
            if ( coin == 1 ) {
                child1[ i ] = p1.getPath()[ i ];
                child2[ i ] = -1;
            } else {
                child1[ i ] = -1;
                child2[ i ] = p1.getPath()[ i ];
            }
        }

        int pos1 = 0, pos2 = 0;
        for ( int town : p2.getPath() ) {
            while ( child1[ pos1 ] != -1 ) pos1++;
            while ( child1[ pos2 ] != -1 ) pos2++;

            for ( int i = 0 ; i < length ; i++ ) {
                if ( child1[ i ] == town ) {
                    child2[ pos2 ] = town;
                    pos2++;
                    break;
                }
                if ( child2[ i ] == town ) {
                    child1[ pos1 ] = town;
                    pos1++;
                    break;
                }
            }
        }

        Path[] children = new Path[ 2 ];
        children[ 0 ] = new Path( child1 );
        children[ 1 ] = new Path( child2 );
        return children;
    }
}