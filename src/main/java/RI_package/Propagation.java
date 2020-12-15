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

    public Graph getGraph() {
        return this.graph;
    }

    public int getNode() {
        return getGraph().getNodeCount();
    }

    public double averageDegree() {
        return Toolkit.averageDegree(getGraph());
    }

    // Calcul le taux de propagation du virus → λ = β / µ
    public double propagationVirus() {
        return this.beta / this.mu;
    }

    // Calcule <k²>
    public double moyenneCarreDegres() {
        double sumDegree = 0;
        for(Node n : getGraph())
            sumDegree += Math.pow(n.getDegree() , 2);
        return sumDegree / getNode();
    }

    // Calcule le seuil épidémique du graphe → λc = <k> / <k²>
    public double seuilEpidemique() {
        return averageDegree() / moyenneCarreDegres();
    }

    // Calcule le degré moyen d'un nœud pour le groupe 0 (scénario 2)
    public double averageDegreeGroupe0() {
        double res = 0;
        for(double x : list0)
            res += x;
        return res / list0.size();
    }

    // Calcule le degré moyen d'un nœud pour le groupe 1 (scénario 3)
    public double averageDegreeGroupe1() {
        double res = 0;
        for(double x : list1)
            res += x;
        return res / list1.size();
    }

    // Calcule le seuil épidémique du graphe pour le scénario 2 où 50% des nœuds sont immunisés
    public double seuilEpidemiqueScenario2() {
        return averageDegreeGroupe0() / moyenneCarreDegres();
    }

    // Calcule le seuil épidémique du graphe pour le scénario 3 où 50% des nœuds ont une arête adjacente aléatoire immunisée
    public double seuilEpidemiqueScenario3() {
        return averageDegreeGroupe1() / moyenneCarreDegres();
    }

    // Calcule le seuil épidémique du graphe aléatoire → 1 / (<k> + 1)
    public double seuilEpidemiqueRandom() {
        return 1 / (averageDegree() + 1);
    }

    public int getNbInfected() {
        return this.infected;
    }

    // Simule le scénario 2 → 50% des nœuds deviennent immunisés, tirage effectué aléatoirement
    public void scenario2() {
        int compteur = 0;
        // On prend 50% des nœuds
        while(compteur < getNode() / 2) {
            // On choisi un nœud aléatoirement
            int random = (int) (Math.random() * getNode());
            Node node = getGraph().getNode(random);
            // On rend ce nœud immunisé au virus
            if(node.getAttribute("Immune").equals("False")) {
                node.setAttribute("Immune" , "True");
                // Pour le calcul du degré moyen du groupe 0
                this.list0.add(node.getDegree());
                compteur++;
            }
        }
    }

    // Simule le scénario 3 → 50% des nœuds ont une de leur arête adjacente immunisée
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
                // On rend ce nœud immunisé au virus
                if(node.getAttribute("Immune").equals("False")) {
                    u.setAttribute("Immune" , "True");
                    // Pour le calcul du degré moyen du groupe 1
                    this.list1.add(u.getDegree());
                    compteur++;
                }
            }
        }
    }

    // Simule la propagation du virus dans le graphe
    public void propagation(int dayMax, String nameFile, String nameFile2) {
        // Noeud source infecté et non immunisé
        Node infecte0 = getGraph().getNode(0);
        infecte0.setAttribute("Immune", "False");
        infecte0.setAttribute("Infected");
        this.infected++;

        // Fichier avec les valeurs brutes
        StringBuilder stringBuilder = new StringBuilder();
        // Fichier avec les valeurs en pourcentage → les graphiques seront plus parlant
        StringBuilder stringBuilderPercent = new StringBuilder();

        // Selon le nombre de jours, on va propager le virus à travers le graphe
        for(int i = 1; i < dayMax; i++) {
            infection();
            stringBuilder.append(String.format("%d%s%d%s", i, " ", getNbInfected(), "\n"));
            stringBuilderPercent.append(String.format("%d%s%f%s", i, " ", (double) getNbInfected() / getGraph().getNodeCount(), "\n"));
        }
        setNameFile(nameFile);
        Utils.saveFile(getNameFile(), stringBuilder.toString());

        setNameFile(nameFile2);
        Utils.saveFile(getNameFile(), stringBuilderPercent.toString());
    }

    // Simule l'envoie d'un mail contenant un virus
    public void infection() {
        // On parcours l'ensemble des nœuds du graphe
        for(Node v : getGraph()) {
            // Pour chaque nœud v infecté
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
                    // Anti virus actif → le nœud est soigné
                    v.removeAttribute("Infected");
                    v.setAttribute("Clear");
                    this.infected--;
                }
            }
        }
    }

    public static void main(String[] args) {
        Propagation propagationDefault_1 = new Propagation(0, 1);

        System.out.println(propagationDefault_1.seuilEpidemique());
        /*Propagation propagationDefault_2 = new Propagation(0, 2);
        Propagation propagationDefault_3 = new Propagation(0, 3);

        Propagation propagationRandom_1 = new Propagation(1, 1);
        Propagation propagationRandom_2 = new Propagation(1, 2);
        Propagation propagationRandom_3 = new Propagation(1, 3);

        Propagation propagationBarabasi_1 = new Propagation(2, 1);
        Propagation propagationBarabasi_2 = new Propagation(2, 2);
        Propagation propagationBarabasi_3 = new Propagation(2, 3);

        System.out.println();

        // Taux de propagation du virus → Lambda = beta / mu
        System.out.println("Taux de propagation du virus: " + propagationDefault_1.propagationVirus());
        // Seuil épidémique → <k> / <k²>
        System.out.println("Seuil épidémique du réseau: " + propagationDefault_1.seuilEpidemique());
        // Seuil épidémique d'un réseau aléatoire → 1 / <k> + 1
        System.out.println("Seul épidémique d'un réseau aléatoire: " + propagationRandom_1.seuilEpidemiqueRandom());

        System.out.println();

        // Graphe de collaboration - Scénario 1
        propagationDefault_1.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario1.dat",
                "./src/main/files/fichierPropagationDefault_Scenario1_Percent.dat");
        // Graphe de collaboration - Scénario 2
        propagationDefault_2.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario2.dat",
                "./src/main/files/fichierPropagationDefault_Scenario2_Percent.dat");
        // Graphe de collaboration - Scénario 3
        propagationDefault_3.propagation(84, "./src/main/files/fichierPropagationDefault_Scenario3.dat",
                "./src/main/files/fichierPropagationDefault_Scenario3_Percent.dat");

        // Degré moyen des groupes 0 et 1
        System.out.println("Groupe 0 - Degré moyen: " + propagationDefault_2.averageDegreeGroupe0());
        System.out.println("Groupe 1 - Degré moyen: " + propagationDefault_3.averageDegreeGroupe1());
        System.out.println();
        // Seuil épidémique des scénarios 2 et 3
        System.out.println("Scénario 2 - Seuil épidémique: " + propagationDefault_2.seuilEpidemiqueScenario2());
        System.out.println("Scénario 3 - Seuil épidémique: " + propagationDefault_3.seuilEpidemiqueScenario3());

        System.out.println();

        // Graphe aléatoire - Scénario 1
        propagationRandom_1.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario1.dat",
                "./src/main/files/fichierPropagationRandom_Scenario1_Percent.dat");
        // Graphe aléatoire - Scénario 2
        propagationRandom_2.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario2.dat",
                "./src/main/files/fichierPropagationRandom_Scenario2_Percent.dat");
        // Graphe aléatoire - Scénario 3
        propagationRandom_3.propagation(84, "./src/main/files/fichierPropagationRandom_Scenario3.dat",
                "./src/main/files/fichierPropagationRandom_Scenario3_Percent.dat");

        System.out.println();

        // Graphe Barabasi - Scénario 1
        propagationBarabasi_1.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario1.dat",
                "./src/main/files/fichierPropagationBarabasi_Scenario1_Percent.dat");
        // Graphe aléatoire - Scénario 2
        propagationBarabasi_2.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario2.dat",
                "./src/main/files/fichierPropagationBarabasi_Scenario2_Percent.dat");
        // Graphe Barabasi - Scénario 3
        propagationBarabasi_3.propagation(84, "./src/main/files/fichierPropagationBarabasi_Scenario3.dat",
                "./src/main/files/fichierPropagationBarabasi_Scenario3_Percent.dat");
         */
    }
}
