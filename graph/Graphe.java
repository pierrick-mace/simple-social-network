package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import accounts.Page;
import accounts.Sommet;
import accounts.Utilisateur;
import exception.BadSyntaxException;
import util.Contract;

/**
 * Cette classe définit le modèle du graphe
 *
 * @cons <pre>
 *  $DESC$ Crée un modèle du graphe
 *  $POST$
 *      getVertices() != null
 *      id == 1
 */
public class Graphe extends Observable {
    
    // CONSTANTES
    
    private static final Pattern REGEX_USER 
        = Pattern.compile("^\\{id = (?<id>[0-9]+), name = (?<name>.+), firstname = (?<firstname>.+), age = (?<age>[0-9]+), neighbors = \\[(?<neighbors>(([0-9]+, )+[0-9]+)|[0-9]*)\\]\\}$");
    
    private static final Pattern REGEX_PAGE 
        = Pattern.compile("^\\{id = (?<id>[0-9]+), name = (?<name>.+), admins = \\[(?<admins>(([0-9]+, )+[0-9]+)|[0-9]*)\\]\\}$");
        
    // ATTRIBUTS
    
    private HashSet<Sommet> vertices;
    private int id;
    
    // CONSTRUCTEUR
    
    public Graphe() {
        vertices = new LinkedHashSet<Sommet>();
        id = 1;
    }
    
    // REQUETES
    
    /** Retourne le nombre de sommets dans le graphe.
     */
    public int getVerticesNb() {
        return vertices.size();
    }
    
    /** Retourne le nombre d'arcs dans le graphe.
     */
    public int getEdgesNb() {
        int nb = 0;
        
        for (Sommet s : vertices) {
            nb += s.getNeighbors().size();
        }
        
        return nb;
    }
    
    /** Retourne le degré sortant du sommet s
     * @pre <pre>
     * s != null </pre>
     */
    public int getVerticeOutDegree(Sommet s) {
        Contract.checkCondition(s != null);
        
        return s.getNeighbors().size();
    }

    /** Retourne l'ensemble des sommets du graphe
     */
    public Set<Sommet> getVertices() {
        return vertices;
    }
    
    /** Retourne l'ensemble des sommets triés par nom
     */
    public Set<Sommet> getVerticesSortedByName() {
        return new TreeSet<Sommet> (vertices);
    }

    /** Retourne l'ensemble des sommets triés par degré sortant
     */
    public Set<Sommet> getVerticesSortedByOutDegree() {
        LinkedList<Sommet> verticesList = new LinkedList<Sommet>(vertices);
        Collections.sort(verticesList, new Comparator<Sommet>() {
            public int compare(Sommet s1, Sommet s2) {
                int s1Neighbors = s1.getNeighbors().size();
                int s2Neighbors = s2.getNeighbors().size();
                
                return (s1Neighbors > s2Neighbors ? -1 : (s1Neighbors == s2Neighbors ? 0 : 1));
            }
        });
        
        return new LinkedHashSet<Sommet>(verticesList);
    }
    
    /** Retourne l'ensemble des sommets triés par page rank
     */
    public Set<Sommet> getVerticesSortedByPageRank() {
        LinkedList<Sommet> verticesList = new LinkedList<Sommet>(vertices);
        Collections.sort(verticesList, new Comparator<Sommet>() {
            public int compare(Sommet s1, Sommet s2) {
                double s1PageRank = s1.getPageRank();
                double s2PageRank = s2.getPageRank();
                
                return (s1PageRank > s2PageRank ? -1 : (s1PageRank == s2PageRank ? 0 : 1));
            }
        });
        
        return new LinkedHashSet<Sommet>(verticesList);
    }
    
    /** Retourne les arcs du graphe sous forme d'une table de hachage (Sommet, Liste des sommets associés)
     */
    public Map<Sommet, List<Sommet>> getEdges() {
        HashMap<Sommet, List<Sommet>> edges = new HashMap<Sommet, List<Sommet>>();
        
        for (Sommet s : vertices) {
            edges.put(s, s.getNeighbors());
        }
        
        return edges;
    }
    
