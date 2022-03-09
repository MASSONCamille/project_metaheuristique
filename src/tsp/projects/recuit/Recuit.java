package tsp.projects.recuit;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.concurrent.ThreadLocalRandom;

import static tsp.projects.Transformations.*;

public class Recuit extends DemoProject {

    private double temperature;
    private int attemptedTransforms = 0;
    private int acceptedTransforms = 0;
    private static double lambda = 0.99;
    private Path path;
    private double p0 = 0.4;

    public Recuit( Evaluation evaluation ) throws InvalidProjectException {
        super( evaluation );
        this.addAuthor( "Pierre Henselmann" );
        this.setMethodName( "Recuit simulé" );
    }

    @Override
    public void initialization() {
        path = new Path( problem.getLength() );
        double initDelta = initLoop();
//        System.out.println( initDelta );
        temperature = -1 * ( initDelta / Math.log( p0 ) );
        //System.out.println( "temperature : " + temperature );
    }

    public double initLoop() {
        double sum = 0;

        for ( int i = 0 ; i < 100 ; i++ ) {
            Path np = transformSwapSection( path );
            double delta = evaluation.evaluate( np ) - evaluation.quickEvaluate( path );
//            System.out.println(delta);
            sum += delta;
            path = np;
        }

        return Math.abs( sum / 100 );
    }

    @Override
    public void loop() {
        Path np = transformSwapSection( path );
        double rand = Math.random();
        double delta = energyDelta( np );
        double pi = Math.min( 1, Math.exp( -1 * ( Math.abs( delta ) / temperature ) ) );
//        System.out.println( "pi : " + pi );
        if ( delta < 0 ) {
            if ( rand >= pi ) {
                path = np;
                acceptedTransforms++;
            }
        } else if ( rand < pi ) {
            path = np;
            acceptedTransforms++;
        }

        evaluation.evaluate( path );
        attemptedTransforms++;
        updateTemperature();
    }

    public void updateTemperature() {
//        temperature *= lambda;

//        System.out.println( "temperature : " + temperature );

        if ( attemptedTransforms >= 100 || acceptedTransforms >= 12 ) {
            temperature *= lambda;
            if ( acceptedTransforms >= 100 ) attemptedTransforms = 0;
            if ( acceptedTransforms >= 12 ) acceptedTransforms = 0;
        }
    }

    public double energyDelta( Path p ) {
        return evaluation.quickEvaluate( p ) - evaluation.quickEvaluate( path );
    }

}
