package tsp.projects.evolution;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Path;

import java.util.Objects;

public class Individu implements Comparable<Individu>{
    private Path path;
    private Double distance;
    private static Evaluation eva = null;

    public static void initEva(Evaluation eval){
        eva = eval;
    }

    public Evaluation getEva() throws ExceptionEvaNoInit{
        if(eva == null) throw new ExceptionEvaNoInit();
        return eva;
    }

    public Individu() {
        this.path = null;
        this.distance = null;
    }

    public Individu(Path path){
        this.path = path;
        try {
            this.distance = getEva().quickEvaluate(path);
        } catch (ExceptionEvaNoInit e) {
            e.printStackTrace();
        }
    }

    public void setPath(Path path){
        this.path = path;
        try {
            this.distance = getEva().quickEvaluate(path);
        } catch (ExceptionEvaNoInit e) {
            e.printStackTrace();
        }
    }

    public Path getPath() {
        return path;
    }

    public Double getDistance() {
        return distance;
    }

    public boolean Valide(){
        boolean valide = false;
        try {
            valide = getEva().evaluate(path) == distance;
        } catch (ExceptionEvaNoInit e) {
            e.printStackTrace();
        }
        return valide;
    }

    @Override
    public int compareTo(Individu o) {
        return (int)Math.round(this.getDistance() - o.getDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individu individu = (Individu) o;

        return Objects.equals(distance, individu.distance);
    }

}
