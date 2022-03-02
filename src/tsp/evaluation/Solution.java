package tsp.evaluation;

import java.util.ArrayList;

/**
 * @author Alexandre Blansché
 * Solution au problème TSP : nom de la méthode, auteurs, bom du problème, évaluation de la solution
 */
public final class Solution implements Comparable<Solution> {
    private String name;
    private ArrayList<String> authors;
    private String problemName;
    private double evaluation;

    /**
     * Constructeur
     *
     * @param authors     Liste des auteurs
     * @param name        Nom de la méthode
     * @param problemName Nom du problème
     * @param evaluation  Évaluation de la solution
     */
    public Solution( ArrayList<String> authors, String name, String problemName, double evaluation ) {
        this.authors = authors;
        this.name = name;
        this.problemName = problemName;
        this.evaluation = evaluation;
    }

    /**
     * Constructeur : moyenne sur plusieurs évaluations
     *
     * @param solutions Liste de solutions
     */
    public Solution( ArrayList<Solution> solutions ) {
        this.authors = solutions.get( 0 ).authors;
        this.name = solutions.get( 0 ).name;
        this.problemName = solutions.get( 0 ).problemName;
        this.evaluation = 0;
        for ( Solution solution : solutions )
            this.evaluation += solution.evaluation;
        this.evaluation /= solutions.size();
    }

    private static void normalize( ArrayList<Solution> solutions ) {
        double min = Double.MAX_VALUE;
        for ( Solution solution : solutions )
            if ( solution.evaluation < min )
                min = solution.evaluation;
        for ( Solution solution : solutions )
            solution.evaluation = 100 * ( solution.evaluation - min ) / min;
    }

    /**
     * Agrégation et normalisation des résultats pour la comparaison des algorithmes
     *
     * @param solutions Solutions de tous les algorithmes à tous les problèmes
     * @return Évaluations agrégées et normalisées
     */
    public static ArrayList<Solution> aggregate( ArrayList<ArrayList<Solution>> solutions ) {
        for ( ArrayList<Solution> s : solutions )
            Solution.normalize( s );
        ArrayList<Solution> agg = new ArrayList<Solution>();
        for ( int i = 0 ; i < solutions.get( 0 ).size() ; i++ ) {
            ArrayList<Solution> projectSolutions = new ArrayList<Solution>();
            for ( ArrayList<Solution> s : solutions )
                projectSolutions.add( s.get( i ) );
            agg.add( new Solution( projectSolutions ) );
        }
        return agg;
    }

    /**
     * @return L'évaluation de la solution
     */
    public double getEvaluation() {
        return this.evaluation;
    }

    @Override
    public String toString() {
        String string = "Projet " + this.name + "\n";
        for ( int i = 0 ; i < this.authors.size() ; i++ )
            string += this.authors.get( i ) + "\n";
        string += "Évaluation: " + this.evaluation;
        return string;
    }

    @Override
    public int compareTo( Solution solution ) {
        int res = 0;
        if ( this.evaluation < solution.evaluation )
            res = -1;
        else if ( this.evaluation > solution.evaluation )
            res = 1;
        return res;
    }
}
