package tsp.projects.hillclimbing;

import tsp.evaluation.Coordinates;
import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.CompetitorProject;
import tsp.projects.DemoProject;
import tsp.projects.InvalidProjectException;

import java.util.*;

import static tsp.projects.Transformations.transformSwapSection;

public class GrimperColine extends DemoProject {

    public GrimperColine(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor( "Camille Masson" );
        this.setMethodName( "Grimper Coline" );
    }

    @Override
    public void initialization() {
        Path path = HillClimbing();
    }

    @Override
    public void loop() {

    }


    public Path HillClimbing() {
        int cpt = 0;
        int index;
        int distance = 0;

        Coordinates pointActuel;
        List<Integer> pasVu;
        int [] path = new int[problem.getLength()];
        pasVu = new ArrayList<>();

        Random random = new Random();
        int j = random.nextInt (problem.getLength());
        pointActuel = this.problem.getCoordinates(j);
        path[0] = j;

        for (int i = 0; i < problem.getLength(); i++) {
            if (i != j) {
                pasVu.add(i);
            }
        }

        int minVoisinDistance;
        int minVoisinIndex;

        while (pasVu.size() != 0) {
            cpt++;
            minVoisinIndex = pasVu.get(0);
            minVoisinDistance = (int) pointActuel.distance(this.problem.getCoordinates(pasVu.get(0)));
            index = 0;
            for (int i = 1; i < pasVu.size(); i++) {
                if (minVoisinDistance > pointActuel.distance(this.problem.getCoordinates(pasVu.get(i)))) {
                    minVoisinDistance = (int) pointActuel.distance(this.problem.getCoordinates(pasVu.get(i)));
                    minVoisinIndex = pasVu.get(i);
                    index = i;
                }
            }
            distance += minVoisinDistance;
            path[cpt] = minVoisinIndex;
            pointActuel = this.problem.getCoordinates(minVoisinIndex);
            pasVu.remove(index);
        }
        Path pathFin = new Path(path);
        return pathFin;
    }
}
