package RI_package;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.util.ArrayList;
import java.util.List;

public class Propagation {
    private final Mesure graph;
    private final List<Node> listInfected;
    private int infected, healed, susceptible;
    private int day;
    private int nbNewInfected = 0;
    private final double beta = (double) 1 / 7;
    private final double mu = (double) 1 / 14;

    public Propagation(int choice) {
        this.graph = new Mesure(choice);

        for (Node n: getGraph()) {
            n.setAttribute("infection", "susceptible");
            // n.setAttribute("anti-virus", "up to date");
        }

        this.listInfected = new ArrayList<>();
        this.healed = 0;
        this.infected = 0;
        this.susceptible = this.graph.getNode();
        this.day = 0;
    }

    public double propagationVirus() {
        return this.beta / this.mu;
    }

    public int moyenneCarreDegres() {
        int sumDegree = 0;
        for(Node n : getGraph())
            sumDegree += Math.pow(n.getDegree() , 2);
        return sumDegree / this.graph.getNode();
    }

    public double seuilEpidemique() {
        return this.graph.averageDegree() / moyenneCarreDegres();
    }

    public double seuilEpidemiqueRandom() {
        return 1 / (this.graph.averageDegree() + 1);
    }

    public Graph getGraph() {
        return this.graph.getGraph();
    }

    public void affiche() {
        if(this.day == 1 || this.day % 7 == 0)
            System.out.println("Day: " + this.day + " | Infected: " + this.infected + "(+" + this.nbNewInfected + ") | Susceptible: " + susceptible + " | Healed: " + this.healed);
    }

    public void affichePercentage() {
        System.out.println("Pourcentage d'infectés: " + (double) listInfected.size() / getGraph().getNodeCount() * 100 + "%");
    }

    public int getNbInfected() {
        return this.infected;
    }

    public void day0() {
        Node infecte0 = getGraph().getNode("0");
        infecte0.setAttribute("infection","infected");
        this.listInfected.add(infecte0);
        this.infected++;
        this.susceptible--;
        this.day++;
    }

    public void infection(int dayMax) {
        day0();
        while(this.day <= dayMax) {
            if(this.day % 7 == 0) {
                List<Node> listNewInfected = new ArrayList<>();
                nbNewInfected = this.listInfected.size();
                // Pour chaque noeud infectés v
                for(Node v : this.listInfected) {
                    // Pour chaque voisin u de v
                    for(Edge u : v) {
                        // On récupère u
                        Node node = u.getOpposite(v);
                        // Nombre aléatoire entre 0 et 1
                        if(Math.random() < this.beta) {
                            // v envoie un mail à u aujourd'hui
                            if (node.getAttribute("infection").equals("susceptible")) {
                                // On update u -> infecté
                                node.setAttribute("infection","infected");
                                this.infected++;
                                this.susceptible--;
                                listNewInfected.add(node);
                            }
                        }
                    }
                }
                this.listInfected.addAll(listNewInfected);
                nbNewInfected = this.listInfected.size() - nbNewInfected;
            }
            affiche();
            this.day++;
        }
        affichePercentage();
    }

    public static void main(String[] args) {
        Propagation propagationDefault = new Propagation(0);
        Propagation propagationRandom = new Propagation(1);

        /* Question 1 */
        // Taux de propagation du virus -> Lambda = beta / mu
        System.out.println("Taux de propagation du virus: " + propagationDefault.propagationVirus());
        // Seuil épidémique -> <k> / <k²>
        System.out.println("Seul épidémique du réseau: " + propagationDefault.seuilEpidemique());
        // Seuil épidémique d'un réseau aléatoire -> 1 / <k> + 1
        System.out.println("Seul épidémique d'un réseau aléatoire: " + propagationRandom.seuilEpidemiqueRandom());

        /* Question 2 */
        propagationDefault.infection(91);
        System.out.println("Infectés total: " + propagationDefault.getNbInfected());
    }
}
