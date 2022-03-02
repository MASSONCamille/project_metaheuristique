package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.ArrayList;
import java.util.HashMap;

public class Evolution extends CompetitorProject {

    private static int NB_INDIVIDUS = 10;
    private HashMap<Path, Double> liste = new HashMap<>();

    public Evolution( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Pierre Henselmann" );
        setMethodName( "Natürliche Selektion" );
    }

    @Override
    public void initialization() {
        for ( int i = 0 ; i < NB_INDIVIDUS ; i++ ) {
            Path path = new Path( problem.getLength() );
            liste.put( path, evaluation.quickEvaluate( path ) );
        }
    }

    @Override
    public void loop() {

    }

}
