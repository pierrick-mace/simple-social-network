package accounts;

import java.util.Iterator;

import util.Contract;

/**
 * Cette classe définit les comptes de type Utilisateur
 *
 *  @cons <pre>
 *  $DESC$ Un utilisateur pour lequel le prénom, le nom et l'âge sont donnés
 *  $ARGS$ String prenom, String nom, int age
 *  $PRE$
 *      nom != null
 *      prenom != null
 *      age > 0
 *  $POST$
 *      getFirstName() == prenom
 *      getName() == name
 *      getAge() == age </pre>
 */
public class Utilisateur extends Sommet {
    
    // ATTRIBUTS

    private String prenom;
    private int age;
    
    // CONSTRUCTEUR
    
    public Utilisateur(String prenom, String nom, int age) {
        super(nom);
        Contract.checkCondition(nom != null);
        Contract.checkCondition(prenom != null);
        Contract.checkCondition(age > 0);
        
        this.prenom = prenom;
        this.age = age;
    }
    
    // REQUETES
    
    /** Retourne le prénom de l'utilisateur
     */
    public String getFirstName() {
        return prenom;
    }
    
    /** Retourne le nom complet de l'utilisateur (Prénom + Nom)
     */
    public String getFullName() {
        return prenom + " " + super.getName();
    }
    
    /** Retourne l'age de l'utilisateur
    */
    public int getAge() {
        return age;
    }
    
    public String toString() {
        return getFullName();
    }
    
    /** Retourne les ID des sommets voisins sous forme de chaîne de caractères 
    */
    public String getNeighborsId() {
        String str = "";
        
        Iterator<Sommet> it = getNeighbors().iterator();
        
        if (it.hasNext()) {
            str = String.valueOf(it.next().getId());
            
            while (it.hasNext()) {
                str += ", " + it.next().getId();
            }
        }
        
        return str;
    }
    
    /* Définition de l'ordre de comparaison pour les utilisateurs
     */
    public int compareTo(Sommet s) {
        if (s instanceof Utilisateur) {
            return this.getFullName().compareTo(((Utilisateur) s).getFullName());
        } else {
            return this.getFullName().compareTo(s.getName());
        }
    }
}
