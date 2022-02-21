package tsp.projects;

import tsp.evaluation.Evaluation;

/**
 * @author Alexandre Blansché
 * C'est la classe à étendre pour le projet !
 */
public abstract class CompetitorProject extends Project {

	public CompetitorProject(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
	}
}
