package tsp.output;

import java.util.ArrayList;

/**
 * @author Alexandre Blansché
 * Classe pour gérer les affichages lors des parties d'Awele
 */
public class OutputWriter
{
    private ArrayList <Output> outputs;
    private ArrayList <Output> debug;

    /**
     * Constructeur...
     */
    public OutputWriter ()
    {
        this.outputs = new ArrayList <Output> ();
        this.debug = new ArrayList <Output> ();
    }
    
    /**
     * Rajoute une sortie
     * @param output Sortie à rajouter
     */
    public void addOutput (Output output)
    {
        output.initialiaze ();
        this.outputs.add (output);
    }
    
    /**
     * Rajoute une sortie de débuggage
     * @param output Sortie à rajouter
     */
    public void addDebug (Output output)
    {
        output.initialiaze ();
        this.debug.add (output);
    }
    
    /**
     * Saut de ligne
     */
    public void print ()
    {
        for (Output output: this.outputs)
            output.println ();
        for (Output debug: this.debug)
            debug.println ();
    }
    
    protected void printDebug ()
    {
        for (Output debug: this.debug)
            debug.println ();
    }
    
    /**
     * Affichage d'un message
     * @param object Objet à afficher
     */
    public void print (Object object)
    {
        for (Output output: this.outputs)
            output.print (object);
        for (Output debug: this.debug)
            debug.print (object);
    }
    
    /**
     * Affichage d'un message avec retour à la ligne
     * @param object Objet à afficher
     */
    public void println (Object object)
    {
        for (Output output: this.outputs)
            output.println (object);
        for (Output debug: this.debug)
            debug.println (object);
    }

    /**
     * Affichage d'un message de débuggage
     * @param object Objet à afficher
     */
    public void printDebug (Object object)
    {
        for (Output debug: this.debug)
            debug.print (object);
    }

    /**
     * Affichage d'un message de débuggage avec retour à la ligne
     * @param object Objet à afficher
     */
    public void printlnDebug (Object object)
    {
        for (Output debug: this.debug)
            debug.println (object);
    }
}
