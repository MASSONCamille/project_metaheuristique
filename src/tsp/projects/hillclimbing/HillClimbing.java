package tsp.projects.hillclimbing;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;
import tsp.projects.Transformations;

import static tsp.projects.Transformations.*;

public class HillClimbing extends CompetitorProject {

    private Path path;

    public HillClimbing( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        this.addAuthor( "Pierre Henselmann" );
        this.setMethodName( "Hill climbing" );
    }

    @Override
    public void initialization() {
        path = new Path( problem.getLength() );
    }

    @Override
    public void loop() {
        Path np = transformSwapSection( path );
        if ( evaluation.evaluate( np ) < evaluation.quickEvaluate( path ) )
            path = np;
    }

}
