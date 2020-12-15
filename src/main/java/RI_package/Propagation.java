package RI_package;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.util.ArrayList;
import java.util.List;

public class Propagation {
    private Graph graph;
    private int infected;
    private String nameFile;
    private final List <Integer> list0;
    private final List <Integer> list1;
    private final double beta = (double) 1 / 7;
    private final double mu = (double) 1 / 14;

    public Propagation(int choice, int scenario) {
        switch (choice) {
            case 0 -> this.graph = CreateGraph.defaultGraph();
            case 1 -> this.graph = CreateGraph.randomGraph();
            case 2 -> this.graph = CreateGraph.barabasiGraph();
            default -> System.exit(1);
        }
        this.infected = 0;
        this.list0 = new ArrayList<>();
        this.list1 = new ArrayList<>();

        for(Node n : getGraph())
            n.setAttribute("Immune", "False");

        switch(scenario) {
            case 1 -> { }
            case 2 -> scenario2();
            case 3 -> scenario3();
            default -> System.exit(1);
        }
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
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

    public double seuilEpidemiqueScenario2() {
        return averageDegreeGroupe0() / moyenneCarreDegres();
    }

    public double seuilEpidemiqueScenario3() {
        return averageDegreeGroupe1() / moyenneCarreDegres();
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

    public double averageDegreeGroupe0() {
        double res = 0;
        for(double x : list0)
            res += x;
        return res / list0.size();
    }

    public double averageDegreeGroupe1() {
        double res = 0;
        for(double x : list1)
            res += x;
        return res / list1.size();
    }

    // 50% des nœuds deviennent immunisés, tirage effectué aléatoirement
    public void scenario2() {
        int compteur = 0;
        // On prend 50% des nœuds
        while(compteur < getNode() / 2) {
            // On choisi un nœud aléatoirement
            int random = (int) (Math.random() * getNode());
            Node node = getGraph().getNode(random);
            // On rend ce nœud immunisé
            node.setAttribute("Immune" , "True");
            // Pour le calcul du degré moyen du groupe 0
            this.list0.add(node.getDegree());
            compteur++;
        }
    }

    // 50% des nœuds ont une de leurs arêtes immunisée
    public void scenario3() {
        int compteur = 0;
        // On prend 50% des nœuds
        while(compteur < getNode() / 2) {
            // On choisi un nœud aléatoirement
            int random = (int) (Math.random() * getNode());
            Node node = getGraph().getNode(random);
            // On choisi une arête aléatoire du nœud trouvé
            Edge edge = Toolkit.randomEdge(node);
            if (edge != null) {
                // On récupère le nœud au bout de l'arête
                Node u = edge.getOpposite(node);
                // On rend ce nœud immunisé
                u.setAttribute("Immune" , "True");
                // Pour le calcul du degré moyen du groupe 1
                this.list1.add(u.getDegree());
                compteur++;
            }
        }
    }

    public void propagation(int dayMax, String nameFile) {
        setNameFile(nameFile);

        // Noeud source infecté et non immunisé
        Node infecte0 = getGraph().getNode(0);
        infecte0.setAttribute("Infected");
        infecte0.setAttribute("Immune", "False");
        this.infected++;

        StringBuilder stringBuilder = new StringBuilder();
        // Selon le nombre de jours, on va propager le virus à travers le graphe
        for(int i = 1; i < dayMax; i++) {
            infection();
            stringBuilder.append(String.format("%d%s%d%s", i, " ", getNbInfected(), "\n"));
        }
        Utils.saveFile(getNameFile(), stringBuilder.toString());
        affichePercentage();
    }

    public void infection() {
        // Pour chaque nœud infectés v
        for(Node v : getGraph()) {
            if(v.hasAttribute("Infected")) {
                // Pour chaque voisin u de v
                for(Edge u : v) {
                    // On récupère le nœud u
                    Node node = u.getOpposite(v);
                    // Si ce nœud n'est pas déjà infecté ni immunisé
                    if (!node.hasAttribute("Infected") && node.getAttribute("Immune").equals("False")) {
                        // Nombre aléatoire entre 0 et 1
                        if(Math.random() < this.beta) {
                            // v envoie un mail à u aujourd'hui et l'infecte
                            node.setAttribute("Infected");
                            this.infected++;
                        }
                    }
                }
                // Nombre aléatoire entre 0 et 1
                if(Math.random() < this.mu) {
                    // Anti virus actif -> le nœud est soigné
                    v.removeAttribute("Infected");
                    v.setAttribute("Clear");
                    this.infected--;
                }
            }
        }
    }

    public static void main(String[] args) {
        long start; long fin;

        System.out.println("Creation des 3 scénarios avec le graphe de collaboration...");
        start = System.currentTimeMillis();
        Propagation propagationDefault_1 = new Propagation(0, 1);
        Propagation propagationDefault_2 = new Propagation(0, 2);
        Propagation propagationDefault_3 = new Propagation(0, 3);
        fin = System.currentTimeMillis();
        System.out.println("Creation terminée ! (" + (fin - start) + "ms)\n");

        System.out.println("Creation des 3 scénarios avec le graphe aléatoire...");
        start = System.currentTimeMillis();
        Propagation propagationRandom_1 = new Propagation(1, 1);
        Propagation propagationRandom_2 = new Propagation(1, 2);
        Propagation propagationRandom_3 = new Propagation(1, 3);
        fin = System.currentTimeMillis();
        System.out.println("Creation terminée ! (" + (fin - start) + "ms)\n");

        System.out.println("Creation des 3 scénarios avec le graphe Barabasi-Albert...");
        start = System.currentTimeMillis();
        Propagation propagationBarabasi_1 = new Propagation(2, 1);
        Propagation propagationBarabasi_2 = new Propagation(2, 2);
        Propagation propagationBarabasi_3 = new Propagation(2, 3);
        fin = System.currentTimeMillis();
        System.out.println("Creation terminée ! (" + (fin - start) + "ms)\n");

        System.out.println();

        // Taux de propagation du virus -> Lambda = beta / mu
        System.out.println("Taux de propagation du virus: " + propagationDefault_1.propagationVirus());
        // Seuil épidémique -> <k> / <k²>
        System.out.println("Seuil épidémique du réseau: " + propagationDefault_1.seuilEpidemique());
        // Seuil épidémique d'un réseau aléatoire -> 1 / <k> + 1
        System.out.println("Seul épidémique d'un réseau aléatoire: " + propagationRandom_1.seuilEpidemiqueRandom());

        System.out.println();

        // Graphe de collaboration - Scénario 1
        propagationDefault_1.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario1.dat");
        System.out.println("Infectés total: " + propagationDefault_1.getNbInfected() +"\n");
        // Graphe de collaboration - Scénario 2
        propagationDefault_2.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario2.dat");
        System.out.println("Infectés total: " + propagationDefault_2.getNbInfected() +"\n");
        // Graphe de collaboration - Scénario 3
        propagationDefault_3.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario3.dat");
        System.out.println("Infectés total: " + propagationDefault_3.getNbInfected() +"\n");

        // Degré moyen des groupes 0 et 1
        System.out.println("Groupe 0 - Degré moyen: " + propagationDefault_2.averageDegreeGroupe0());
        System.out.println("Groupe 1 - Degré moyen: " + propagationDefault_3.averageDegreeGroupe1());
        System.out.println();
        // Seuil épidémique des scénarios 2 et 3
        System.out.println("Scénario 2 - Seuil épidémique: " + propagationDefault_2.seuilEpidemiqueScenario2());
        System.out.println("Scénario 3 - Seuil épidémique: " + propagationDefault_3.seuilEpidemiqueScenario3());

        System.out.println();

        // Graphe aléatoire - Scénario 1
        propagationRandom_1.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario1.dat");
        System.out.println("Infectés total: " + propagationRandom_1.getNbInfected() +"\n");
        // Graphe aléatoire - Scénario 2
        propagationRandom_2.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario2.dat");
        System.out.println("Infectés total: " + propagationRandom_2.getNbInfected() +"\n");
        // Graphe aléatoire - Scénario 3
        propagationRandom_3.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario3.dat");
        System.out.println("Infectés total: " + propagationRandom_3.getNbInfected() +"\n");

        System.out.println();

        // Graphe Barabasi - Scénario 1
        propagationBarabasi_1.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario1.dat");
        System.out.println("Infectés total: " + propagationBarabasi_1.getNbInfected() +"\n");
        // Graphe aléatoire - Scénario 2
        propagationBarabasi_2.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario2.dat");
        System.out.println("Infectés total: " + propagationBarabasi_2.getNbInfected() +"\n");
        // Graphe Barabasi - Scénario 3
        propagationBarabasi_3.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario3.dat");
        System.out.println("Infectés total: " + propagationBarabasi_3.getNbInfected() +"\n");
    }
}
