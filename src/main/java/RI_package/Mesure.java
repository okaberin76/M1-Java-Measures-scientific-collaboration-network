package RI_package;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.BreadthFirstIterator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.List;

public class Mesure {
    private Graph graph;
    private String nameFile;

    public Mesure(int choice) {
        switch (choice) {
            case 1 -> this.graph = CreateGraph.defaultGraph();
            case 2 -> this.graph = CreateGraph.randomGraph();
            case 3 -> this.graph = CreateGraph.barabasiGraph();
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

    public int getEdge() {
        return getGraph().getEdgeCount();
    }

    public double averageDegree() {
        return Toolkit.averageDegree(getGraph());
    }

    public double coefficientClustering() {
        return Toolkit.averageClusteringCoefficient(getGraph());
    }

    public double coefficientClusteringGraph() {
        return averageDegree() / getNode();
    }

    public boolean isConnected() {
        return Toolkit.isConnected(getGraph());
    }

    public void degreeDistribution(String nameFile) {
        setNameFile(nameFile);
        StringBuilder stringBuilder = new StringBuilder();
        int[] p = Toolkit.degreeDistribution(getGraph());

        for(int k = 0; k < p.length; k++)
            if (p[k] != 0)
                stringBuilder.append(String.format("%6d%20.8f%n", k, ((double) p[k] / getNode())));
        Utils.saveFile(getNameFile(), stringBuilder.toString());
    }

    public double[] distanceDistribution(int n) {
        List<Node> randomSet = Toolkit.randomNodeSet(getGraph(), n);
        double[] tab = new double[50];
        double sum = 0;

        for(int i = 0; i < n; i++) {
            BreadthFirstIterator k = (BreadthFirstIterator) randomSet.get(i).getBreadthFirstIterator();
            while (k.hasNext()) {
                Node next = k.next();
                tab[k.getDepthOf(next)]++;
                sum += k.getDepthOf(next);
            }
        }
        System.out.println("La distance moyenne est: " + sum / (n * (getNode())));
        return tab;
    }

    public void distribution(int n, String nameFile) {
        double[] tab = distanceDistribution(n);
        setNameFile(nameFile);
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < tab.length; i++)
            stringBuilder.append(String.format("%d%s%f%s", i, " ", tab[i] / (getNode() * 1000), "\n"));
        Utils.saveFile(getNameFile(), stringBuilder.toString());
    }

    public static void main(String[] args) {
        /*
        System.setProperty("org.graphstream.ui", "swing");

        // Exercice 1
        // Graphe par défaut, c'est à dire celui du fichier com-dblp.ungraph.txt
        RI_package.Mesure mesure = new RI_package.Mesure(0);

        // Exercice 2
        System.out.println("\nExercice 2\n");
        // nœuds
        System.out.println("Nombre de nœuds: " + mesure.getNode());
        // Liens
        System.out.println("Nombre de liens: " + mesure.getEdge());
        // Degré moyen
        System.out.println("Degré moyen: " + mesure.averageDegree());
        // Coefficient de clustering du noeud
        System.out.println("Coefficient de clustering: " + mesure.coefficientClustering());
        // Coefficient de clustering du graphe
        System.out.println("Coefficient de clustering pour un réseau aléatoire: " + mesure.coefficientClusteringGraph());

        // Exercice 3
        System.out.println("\nExercice 3\n");
        // Connexité
        System.out.println("Réseau connexe: " + mesure.isConnected());

        // Exercice 4
        // Distribution des degrés
        mesure.degreeDistribution("./src/main/files/fichierDistributionDegree.dat");

        // Exercice 5
        mesure.distribution(1000, "./src/main/files/fichierDistributionDistance.dat");
         */

        // Exercice 6 -> environ 10 min à run
        System.out.println("Exercice 6\n");
        // Graphe par défaut, c'est à dire celui du fichier com-dblp.ungraph.txt
        Mesure mesure = new Mesure(1);
        // Graphe aléatoire
        Mesure mesure2 = new Mesure(2);
        // Graphe Barabasi-Albert
        Mesure mesure3 = new Mesure(3);
        // nœuds
        System.out.println("Nombre de nœuds:\n" + "Default -> " + mesure.getNode() + "\nRandom -> " + mesure2.getNode() + "\nBarabasi -> " + mesure3.getNode());
        // Liens
        System.out.println("\nNombre de liens:\n" + "Default -> " + mesure.getEdge() + "\nRandom -> " + mesure2.getEdge() + "\nBarabasi -> " + mesure3.getEdge());
        // Degré moyen
        System.out.println("\nDegré moyen:\n" + "Default -> " + mesure.averageDegree() + "\nRandom -> " + mesure2.averageDegree() + "\nBarabasi -> " + mesure3.averageDegree());
        // Coefficient de clustering du noeud
        System.out.println("\nCoefficient de clustering (noeud):\n" + "Default -> " + mesure.coefficientClustering() + "\nRandom -> " + mesure2.coefficientClustering() + "\nBarabasi -> " + mesure3.coefficientClustering());
        // Coefficient de clustering du graphe
        System.out.println("\nCoefficient de clustering (graphe):\n" + "Default -> " + mesure.coefficientClusteringGraph() + "\nRandom -> " + mesure2.coefficientClusteringGraph() + "\nBarabasi -> " + mesure3.coefficientClusteringGraph());
        // Connexité
        System.out.println("\nRéseau connexe:\n" + "Default -> " + mesure.isConnected() + "\nRandom -> " + mesure2.isConnected() + "\nBarabasi -> " + mesure3.isConnected());
        // Distribution des degrés
        mesure.degreeDistribution("./src/main/files/fichierDistributionDegree_Default.dat");
        mesure2.degreeDistribution("./src/main/files/fichierDistributionDegree_Random.dat");
        mesure3.degreeDistribution("./src/main/files/fichierDistributionDegree_Barabasi.dat");
        // Distribution des distances
        mesure.distribution(1000, "./src/main/files/fichierDistributionDistance_Default.dat");
        mesure2.distribution(1000, "./src/main/files/fichierDistributionDistance_Random.dat");
        mesure3.distribution(1000, "./src/main/files/fichierDistributionDistance_Barabasi.dat");
    }
}
