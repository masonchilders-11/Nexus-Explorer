import java.util.*;

/**
 * Testing out the Kevin Bacon graph creation on the test samples
 *
 * @author Mason Childers, CS10, 23W
 */
public class BaconTest {
    public static void main(String[] args) throws Exception {
        BaconGraph bacon = new BaconGraph();
        AverageSeparationCalc calc = new AverageSeparationCalc();

        Map<Integer, String> actorMap = bacon.createActorMap("PS4/actorsTest.txt");
        System.out.println(actorMap);

        System.out.println("----------");

        Map<Integer, String> movieMap = bacon.createMovieMap("PS4/moviesTest.txt");
        System.out.println(movieMap);

        System.out.println("----------");

        Map<String, List<String>> movieActorMap = bacon.createMovieActorMap(actorMap, movieMap, "PS4/movie-actorsTest.txt");
        System.out.println(movieActorMap);

        System.out.println("----------");

        Graph<String, List<String>> baconGraph = bacon.createBaconGraph(actorMap, movieActorMap);
        System.out.println(baconGraph);

        System.out.println("----------");

        Graph<String, List<String>> bfs = GraphLib.bfs(baconGraph, "Kevin Bacon");
        System.out.println(bfs);

        System.out.println("----------");

        List<String> dartmouthPath = GraphLib.getPath(bfs, "Dartmouth (Earl thereof)");
        List<String> bobPath = GraphLib.getPath(bfs, "Bob");
        List<String> kevinPath = GraphLib.getPath(bfs, "Kevin Bacon");
        List<String> nobodyPath = GraphLib.getPath(bfs, "Nobody");
        System.out.println(dartmouthPath);
        System.out.println(bobPath);
        System.out.println(kevinPath);
        System.out.println(nobodyPath);

        System.out.println("----------");

        Set<String> missingVertices = GraphLib.missingVertices(baconGraph, bfs);
        System.out.println(missingVertices);

        System.out.println("----------");

        double averageSeparation = GraphLib.averageSeparation(bfs, "Kevin Bacon");
        System.out.println(averageSeparation);

        System.out.println("---------");

        Map<String, Double> averageSeparationMap = calc.createAverageSeparationMap(baconGraph, bfs, actorMap);
        System.out.println(averageSeparationMap);

        System.out.println("----------");

        Iterable<String> vertexIterable = bfs.vertices();
        List<String> vertexList = new ArrayList<>();

        for (String vertex : vertexIterable) {
            vertexList.add(vertex);
        }

        vertexList.remove("Bob");
        vertexList.add("Bob");
        System.out.println(vertexList);

        Comparator<String> compare = new AverageSeparationComparator(averageSeparationMap);
        vertexList.sort(compare);

        System.out.println(vertexList);
    }
}
