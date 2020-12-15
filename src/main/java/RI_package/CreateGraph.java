package RI_package;

import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceEdge;

import java.io.IOException;

public class CreateGraph {
    /**
     * Construit le graphe de collaboration
     */
    public static Graph defaultGraph( ) {
        Graph graph = new DefaultGraph("Default");
        FileSource fs = new FileSourceEdge(true);
        fs.addSink(graph);
        try {
            fs.readAll("./src/main/resources/com-dblp.ungraph.txt");
        } catch ( IOException e) {
            System.out.println("No such file");
            System.exit(1);
        } finally {
            fs.removeSink(graph);
        }
        return graph;
    }

    /**
     * Construit un graphe aléatoire ayant le même nombre de nœuds et degré moyen que le graphe de collaboration
     */
    public static Graph randomGraph( ) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new RandomGenerator(6.62208890914917);
        gen.addSink(graph);
        gen.begin();
        for(int i = 0; i < 317080; i++)
            gen.nextEvents();
        gen.end();
        return graph;
    }

    /**
     * Construit un graphe de type Barabasi-Albert ayant le même nombre de nœuds et degré moyen que le graphe de collaboration
     */
    public static Graph barabasiGraph( ) {
        Graph graph = new SingleGraph("Barabasi");
        Generator gen = new BarabasiAlbertGenerator((int) 6.62208890914917);
        gen.addSink(graph);
        gen.begin();
        for(int i = 0; i < 317080; i++)
            gen.nextEvents();
        gen.end();
        return graph;
    }

}
