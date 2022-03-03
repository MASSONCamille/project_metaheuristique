package tsp.projects;

import tsp.evaluation.Path;

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
        int sectionLength = Math.abs( rand1 - rand2 );
        int min = min( rand1, rand2 );
        int max = max( rand1, rand2 );
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

    public static Path Mutate( Path path ) {
        return path;
    }

    public static Path[] Crossover( Path p1, Path p2 ) {
        Path[] children = new Path[ 2 ];
        children[0]
        int length = p1.getPath().length;
        for ( int i = 0 ; i < length ; i++ ) {
            double rand = Math.random();
            if ( i < 0.5 )
                children[ 0 ].getPath()[ i ] = p1.getPath()[ i ];

        }

        return children;
    }

    public static Path[] CrossoverBrut( Path p1, Path p2 ) {
        int length = p1.getPath().length;
        int[] child1 = new int[length] , child2 = new int[length];

        int Nrand = ThreadLocalRandom.current().nextInt( 0, length );
        int[] listRand = new int[Nrand];
        for (int i = 0; i < Nrand; i++) {
            listRand[]
        }



        Path[] children = new Path[2];
        children[0] = new Path(child1);
        children[1] = new Path(child2);
        return children;
    }

    public static Path[] CrossoverCours( Path p1, Path p2 ) {
        int length = p1.getPath().length;
        int[] child1 = new int[length] , child2 = new int[length];

        for(int i=0; i<length; i++){
            int coin = ThreadLocalRandom.current().nextInt( 0, 2 );
            if(coin == 1){
                child1[i] = p1.getPath()[i];
                child2[i] = -1;
            }else{
                child1[i] = -1;
                child2[i] = p1.getPath()[i];
            }
        }

        int pos1 = 0, pos2 = 0;
        for (int town: p2.getPath()) {
            while(child1[pos1] != -1) pos1++;
            while(child1[pos2] != -1) pos2++;

            for(int i=0; i<length; i++){
                if(child1[i] == town){
                    child2[pos2] = town;
                    pos2++;
                    break;
                }
                if(child2[i] == town){
                    child1[pos1] = town;
                    pos1++;
                    break;
                }
            }
        }

        Path[] children = new Path[2];
        children[0] = new Path(child1);
        children[1] = new Path(child2);
        return children;
    }
}