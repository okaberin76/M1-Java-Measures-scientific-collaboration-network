package RI_package;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Propagation {
    private Graph graph;
    private int infected, healed;
    int[] tabInfected;
    private final double beta = (double) 1 / 7;
    private final double mu = (double) 1 / 14;

    public Propagation(int choice) {
        switch (choice) {
            case 0 -> this.graph = CreateGraph.defaultGraph();
            case 1 -> this.graph = CreateGraph.randomGraph();
            case 2 -> this.graph = CreateGraph.barabasiGraph();
            default -> System.exit(1);
        }
        this.healed = 0;
        this.infected = 0;
    }

    public double averageDegree() {
        return Toolkit.averageDegree(getGraph());
    }

    public Graph getGraph() {
        return this.graph;
    }

    public int getNode() {
        return getGraph().getNodeCount();
    }

    public double propagationVirus() {
        return this.beta / this.mu;
    }

    public int moyenneCarreDegres() {
        int sumDegree = 0;
        for(Node n : getGraph())
            sumDegree += Math.pow(n.getDegree() , 2);
        return sumDegree / getNode();
    }

    public double seuilEpidemique() {
        return averageDegree() / moyenneCarreDegres();
    }

    public double seuilEpidemiqueRandom() {
        return 1 / (averageDegree() + 1);
    }

    public void affichePercentage() {
        System.out.println("Pourcentage d'infectés: " + (double) getNbInfected() / getGraph().getNodeCount() * 100 + "%");
    }

    public int getNbInfected() {
        return this.infected;
    }

    public int getNbHealed() {
        return this.healed;
    }

    public void propagation(int dayMax) {
        this.tabInfected = new int[dayMax];
        // Noeud source infecté
        Node infecte0 = getGraph().getNode(0);
        infecte0.setAttribute("infected");
        this.infected++;

        this.tabInfected[0] = getNbInfected();
        System.out.println("Day: " + 0 + " | Infected: " + this.tabInfected[0]);

        for(int i = 1; i < dayMax; i++) {
            infection();
            this.tabInfected[i] = getNbInfected();
            //System.out.println("Day: " + i + " | Infected: " + getNbInfected() + " | Healed: " + getNbHealed());
        }
        affichePercentage();
    }

    public void infection() {
        // Pour chaque noeud infectés v
        for(Node v : getGraph()) {
            if(v.hasAttribute("infected")) {
                // Pour chaque voisin u de v qui n'est pas infecté
                for(Edge u : v) {
                    if (!u.getOpposite(v).hasAttribute("infected")) {
                        // Nombre aléatoire entre 0 et 1
                        if(Math.random() < this.beta) {
                            // v envoie un mail à u aujourd'hui
                            u.getOpposite(v).setAttribute("infected");
                            this.infected++;
                        }
                    }
                }
                if(Math.random() < this.mu) {
                    v.removeAttribute("infected");
                    v.setAttribute("healed");
                    this.infected--;
                    this.healed++;
                }
            }
        }
    }

    public static void main(String[] args) {
        Propagation propagationDefault = new Propagation(0);
        Propagation propagationRandom = new Propagation(1);
        Propagation propagationBarabasi = new Propagation(2);

        /* Question 1 */
        System.out.println("\nQuestion 1\n");
        // Taux de propagation du virus -> Lambda = beta / mu
        System.out.println("Taux de propagation du virus: " + propagationDefault.propagationVirus());
        // Seuil épidémique -> <k> / <k²>
        System.out.println("Seul épidémique du réseau: " + propagationDefault.seuilEpidemique());
        // Seuil épidémique d'un réseau aléatoire -> 1 / <k> + 1
        System.out.println("Seul épidémique d'un réseau aléatoire: " + propagationRandom.seuilEpidemiqueRandom());

        /* Question 2 */
        System.out.println("\nQuestion 2\n");
        propagationDefault.propagation(84);
        System.out.println("Infectés total: " + propagationDefault.getNbInfected() +"\n");

        propagationRandom.propagation(84);
        System.out.println("Infectés total: " + propagationRandom.getNbInfected() +"\n");

        propagationBarabasi.propagation(84);
        System.out.println("Infectés total: " + propagationBarabasi.getNbInfected() +"\n");
    }
}
