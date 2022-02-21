package tsp.projects;

import tsp.evaluation.Evaluation;


/**
 * @author Alexandre Blansché
 * Uniquement pour les projets en exemple.
 * Ce n'est pas la classe à étendre pour le projet, c'est la classe CompetitorProject qu'il faut étendre !
 */
public abstract class DemoProject extends Project {

	public DemoProject(Evaluation evaluation) throws InvalidProjectException {
		super(evaluation);
	}
}
