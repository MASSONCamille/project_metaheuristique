package tsp.evaluation;

import java.util.Random;

/**
 * @author Alexandre Blansché
 * Classe représentant un chemin pour parcourir les "villes"
 * Attention, rien ne garantit que le chemin est valide
 */
public final class Path {
    private int[] path;

    /**
     * Constructeur
     *
     * @param path Liste ordonnée des "villes"
     */
    public Path( Path path ) {
        this.path = new int[ path.path.length ];
        for ( int i = 0 ; i < this.path.length ; i++ )
            this.path[ i ] = path.path[ i ];
    }

    /**
     * Constructeur
     *
     * @param path Liste ordonnée des "villes"
     */
    public Path( int[] path ) {
        this.path = path;
    }

    /**
     * Constructeur : chemin aléatoire d'une longueur donnée
     *
     * @param length Nombre de "villes"
     */
    public Path( int length ) {
        this.path = Path.getRandomPath( length );
    }

    /**
     * @param length Nombre de "villes"
     * @return Un chemin aléatoire d'une longueur donnée
     */
    public static int[] getRandomPath( int length ) {
        int[] path = new int[ length ];
        for ( int i = 0 ; i < length ; i++ )
            path[ i ] = i;
        Random random = new Random();
        for ( int i = length - 1 ; i > 0 ; i-- ) {
            int j = random.nextInt( i + 1 );
            int tmp = path[ i ];
            path[ i ] = path[ j ];
            path[ j ] = tmp;
        }
        return path;
    }

    /**
     * @return Liste ordonnée des "villes"
     */
    public int[] getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        String string = Integer.toString( this.path[ 0 ] );
        for ( int i = 1 ; i < this.path.length ; i++ )
            string += ";" + this.path[ i ];
        return string;
    }
}
