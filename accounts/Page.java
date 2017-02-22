package accounts;

import java.util.HashSet;
import java.util.Iterator;

import util.Contract;

/**
 * Cette classe définit les comptes de type Page
 * 
 * @cons <pre>
 * $DESC$ Une page pour laquelle le nom et la liste des administrateurs est donnée
 * $ARGS$ String name, HashSet<Utilisateur> admins
 * $PRE$ 
 *  name != null
 *  admins != null
 * $POST$ 
 *  getAdmins() == admins
 *  getName() == name
 * 
 * 
 * $DESC$ Une page pour laquelle seul le nom est donné
 * $ARGS$ String name
 * $PRE$ 
 *  name != null
 * $POST$
 *  getName() == name
 *  getAdmins() != null </pre>
 */
public class Page extends Sommet {

    // ATTRIBUTS
    
    private HashSet<Utilisateur> admins;
    
    // CONSTRUCTEUR
    
    public Page(String name, HashSet<Utilisateur> admins) {
        super(name);
        Contract.checkCondition(admins != null);
        
        this.admins = admins;
    }
    
    public Page(String name) {
        super(name);
        
        admins = new HashSet<Utilisateur>();
    }
    
    // REQUETES
    
    /** Renvoie l'ensemble des administrateurs de la page
     */
    public HashSet<Utilisateur> getAdmins() {
        return admins;
    }
    
    public String toString() {
        return getName();
    }
    
    /** Renvoie les ID des administrateurs sous forme de chaîne de caractères
     */
    public String getAdminsId() {
        String str = "";
        
        Iterator<Utilisateur> it = admins.iterator();
        
        if (it.hasNext()) {
            str = String.valueOf(it.next().getId());
            
            while (it.hasNext()) {
                str += ", " + it.next().getId();
            }
        }
        
        return str;
    }
    
    /*
     * Définition de l'ordre de comparaison pour les pages
     */
    public int compareTo(Sommet s) {
        if (s instanceof Utilisateur) {
            return this.getName().compareTo(((Utilisateur) s).getFullName());
        } else {
            return this.getName().compareTo(s.getName());
        }
    }
    
    // COMMANDES
    
    /** Ajoute un utilisateur comme administrateur de la page
     *  @pre <pre>
     *   u != null </pre>
     *  
     *  @post <pre>
     *   getAdmins().contains(u); </pre>
     */
    public void addAdmin(Utilisateur u) {
        Contract.checkCondition(u != null);
        
        admins.add(u);
    }
    
    /** Retire un administrateur de la page
     * @pre <pre>
     *  u != null
     *  getAdmins().contains(u) </pre>
     * 
     * @post <pre>
     *  !getAdmins().contains(u) </pre>
     */
    public void removeAdmin(Utilisateur u) {
        Contract.checkCondition(u != null);
        Contract.checkCondition(admins.contains(u));
        
        admins.remove(u);
    }
}
