package tsp.projects.pierrecamillemk1;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;
import tsp.evaluation.Problem;
import tsp.projects.CompetitorProject;
import tsp.projects.InvalidProjectException;

import java.util.Random;

public class PierreCamilleMk1 extends CompetitorProject {

    private Path path;


    public PierreCamilleMk1(Evaluation evaluation) throws InvalidProjectException {
        super(evaluation);
        this.addAuthor("Henselmann & Masson");
        this.setMethodName("Mk1");
    }

    @Override
    public void initialization() {
        this.path = new Path(problem.getLength());
    }

    @Override
    public void loop() {
        evaluation.evaluate(this.path);
        this.echange2ville();
    }

    public void echange2ville() {
        Random random = new Random();
        int a,b;
        a = random.nextInt(path.getPath().length);
        do {
            b = random.nextInt(path.getPath().length);
        }while (a == b);

        Path newp = this.path;
        newp.getPath()[a] = path.getPath()[b];
        newp.getPath()[b] = path.getPath()[a];

        this.path = newp;
    }
}
