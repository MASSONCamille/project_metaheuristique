package tsp.evaluation;

/**
 * @author Alexandre Blansché
 * Évaluation d'un chemin pour un problème TSP
 */
public final class Evaluation
{
	private double bestEvaluation;
	private Problem problem;
	
	/**
	 * Constructeur
	 * @param problem Le problème (liste de "villes")
	 */
	public Evaluation (Problem problem)
	{
		this.setBestEvaluation (Double.MAX_VALUE);
		this.problem = problem;
	}
	
	/**
	 * @param path Un chemin
	 * @return Indique si le chemin est valid ou pas
	 */
	public boolean isValid (Path path)
	{
		return this.isValid (path.getPath ());
	}
	
	/**
	 * @param path Un chemin
	 * @return Indique si le chemin est valid ou pas
	 */
	public boolean isValid (int [] path)
	{
		boolean valid = path.length == this.problem.getLength ();
		if (valid)
		{
			boolean [] exists = new boolean [path.length];
			for (int i = 0; i < exists.length; i++)
				exists [i] = false;
			for (int i : path)
				if (i < exists.length)
					exists [i] = true;
				else
					valid = false;
			for (int i = 0; i < exists.length; i++)
				if (exists [i] == false)
					valid = false;
		}
		return valid;
	}
	
	/**
	 * @param path Le chemin à évaluer
	 * @return Retourne la distance parcouru (ou Double.MAX_VALUE si le chemin est invalide ou si le temps est écoulé) 
	 */
	public double evaluate (Path path)
	{
	    double evaluation = Double.MAX_VALUE;
	    if (this.isValid (path))
	    {
	        evaluation = this.quickEvaluate (path);
	        if (evaluation < this.getBestEvaluation ())
	            if (!Thread.currentThread ().isInterrupted ())
	                this.setBestEvaluation (evaluation);
	    }
	    return evaluation;
	}
    
    /**
     * @param path Le chemin à évaluer
     * @return Retourne la distance parcouru SANS vérifier si le chemin est valide
     * et SANS mettre à jour la meilleure distance trouvée 
     */
    public double quickEvaluate (Path path)
    {
        double evaluation = 0;
        int [] p = path.getPath ();
        Coordinates c1 = this.problem.getCoordinates (p [0]);
        Coordinates c2 = null;
        for (int i = 1; i < p.length; i++)
        {
            c2 = this.problem.getCoordinates (p [i]);
            evaluation += c1.distance (c2);
            c1 = c2;
        }
        c2 = this.problem.getCoordinates (p [0]);
        evaluation += c1.distance (c2);
        return evaluation;
    }

	/**
	 * @return Le problème TSP
	 */
	public Problem getProblem ()
	{
		return this.problem;
	}

	/**
	 * @return L'évaluation de la meilleure solution
	 */
	public double getBestEvaluation ()
	{
		return this.bestEvaluation;
	}

	private void setBestEvaluation (double bestEvaluation)
	{
		this.bestEvaluation = bestEvaluation;
	}
}
