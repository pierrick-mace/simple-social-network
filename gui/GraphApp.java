package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import accounts.Page;
import accounts.Sommet;
import accounts.Utilisateur;
import exception.BadSyntaxException;
import graph.Graphe;

public class GraphApp {

    // ATTRIBUTS
    
    private Graphe model;
    private JFrame mainFrame;
    
    private JList<Sommet> userList;
    private DefaultListModel<Sommet> userListModel;
    private JList<Sommet> pageList;
    private DefaultListModel<Sommet> pageListModel;
    
    private JButton addUser;
    private JButton addPage;
    private JButton removeUser;
    private JButton removePage;
    private JButton userInfo;
    private JButton pageInfo;
    private JButton followButton;
    private JButton stopFollowing;
    private JButton setAdmin;
    private JButton removeAdmin;
    private JButton saveGraph;
    private JButton loadGraph;
    private JButton graphStats;
    private JButton verticesSortedByName;
    private JButton verticesSortedByOutDegree;
    private JButton verticesSortedByPageRank;
    private JButton edgesButton;
    
    private JFileChooser fileChooser;
    
    // CONSTRUCTEUR
    
    public GraphApp() {
        createModel();
        createView();
        placeComponents();
        createController();
    }
    
    // COMMANDES
    
    public void display() {
        refresh();
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
    
    // OUTILS
    
    // Instanciation du modèle
    private void createModel() {
        model = new Graphe();
    }
    
    // Instanciation des composants majeurs
    private void createView() {
        int width = 800;
        int height = 600;
        
        mainFrame = new JFrame("Graphe");
        mainFrame.setPreferredSize(new Dimension(width, height));
        
        userListModel = new DefaultListModel<Sommet>();
        pageListModel = new DefaultListModel<Sommet>();
        
        userList = new JList<Sommet>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pageList = new JList<Sommet>(pageListModel);
        pageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        addUser = new JButton("Ajouter Utilisateur");
        addPage = new JButton("Ajouter Page");
        removeUser = new JButton("Supprimer Utilisateur");
        removePage = new JButton("Supprimer Page");
        userInfo = new JButton("Infos Utilisateur");
        pageInfo = new JButton("Infos Page");
        followButton = new JButton("Suivre");
        stopFollowing = new JButton("Arreter de suivre");
        setAdmin = new JButton("Ajouter Admin");
        removeAdmin = new JButton("Supprimer Admin");
        saveGraph = new JButton("Sauvegarder");
        loadGraph = new JButton("Charger");
        graphStats = new JButton("Statistiques du graphe");
        verticesSortedByName = new JButton("Ensemble des sommets triés par nom");
        verticesSortedByOutDegree = new JButton("Ensemble des sommets triés par degré sortant");
        verticesSortedByPageRank = new JButton("Ensemble des sommets triés par Page Rank");
        edgesButton = new JButton("Ensemble des arcs");
        
        
        fileChooser = new JFileChooser();
    }
    
    // Placement des composants
    private void placeComponents() {
        JPanel p = new JPanel(new GridLayout(0, 1)); {
            JPanel r = new JPanel(new BorderLayout()); {
                JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                    s.add(new JLabel("Utilisateurs"));
                }
                r.add(s, BorderLayout.NORTH);
                r.add(new JScrollPane(userList), BorderLayout.CENTER);
            }
            
            p.add(r);
            
            r = new JPanel(new BorderLayout()); {
                JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                    s.add(new JLabel("Pages"));
                }
                
                r.add(s, BorderLayout.NORTH);
                
                r.add(new JScrollPane(pageList), BorderLayout.CENTER);
            }
            
            p.add(r);
        }
        
        mainFrame.add(p, BorderLayout.WEST);
    
        p = new JPanel(new BorderLayout()); {
            JPanel q = new JPanel(new GridLayout(0, 2)); {
                JPanel r = new JPanel(new GridLayout(0, 1)); {
                    JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                        s.add(new JLabel("Actions utilisateurs:"));
                    }
                    
                    r.add(s);
                    r.add(addUser);
                    r.add(removeUser);
                    r.add(userInfo);
                    r.add(followButton);
                    r.add(stopFollowing);
                }
                
                q.add(r);
                
                r = new JPanel(new GridLayout(0, 1)); {
                    
                    JPanel s = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                        s.add(new JLabel("Actions pages:"));
                    }
                    
                    r.add(s);
                    r.add(addPage);
                    r.add(removePage);
                    r.add(pageInfo);
                    r.add(setAdmin);
                    r.add(removeAdmin);             
                }
                
