package tsp.projects;

import java.util.ArrayList;

import tsp.evaluation.Evaluation;
import tsp.evaluation.Problem;
import tsp.evaluation.Solution;

/**
 * @author Alexandre Blansché
 * Méthode métaheuristique pour résoudre la TSP
 */
public abstract class Project implements Runnable {
    protected Evaluation evaluation;
    protected Problem problem;
    private String name;
    private ArrayList<String> authors;

    /**
     * Constructeur
     *
     * @param evaluation Méthode d'évaluation de la solution
     * @throws InvalidProjectException
     */
    public Project( Evaluation evaluation ) throws InvalidProjectException {
        this.evaluation = evaluation;
        this.problem = evaluation.getProblem();
        this.name = "";
        this.authors = new ArrayList<String>();
    }

    /**
     * Fonction pour donner un nom au bot (soyez imaginatifs !)
     * Doit être appelé dans le constructeur des classes dérivées
     *
     * @param name Le nom du bot
     */
    protected void setMethodName( String name ) {
        this.name = name;
    }

    /**
     * Fonction pour définir tous les auteurs
     *
     * @param names Prénoms et noms des étudiants
     * @throws InvalidProjectException Il ne peut y avoir que deux auteurs au maximum !
     */
    protected void setAuthors( String... names ) throws InvalidProjectException {
        if ( names.length > 2 )
            throw new InvalidProjectException( "Trop d'auteurs pour ce bot" );
        else {
            this.authors = new ArrayList<String>();
            for ( String name : names )
                this.authors.add( name );
        }
    }

    /**
     * Fonction pour rajouter un auteur
     *
     * @param name Prénom et nom de l'étudiant
     * @throws InvalidProjectException Il ne peut y avoir que deux auteurs au maximum !
     */
    protected void addAuthor( String name ) throws InvalidProjectException {
        if ( this.authors.size() < 2 )
            this.authors.add( name );
        else
            throw new InvalidProjectException( "Trop d'auteurs pour cet algorithme !" );
    }

    /**
     * @return La meilleure solution découverte par la méthode
     */
    public Solution getSolution() {
        return new Solution( this.authors, this.name, this.problem.getName(), this.evaluation.getBestEvaluation() );
    }

    /**
     * Initialisation de l'algorithme
     */
    public abstract void initialization();

    /**
     * Boucle principale de l'algorithme
     */
    public abstract void loop();

    @Override
    public void run() {
        try {
            this.initialization();
        } catch ( Exception e ) {
        }
        while ( !Thread.currentThread().isInterrupted() )
            try {
                this.loop();
            } catch ( Exception e ) {
            }
    }
}