    /** Retourne le sommet de nom 'name' si il existe,
     *  Retourne null sinon
     * @pre <pre>
     *  name != null </pre>
     */
    public Sommet getVerticeByName(String name) {
        for (Sommet s : vertices) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        
        return null;
    }
    
    /** Retourne un sommet d'id 'id' si il existe,
     *  Retourne null sinon 
     * 
     * @pre <pre>
     *  id > 0 </pre>
     */
    public Sommet getVerticeById(int id) {
        Contract.checkCondition(id > 0);
        
        for (Sommet s : vertices) {
            if (s.getId() == id) {
                return s;
            }
        }
        
        return null;
    }
    
    /** Retourne le nombre de comptes de type Page
     */
    public int getPagesNb() {
        int nb = 0;
        
        for (Sommet s : vertices) {
            if (s instanceof Page) {
                nb++;
            }
        }
        
        return nb;
    }
    
    /** Retourne le nombre de comptes de type Utilisateur
     */
    
    public int getUsersNb() {
        int nb = 0;
        
        for (Sommet s : vertices) {
            if (s instanceof Utilisateur) {
                nb++;
            }
        }
        
        return nb;
    }
    
    /** Retourne l'age moyen des utilisateurs
     */
    public float getAverageAge() {
        float age = 0;
        float nb = 0;
        
        for (Sommet s : vertices) {
            if (s instanceof Utilisateur) {
                age += ((Utilisateur) s).getAge();
                nb++;
            }
        }
        
        return age / nb;
    }
    
    /** Retourne l'ensemble des comptes administrateurs de la page p
     * @pre <pre>
     *  p != null
     *  p instanceof Page
     *  vertices.contains(p)
     */
    public Set<Utilisateur> getPageAdmins(Sommet p) {
        Contract.checkCondition(p != null);
        Contract.checkCondition(p instanceof Page);
        Contract.checkCondition(vertices.contains(p));
        
        return ((Page) p).getAdmins();  
    }
    
    // COMMANDES
    
    /** Ajoute un sommet au graphe.
     * 
     * @pre <pre>
     *  s != null
     *  !getVertices().contains(s) </pre>
     * 
     * @post <pre>
     *  getVertices().contains(s)
     *  id == (old) id + 1 </pre>
     */
    
    public void addVertice(Sommet s) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(!vertices.contains(s));
        
        vertices.add(s);
        s.setId(id++);
        
