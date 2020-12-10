import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.IOException;

public class Mesure {
    private final Graph g;
    private String nameFile = "./src/main/files/";

    public Mesure() {
        this.g = new DefaultGraph("g") ;
        FileSource fs = new FileSourceEdge(true);
        fs.addSink(this.g);
        try {
            fs.readAll("./src/main/files/com-dblp.ungraph.txt");
        } catch( IOException e) {
            System.out.println("No such file");
            System.exit(1);
        } finally {
            fs.removeSink(this.g);
        }
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile += nameFile;
    }

    public Graph getGraph() {
        return this.g;
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

    public void degreeDistribution() {
        setNameFile("fichierDistributionDegree.dat");
        StringBuilder stringBuilder = new StringBuilder();
        int[] dd = Toolkit.degreeDistribution(getGraph());

        for (int k = 0; k < dd.length; k++)
            if (dd[k] != 0)
                stringBuilder.append(String.format("%6d%20.8f%n", k, ((double) dd[k] / getNode())));
        Utils.saveFile(getNameFile(), stringBuilder.toString());
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        /** Exercice 1 */
        System.out.println("\nExercice 1\n");
        System.out.println("Création du graphe ...");
        Mesure mesure = new Mesure();
        System.out.println("Graphe créé !");

        /** Exercice 2 */
        System.out.println("\nExercice 2\n");
        // Noeuds
        System.out.println("Nombre de noeuds: " + mesure.getNode());
        // Liens
        System.out.println("Nombre de liens: " + mesure.getEdge());
        // Degré moyen
        System.out.println("Degré moyen: " + mesure.averageDegree());
        // Coefficient de clustering du noeud
        System.out.println("Coefficient de clustering: " + mesure.coefficientClustering());
        // Coefficient de clustering du graphe
        System.out.println("Coefficient de clustering pour un réseau aléatoire: " + mesure.coefficientClusteringGraph());

        /** Exercice 3 */
        System.out.println("\nExercice 3\n");
        // Connexité
        System.out.println("Réseau connexe: " + mesure.isConnected());

        /** Exercice 4 */
        // Distribution des degrés
        mesure.degreeDistribution();
    }
}