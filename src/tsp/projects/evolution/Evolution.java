package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

public class Evolution extends CompetitorProject {

    public Evolution( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        addAuthor( "Pierre Henselmann" );
        setMethodName( "Natürliche Selektion" );
    }

    @Override
    public void initialization() {

    }

    @Override
    public void loop() {

    }

}
