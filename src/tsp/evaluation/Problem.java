package tsp.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author Alexandre Blansché
 * Instance du TSP
 * Ensemble de "villes" à visiter
 */
public final class Problem
{
    private static final String directory = "data";
    private String name;
	private int length;
	private double [][] data;
    
    /**
     * @return La liste des problèmes disponibles
     */
    public static ArrayList <Problem> getProblems ()
    {
        File directory = new File (Problem.directory);
        File [] files = directory.listFiles ();
        Arrays.sort(files);
        ArrayList <Problem> problems = new ArrayList <Problem> ();
        for (File file: files)
            problems.add (new Problem (file.getAbsolutePath ()));
        return problems;
    }

	private Problem (String filename)
	{
	    String [] parts = filename.split ("/|\\.");
	    this.name = parts [parts.length - 2];
		this.length = 0;
		try
		{
			BufferedReader in = new BufferedReader (new FileReader (new File (filename)));
			String line = null;
			while ((line = in.readLine ()) != null)
				this.length++;
			this.data = new double [this.length][2];in = new BufferedReader (new FileReader (new File (filename)));
			int row = 0;
			while ((line = in.readLine ()) != null)
			{
				StringTokenizer tokenizer = new StringTokenizer (line);
				int column = 0;
				while (tokenizer.hasMoreElements ())
				{
					this.data [row][column] = Double.parseDouble (tokenizer.nextToken ());
					column++;
				}
				row++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Le nom du problème
	 */
	public String getName ()
	{
	    return this.name;
	}
	
	@Override
	public String toString ()
	{
	    return this.name + ": " + this.length;
	}
	
	/**
	 * @param index L'indice d'une "ville"
	 * @return Les coordonnées de la "ville"
	 */
	public Coordinates getCoordinates (int index)
	{
		return new Coordinates (this.data [index][0], this.data [index][1]);
	}

	/**
	 * @return Le nombre de "villes"
	 */
	public int getLength ()
	{
		return this.length;
	}	

	/**
	 * @return Une copie du tableau des coordonnées
	 */
	public double [][] getData ()
	{
		double [][] data;
		data = new double [this.data.length][this.data [0].length];
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < this.data [0].length; j++)
				data [i][j] = this.data [i][j];
		return data;
	}	
}
