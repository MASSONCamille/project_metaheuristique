package tsp.evaluation;

/**
 * @author Alexandre Blansché
 * Classe pour représenter des coordonnées 2D et calculer la distance euclidienne
 */
public final class Coordinates
{
	private double x, y;
	
	/**
	 * @param x Abscisse
	 * @param y Ordonnée
	 */
	public Coordinates (double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @param c Coordonnées d'un autre point
	 * @return La distance entre les deux points
	 */
	public double distance (Coordinates c)
	{
		double dx = this.x - c.x;
		double dy = this.y - c.y;
		return Math.sqrt (dx * dx + dy * dy);
	}
}
