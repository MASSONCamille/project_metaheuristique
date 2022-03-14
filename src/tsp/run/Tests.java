package tsp.run;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.evolution.Individu;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

import static tsp.projects.Transformations.crossing;
import static tsp.projects.Transformations.simpleCrossover;

public class Tests {
    public static void main( String[] args ) {
        Problem.getProblems();
        ArrayList<Problem> problems = Problem.getProblems();
        Problem problem = problems.get(0);

        Evaluation evaluation = new Evaluation(problem);

        Individu.initEva(evaluation);
        ArrayList<Individu> pop = new ArrayList<>();
        for ( int i = 0 ; i < 5 ; i++ ) {
            pop.add(new Individu(new Path(problem.getLength())));
        }

        print(pop);
        System.out.println();

        Collections.sort(pop);
        pop = new ArrayList<>(pop.subList(0,2));
        print(pop);


    }

    public static void print(ArrayList<Individu> pop){
        System.out.println("pop:");
        for (Individu ind : pop) {
            System.out.println("Eval: " + ind.getDistance());
        }
    }
}
