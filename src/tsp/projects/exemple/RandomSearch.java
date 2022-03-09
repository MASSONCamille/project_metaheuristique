package tsp.projects.exemple;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.DemoProject;

/**
 * @author Alexandre Blansché
 * Recherche aléatoire
 */
public class RandomSearch extends DemoProject {
    private int length;

    /**
     * Méthode d'évaluation de la solution
     *
     * @param evaluation
     * @throws InvalidProjectException
     */
    public RandomSearch( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        this.addAuthor( "Alexandre Blansché" );
        this.setMethodName( "Exemple" );
    }

    @Override
    public void initialization() {
        this.length = this.problem.getLength();
    }

    @Override
    public void loop() {
        Path path = new Path( this.length );
        this.evaluation.evaluate( path );
    }
}
