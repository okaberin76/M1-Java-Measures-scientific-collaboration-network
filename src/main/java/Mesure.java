import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.IOException;
import java.util.Arrays;

public class Mesure {

    public static void main(String ... args) throws IOException {
        System.setProperty("org.graphstream.ui", "swing");
        System.out.println("Début du programme");
        /** Exercice 1 */
        System.out.println("\nExercice 1\n");
        System.out.println("Création du graphe ...");
        Graph g = new DefaultGraph("g") ;
        FileSource fs = new FileSourceEdge(true);
        fs.addSink(g);
        try {
            fs.readAll("./src/main/files/com-dblp.ungraph.txt");
        } catch( IOException e) {
            System.out.println("no such file");
        } finally {
            fs.removeSink(g);
        }
        System.out.println("Graphe créé !");

        /** Exercice 2 */
        System.out.println("\nExercice 2\n");
        System.out.println("Nombre de noeuds: " + g.getNodeCount());
        System.out.println("Nombre de liens: " + g.getEdgeCount());
        System.out.println("Degré moyen: " + Toolkit.averageDegree(g));
        // Coefficient de clustering du noeud
        System.out.println("Coefficient de clustering: " + Toolkit.averageClusteringCoefficient(g));
        // Coefficient de clustering du graphe
        System.out.println("Coefficient de clustering pour un réseau aléatoire: " + Toolkit.averageDegree(g) / g.getNodeCount());

        /** Exercice 3 */
        System.out.println("\nExercice 3\n");
        System.out.println("Réseau connexe: " + Toolkit.isConnected(g));
        System.out.println("Probabilité qu'un noeud au hasard est un degré k ?: " + Arrays.toString(Toolkit.degreeDistribution(g)));

        //graph.display();
    }
}