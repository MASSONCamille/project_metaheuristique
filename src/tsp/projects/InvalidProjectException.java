package tsp.projects;

/**
 * @author Alexandre Blansché
 * Exception indiquand qu'un bot n'est pas valide
 */
public class InvalidProjectException extends Exception
{
    /**
     * Constructeur...
     */
    public InvalidProjectException ()
    {
        super ("Bot invalide");
    }
    
    /**
     * @param message Le message à afficher
     */
    public InvalidProjectException (String message)
    {
        super ("Bot invalide : " + message);
    }
}
