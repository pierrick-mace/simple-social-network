package accounts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import util.Contract;

/**
 * Cette classe définit les sommets du graphe
 * 
 * @cons <pre>
 * $DESC$ Un sommet dont le nom est donné
 * $ARGS$ String name
 * $PRE$
 *  name != null
 * $POST$
 *  getNeighbors() != null
 *  getFollowers() != null
 *  getDistance(Sommet s) != null
 *  getName() != null
 *  getPageRank() == 1 </pre>
 */
public abstract class Sommet implements Comparable<Sommet> {

    // ATTRIBUTS
    
    private LinkedList<Sommet> neighbors;
    private LinkedList<Sommet> followers;
    private String name;
    private int id;
    private double pageRank;
    private Map<Sommet, Integer> distance;
    
    // CONSTRUCTEUR
    
    public Sommet(String name) {
        Contract.checkCondition(name != null);
        
        neighbors = new LinkedList<Sommet>();
        followers = new LinkedList<Sommet>();
        distance = new HashMap<Sommet, Integer>();
        this.name = name;
        this.pageRank = 1;
    }
    
    // REQUETES
    
    /** Renvoie la liste des sommets voisins sortants
     */
    public LinkedList<Sommet> getNeighbors() {
        return neighbors;
    }
    
    /** Renvoie la liste des sommets voisins entrants
     */
    public LinkedList<Sommet> getFollowers() {
        return followers;
    }
    
    /** Renvoie le nom du sommet
     */
    public String getName() {
        return name;
    }
    
    /** Renvoie l'ID du sommet
     */
    public int getId() {
        return id;
    }
    
    /** Renvoie l'ID des voisins entrants sous forme de chaîne de caractères
     */
    public String getFollowersId() {
        String str = "";
        
        Iterator<Sommet> it = getFollowers().iterator();
        
        if (it.hasNext()) {
            str = String.valueOf(it.next().getId());
            
            while (it.hasNext()) {
                str += ", " + it.next().getId();
            }
        }
        
        return str;
    }
    
    /** Renvoie le page rank du sommet
     */
    public double getPageRank() {
        return pageRank;
    }
    
    /** Renvoie la distance entre ce sommet et le sommet s
    */
    public int getDistance(Sommet s) {
        Contract.checkCondition(s != null);
        
        return distance.get(s);
    }
    
    // COMMANDES
    
    /** Ajoute un voisin sortant à ce sommet
     * @pre <pre>
     *  s != null
     *  !getNeighbors().contains(s) </pre>
     * 
     * @post <pre>
     *  getNeighbors().contains(s) </pre>
     */
    public void addNeighbor(Sommet s) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(!neighbors.contains(s));
        
        neighbors.add(s);
    }
    
    /** Enlève un voisin sortant de ce sommet
     * @pre <pre>
     *  s != null
     *  getNeighbors().contains(s) </pre>
     *  
     * @post <pre>
     *  !getNeighbors().contains(s) </pre>
     */
    public void removeNeighbor(Sommet s) {  
        Contract.checkCondition(s != null);
        Contract.checkCondition(neighbors.contains(s));
        
        neighbors.remove(s);
    }
    
    /** Supprime tous les voisins sortants de ce sommet
     * @post <pre>
     *  getNeighbors().size() == 0 </pre> 
    */
    public void removeAllNeighbors() {
        neighbors.clear();
    }
    
    /** Ajoute un voisin entrant à ce sommet
     * @pre <pre>
     *  s != null
     *  !getFollowers().contains(s) </pre>
     * 
     * @post <pre>
     *  getFollowers().contains(s) </pre>
     */
    public void addFollower(Sommet s) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(!followers.contains(s));
        
        followers.add(s);
    }
    
    /** Enlève un voisin entrant de ce sommet
     * @pre <pre>
     *  s != null
     *  getFollowers().contains(s) </pre>
     * 
     * @post <pre>
     *  !getFollowers().contains(s) </pre>
     */
    public void removeFollower(Sommet s) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(followers.contains(s));
        
        followers.remove(s);
    }
    
    /** Supprime tous les voisins entrants de ce sommet
     * @post <pre>
     *  getFollowers().size == 0 </pre>
     */
    public void removeAllFollowers() {
        followers.clear();
    }
    
    /** Donne l'ID 'id' au sommet
     * @pre <pre>
     *  id > 0 </pre>
     * 
     * @post <pre>
     *  getId() == id </pre>
     */
    public void setId(int id) {
        Contract.checkCondition(id > 0);
        
        this.id = id;
    }
    
    /** Donne le page rank 'pr' au sommet
     * @pre <pre>
     *  pr > 0 </pre>
     *
     * @post <pre>
     *  getPageRank() == pr;
     */
    public void setPageRank(double pr) {
        Contract.checkCondition(pr > 0);
        
        pageRank = pr;
    }
    
    /** Donne la distance 'dist' entre ce sommet et le sommet s
     * @pre <pre>
     *  s != null
     *  dist >= 0 </pre>
     * 
     * @post <pre>
     *  getDistance(s) == dist </pre>
     */
    public void setDistance(Sommet s, int dist) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(dist >= 0);
        
        distance.put(s, dist);
    }
    
    /** Supprime toutes les distances de ce sommet
     *  @post <pre>
     *      distance.size() == 0
     */
    public void clearDistance() {
        distance.clear();
    }
}
