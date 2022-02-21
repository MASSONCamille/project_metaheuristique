package tsp.output;

/**
 * @author Alexandre Blansché
 * Sortie standard
 * Classe singleton
 */
public class StandardOutput extends Output
{
    private static StandardOutput instance = null;
    
    private StandardOutput ()
    {        
    }
    
    /**
     * @return Accès à l'instance
     */
    public static StandardOutput getInstance ()
    {
        if (StandardOutput.instance == null)
            StandardOutput.instance = new StandardOutput ();
        return StandardOutput.instance;
    }
    
    @Override
    public void print (String string)
    {
        System.out.print (string);
    }

    @Override
    public void initialiaze ()
    {
    }
}
