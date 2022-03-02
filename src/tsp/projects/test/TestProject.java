package tsp.projects.test;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

public class TestProject extends DemoProject {

    private int length;

    public TestProject( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        this.addAuthor( "Pierre Henselmann" );
        this.setMethodName( "Test" );
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