                q.add(r);
            }
            
            p.add(q, BorderLayout.NORTH);
            
            q = new JPanel(new GridLayout(0, 1)); {
                JPanel r = new JPanel(new GridLayout(0, 1)); {
                    r.add(graphStats);
                    r.add(verticesSortedByName);
                    r.add(verticesSortedByOutDegree);
                    r.add(verticesSortedByPageRank);
                    r.add(edgesButton);
                }
                
                q.add(r);
        
                r = new JPanel(new FlowLayout(FlowLayout.CENTER)); {
                    r.add(saveGraph);
                    r.add(loadGraph);
                }
                
                q.add(r);
            }
            
            p.add(q, BorderLayout.SOUTH);
        }
        
        mainFrame.add(p, BorderLayout.CENTER);
        
    }
    
    // Création de l'observateur du modèle et des écouteurs de la vue
    private void createController() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        model.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                refresh();
            }
        });
        
        addUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String firstname = JOptionPane.showInputDialog(null, "Entrez le prénom:");
                String name = JOptionPane.showInputDialog(null, "Entrez le nom:");
                String age = JOptionPane.showInputDialog(null, "Entrez l'âge:");
                
                if (firstname != null && firstname.length() > 0 
                        && name != null && name.length() > 0
                        && age != null && age.length() > 0) {
                    model.addVertice(new Utilisateur(firstname, name, Integer.parseInt(age)));
                }
            }
        });
        
        addPage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(null, "Entrez le nom de la page");
                
                if (name != null && name.length() > 0) {
                    model.addVertice(new Page(name));
                }
            }
        });
        
        removeUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Sommet s = userList.getSelectedValue();
                if (s != null) {
                    model.removeVertice(s);
                }
            }
        });
        
        removePage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Sommet s = pageList.getSelectedValue();
                if (s != null) {
                    model.removeVertice(s);
                }
            }
        });
        
        userInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Sommet s = userList.getSelectedValue();
                if (s != null) {
                    String strInfo = "ID: " + s.getId() + "\nNom: " + ((Utilisateur) s).getFullName() 
                            + "\nAge: " + ((Utilisateur) s).getAge() 
                            + "\nSuit: " + s.getNeighbors().toString() 
                            + "\nEst suivi par: " + s.getFollowers().toString()
                            + "\nPage Rank: " + s.getPageRank()
                            + "\nDegré sortant: " + s.getNeighbors().size()
                            + "\nDegré entrant: " + s.getFollowers().size();
                    
                    showInfoDialog(strInfo, "Infos de " + ((Utilisateur) s).getFullName());
                }
            }
        });
        
        pageInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Sommet s = pageList.getSelectedValue();
                if (s != null) {
                    String strInfo = "ID: " + s.getId() + "\nNom: " + s.getName()
                            + "\nEst suivi par: " + s.getFollowers().toString() 
                            + "\nAdmins: " + ((Page) s).getAdmins().toString()
                            + "\nPage Rank: " + s.getPageRank();
                    
                    showInfoDialog(strInfo, "Infos de la page " + s.getName());
                }
            }
        });
        
        followButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Utilisateur u = (Utilisateur) userList.getSelectedValue();
                
                if (u != null) {
                    Set<Sommet> verticesSet = new HashSet<Sommet>();
                    for (Sommet s : model.getVertices()) {
                        if (!u.getNeighbors().contains(s) && s != u) {
                            verticesSet.add(s);
                        }
                    }
                    
                    Object[] verticesList = verticesSet.toArray();
                    
                    if (verticesList.length > 0) {
                        Sommet s = (Sommet) JOptionPane.showInputDialog(null,
                                            "Choisissez un utilisateur ou une page à suivre.",
                                            "Suivre un utilisateur ou une page",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            verticesList,
                                            verticesList[0]);
                        if (s != null) {
                            model.addEdge(u, s);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, 
                                "Cet utilisateur suit déjà tout le monde",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        stopFollowing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Utilisateur u = (Utilisateur) userList.getSelectedValue();
                
                if (u != null) {
                    Object[] verticesList = u.getNeighbors().toArray();
                    
                    if (verticesList.length > 0) {
                        Sommet s = (Sommet) JOptionPane.showInputDialog(null,
                                            "Choisissez un utilisateur ou une page à ne plus suivre.",
                                            "Arreter de suivre un utilisateur ou une page",
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            verticesList,
                                            verticesList[0]);
                        if (s != null) {
                            model.removeEdge(u, s);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Cet utilisateur ne suit personne.",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        setAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Page s = (Page) pageList.getSelectedValue();
                if (s != null) {
                    Set<Utilisateur> userSet = new HashSet<Utilisateur>();
                    for (Sommet v : model.getVertices()) {
                        if (v instanceof Utilisateur) {
                            if (!s.getAdmins().contains(v)) {
                                userSet.add((Utilisateur) v);
                            }
                        }
                    }
                    
                    Object[] userList = userSet.toArray();
                    
                    if (userList.length > 0) {
                        Utilisateur u = (Utilisateur) JOptionPane.showInputDialog(null,
                                                "Choisissez un utilisateur à ajouter comme administrateur.",
                                                "Ajout d'administrateur",
                                                JOptionPane.QUESTION_MESSAGE,
                                                null,
                                                userList,
                                                userList[0]);
                        if (u != null) {
                            s.addAdmin(u);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, 
                                "Vous ne pouvez plus ajouter d'admin à cette page",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        removeAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Page s = (Page) pageList.getSelectedValue();
                if (s != null) {
                    Object[] listAdmins = s.getAdmins().toArray();
                    if (listAdmins.length > 0) {
                        Utilisateur u = (Utilisateur) JOptionPane.showInputDialog(null,
                                                "Choisissez un admin à enlever.",
                                                "Suppression d'administrateur",
                                                JOptionPane.QUESTION_MESSAGE,
                                                null,
                                                listAdmins,
                                                listAdmins[0]);
                        if (u != null) {
                            s.removeAdmin(u);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, 
                                "Vous ne pouvez plus supprimer d'admin de cette page",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        saveGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showSaveDialog(mainFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        model.saveGraph(fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, 
                                "Erreur lors de la sauvegarde du graphe.",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }); 
        
        loadGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnVal = fileChooser.showOpenDialog(mainFrame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        model.loadGraph(fileChooser.getSelectedFile());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null,
                                "Erreur lors de l'ouverture du fichier.",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (BadSyntaxException ex) {
                        JOptionPane.showMessageDialog(null, 
                                "Le fichier est corrompu",
                                "Erreur!",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        graphStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dialogString = "Nombre de sommets: " + model.getVerticesNb()
                        + "\nNombre d'arcs: " + model.getEdgesNb()
                        + "\nEnsemble des sommets: " + model.getVertices().toString()
                        + "\nNombre d'utilisateurs: " + model.getUsersNb()
                        + "\nNombre de pages: " + model.getPagesNb()
                        + "\nÂge moyen des utilisateurs: " + model.getAverageAge();
                
                showInfoDialog(dialogString, "Statistiques du graphe");
                        
            }
        });
        
        verticesSortedByName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {                
                showInfoDialog(model.getVerticesSortedByName().toString(), "Sommets triés par nom");
            }
        });
        
        verticesSortedByOutDegree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfoDialog(model.getVerticesSortedByOutDegree().toString(), "Sommets triés par degré sortant");
            }
        });
        
        verticesSortedByPageRank.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfoDialog(model.getVerticesSortedByPageRank().toString(), "Sommets triés par page rank");
            }
        });
        
        edgesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfoDialog(model.getEdges().toString(), "Ensemble des arcs du graphe");
            }
        });
    }
    
    private void refresh() {
        model.generatePageRank();
        
        userListModel.removeAllElements();
        pageListModel.removeAllElements();
        
        for (Sommet s : model.getVertices()) {
            if (s instanceof Utilisateur) {
                userListModel.addElement(s);
            } else {
                pageListModel.addElement(s);
            }
        }
    }
    
    private void showInfoDialog(String str, String dialogTitle) {
        JTextArea txtArea = new JTextArea(str);
        txtArea.setEditable(false);
        txtArea.setWrapStyleWord(true);
        
        JScrollPane sp = new JScrollPane(txtArea);
        sp.setPreferredSize(new Dimension(1000, 300));
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        JOptionPane.showMessageDialog(null,
                                        sp,
                                        dialogTitle,
                                        JOptionPane.INFORMATION_MESSAGE);   
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GraphApp().display();
            }
        });
    }
}