        setChanged();
        notifyObservers();
    }
    
    /** Enlève un sommet du graphe.
     * 
     * @pre <pre>
     *  s != null
     *  getVertices().contains(s) </pre>
     * 
     * @post <pre>
     *  !getVertices().contains(s) </pre>
     *  forall v in vertices
     *      !v.getNeighbors().contains(s)
     *      !v.getFollowers().contains(s)
     * 
     *      v instance of Page && s instanceof Utilisateur && v.getAdmins().contains(s) ==>
     *          !v.getAdmins().contains(s)
     *          
     */
    
    public void removeVertice(Sommet s) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(vertices.contains(s));
        
        for (Sommet v : vertices) {
            if (v.getNeighbors().contains(s)) {
                v.removeNeighbor(s);
            }
            
            if (v.getFollowers().contains(s)) {
                v.removeFollower(s);
            }
            
            if (v instanceof Page && s instanceof Utilisateur) {
                Page p = (Page) v;
                if (p.getAdmins().contains(s)) {
                    p.removeAdmin((Utilisateur) s);
                }
            }
        }
        
        s.removeAllNeighbors();
        s.removeAllFollowers();
            
        vertices.remove(s);
        
        setChanged();
        notifyObservers();
    }
    
    /** Ajoute un arc entre le sommet s et v
     * @pre <pre>
     *  s != null
     *  v != null 
     *  s instanceof Utilisateur </pre>
     *    
     * @post <pre>
     *  s.getNeighbors().contains(v) 
     *  v.getFollowers().contains(s) </pre>
     */
    public void addEdge(Sommet s, Sommet v) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(v != null);
        Contract.checkCondition(s instanceof Utilisateur);
        
        s.addNeighbor(v);
        v.addFollower(s);
        
        setChanged();
        notifyObservers();
    }
    
    /** Supprime un arc entre le sommet s et v
     * @pre <pre>
     *  s != null
     *  v != null
     *  s.getNeighbors().contains(v) </pre>
     * 
     * @post <pre>
     *  !s.getNeighbors().contains(v) 
     *  !v.getFollowers().contains(s) </pre>
     */
    public void removeEdge(Sommet s, Sommet v) {
        Contract.checkCondition(s != null);
        Contract.checkCondition(v != null);
        Contract.checkCondition(s.getNeighbors().contains(v));
        
        s.removeNeighbor(v);
        v.removeFollower(s);
        
        setChanged();
        notifyObservers();
    }
    
    /** Sauvegarde le graphe dans un fichier.
     * 
     * @pre <pre>
     *  file != null </pre>
     * 
     * @post <pre>
     *  le contenu du graphe est sauvegardé dans file </pre>
     * 
     * @throws <pre>
     *  IOException: Erreur d'entrée/sortie </pre>
     */
    
    public void saveGraph(File file) throws IOException {
        Contract.checkCondition(file != null);
        
        PrintWriter output 
            = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        
        try {
        
            String str = "";
            
            for (Sommet s : vertices) {
                str = "";
                
                if (s instanceof Utilisateur) {
                    Utilisateur u = (Utilisateur) s;
                    str = "{id = " + u.getId() + ", name = " + u.getName() + ", firstname = " + u.getFirstName() 
                        + ", age = " + u.getAge() + ", neighbors = [" 
                        + u.getNeighborsId() + "]}\n"; 
                } else if (s instanceof Page) {
                    Page p = (Page) s;
                    str = "{id = " + p.getId() + ", name = " + p.getName() 
                        + ", admins = [" + p.getAdminsId() + "]}\n";
                }
                
                output.print(str);
            }

        } finally {
            output.close();
        }
        
        setChanged();
        notifyObservers();
    }
    
    /** Charge le graphe depuis un fichier
     * @pre <pre>
     *  file != null </pre>
     * 
     * @post <pre>
     *  Le graphe est chargé avec le contenu du fichier 
     *  this.id == max(forall v in vertices: v.getId()) + 1</pre> 
     *          
     *
     * @throws <pre>
     *  IOException: Erreur d'entrée/sortie
     *  BadSyntaxException: La ligne lue dans le fichier ne correspond ni à un utilisateur, ni à une page, 
     *                      le fichier est donc corrompu </pre>
    */
    
    public void loadGraph(File file) throws IOException, BadSyntaxException {
        Contract.checkCondition(file != null);
        
        this.clear();
        
        BufferedReader input
            = new BufferedReader(new FileReader(file));
        
        Map<Integer, List<Integer>> neighborsMap = new HashMap<Integer, List<Integer>>();
        
        int highestId = 0;
        
        try {
            String line = input.readLine();
            
            while (line != null) {
                
                if (REGEX_USER.matcher(line).matches()) {
                    // La ligne correspond à un utilisateur
                    Matcher m = REGEX_USER.matcher(line);
                    if (m.find()) {
                        int id = Integer.parseInt(m.group("id"));
                        String name = m.group("name");
                        int age = Integer.parseInt(m.group("age"));
                        String firstname = m.group("firstname");
                        String strneighbors = m.group("neighbors");
                        
                        Utilisateur u = new Utilisateur(firstname, name, age);
                        u.setId(id);
                        
                        
                        if (!strneighbors.isEmpty()) {
                            LinkedList<Integer> neighborsList = new LinkedList<Integer>();
                            StringTokenizer st = new StringTokenizer(strneighbors, ",");
                            
                            while (st.hasMoreTokens()) {
                                String s = st.nextToken();
                                neighborsList.add(Integer.parseInt(s.trim()));
                            }
                            
                            neighborsMap.put(id, neighborsList);
                            
                        }
                        
                        vertices.add(u);
                        
                        highestId = highestId > id ? highestId : id;
                    }
                    
                } else if (REGEX_PAGE.matcher(line).matches()) {
                    // La ligne correspond à une page
                    Matcher m = REGEX_PAGE.matcher(line);
                    if (m.find()) {
                        int id = Integer.parseInt(m.group("id"));
                        String name = m.group("name");
                        String stradmins = m.group("admins");
                        
                        Page p = new Page(name);
                        p.setId(id);
                        
                        if (!stradmins.isEmpty()) {
                        
                            LinkedList<Integer> adminsList = new LinkedList<Integer>();
                            StringTokenizer st = new StringTokenizer(stradmins, ",");
                            
                            while (st.hasMoreTokens()) {
                                String s = st.nextToken();
                                adminsList.add(Integer.parseInt(s.trim()));
                            }
                            
                            neighborsMap.put(id, adminsList);
                        }
                        
                        vertices.add(p);
                        
                        highestId = highestId > id ? highestId : id;
                    }
                
                } else {
                    this.clear();
                    throw new BadSyntaxException();
                }
                
                line = input.readLine();
            }
            
        for (int i : neighborsMap.keySet()) {
            Sommet s = getVerticeById(i);
            
            if (s instanceof Utilisateur) {
                for (int id : neighborsMap.get(i)) {
                    s.addNeighbor(getVerticeById(id));
                    getVerticeById(id).addFollower(s);
                }
            } else {
                for (int id : neighborsMap.get(i)) {
                    ((Page) s).addAdmin((Utilisateur) getVerticeById(id));
                }
            }
        }
        
        this.id = highestId + 1;
            
        } finally {
            input.close();
        }
        
        setChanged();
        notifyObservers();
    }
    
    
    /** Génère le page rank pour chaque sommet
    */
    
    public void generatePageRank() {
        
        // Initialisation du page rank à 1
        for (Sommet s : vertices) {
            s.setPageRank(1);
        }
        
        int i = 0;

        // Calcul du page rank
        while (i <= 100) {
            for (Sommet s : vertices) {
                double followersPr = 0;
                
                for (Sommet v : s.getFollowers()) {
                    followersPr += v.getPageRank() / this.getVerticeOutDegree(v);
                }
                
                s.setPageRank((0.15 / vertices.size()) + (0.85 * followersPr));
            }
            
            i++;
        }
    }
    
    
    /** Calcule la plus petite distance entre le sommet s et les sommets du graphe
     * @pre <pre>
     *  s != null </pre>
     */
    public void computeSmallestDistanceFrom(Sommet s) {
        Contract.checkCondition(s != null);
        
        // Initialisation des distances entre chaque sommet et la source à 10000000
        for (Sommet u : vertices) {
            u.setDistance(s, 10000000);
        }
        
        // Initialisation de la distance de s à 0
        s.clearDistance();
        
        // p prend l'ensemble des sommets du graphe
        HashSet<Sommet> p = new HashSet<Sommet>(vertices);
        
        // Calcul de la plus petite distance entre le sommet et la source
        while (!p.isEmpty()) {
            // On prend le sommet u le plus proche de la source
            Sommet u = getClosestVertice(s, p);
            if (u != null) {
                // On supprime u de p
                p.remove(u);
                
                for (Sommet v : u.getNeighbors()) {
                    int alt = u.getDistance(s) + 1;
                    if (alt <= v.getDistance(s)) {
                        v.setDistance(s, alt);
                    }
                }
            }
        }
    }
    
    // OUTILS
    
    private void clear() {
        for (Sommet s : vertices) {
            s.removeAllNeighbors();
            s.removeAllFollowers();
        }
        
        vertices.clear();
    }
    
    private Sommet getClosestVertice(Sommet s, Set<Sommet> verticesSet) {
        Sommet result = null;
        int dist;
        
        Iterator<Sommet> it = verticesSet.iterator();
        
        if (it.hasNext()) {
            result = it.next();
            dist = result.getDistance(s);

            while (it.hasNext()) {
                Sommet v = it.next();
                if (v.getDistance(s) < dist) {
                    result = v;
                    dist = v.getDistance(s);
                }
            }
        }
        
        return result;
    }
}
