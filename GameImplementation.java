import java.util.*;

/**
 * Implementation of the Kevin Bacon game taking user input
 *
 * @author Mason Childers, CS10, 23W
 */
public class GameImplementation {

    public static void main(String[] args) throws Exception {
        // initializing the BaconGraph object to create the graph of actors
        BaconGraph bacon = new BaconGraph();

        // initializing scanner to read user input
        Scanner input = new Scanner(System.in);
        String line;

        // initializing the quit condition to false
        boolean quit = false;

        // creating the graph of actors
        Map<Integer, String> actorMap = bacon.createActorMap("PS4/actors.txt");
        Map<Integer, String> movieMap = bacon.createMovieMap("PS4/movies.txt");
        Map<String, List<String>> movieActorMap = bacon.createMovieActorMap(actorMap, movieMap, "PS4/movie-actors.txt");
        Graph<String, List<String>> baconGraph = bacon.createBaconGraph(actorMap, movieActorMap);

        // initializing the center of the universe as Kevin Bacon
        String centerOfUniverse = "Kevin Bacon";
        Graph<String, List<String>> centerBFS = GraphLib.bfs(baconGraph, centerOfUniverse);

        // storing a list of the top 25 actors sorted in decreasing order by inDegree for future use
        List<String> inDegreeList = GraphLib.verticesByInDegree(baconGraph);
        List<String> inDegreeTop25 = new ArrayList<>();

        for (int i = 0; i < 25; i++){
            inDegreeTop25.add(inDegreeList.get(i));
        }

        // storing a list of the top 25 actors sorted in increasing order by average separation for future use
        AverageSeparationCalc calc = new AverageSeparationCalc();
        Map<String, Double> averageSeparationMap = calc.createAverageSeparationMap(baconGraph, centerBFS, actorMap);

        Iterable<String> vertexIterable = centerBFS.vertices(); // note: only using vertices connected to Bacon as described in the pset description
        List<String> vertexList = new ArrayList<>();

        for (String vertex : vertexIterable) {
            vertexList.add(vertex);
        }

        Comparator<String> compare = new AverageSeparationComparator(averageSeparationMap);
        vertexList.sort(compare);

        List<String> averageSeparationTop25 = new ArrayList<>();
        for (int i = 0; i < 25; i++){
            averageSeparationTop25.add(vertexList.get(i));
        }

        // printing out the possible commands for the game
        System.out.println("Commands:\n" +
                "u-<name>: make <name> center of the universe\n" +
                "p-<name>: find shortest path from actor to center\n" +
                "n: number of actors connected to center of universe\n" +
                "s: find average separation for center of universe\n" +
                "d: give top 25 actors sorted by in degree (max to min) in the original map\n" +
                "a: give top 25 actors sorted by average separation (min to max)\n" +
                "q: quit the game\n");

        // while the user input is not q (to quit the game)...
        while (!quit){

            // print out whose game it is
            System.out.println(centerOfUniverse + " game >");

            // create string array using the input
            line = input.nextLine();
            String[] items = line.split("-");

            // if you're creating a new center of the universe...
            if (Objects.equals(items[0], "u") && items.length > 1){

                // the actor is whatever is after the "-"
                String centerActor = items[1];

                // as long as the bacon graph has the actor...
                if (baconGraph.hasVertex(centerActor)){

                    // make the center of the universe the actor given
                    centerOfUniverse = centerActor;
                    centerBFS = GraphLib.bfs(baconGraph, centerOfUniverse);

                    System.out.println(centerOfUniverse + " is now the center of the universe\n");

                }
                // otherwise, print out that there is no actor with the given name
                else {
                    System.out.println("There is no actor with the given name\n");
                }
            }

            // if you're finding a path to the center...
            else if (Objects.equals(items[0], "p") && items.length > 1){


                // the actor is whatever is after the "-"
                String actor = items[1];

                // as long as the bacon graph has the actor...
                if (baconGraph.hasVertex(actor)){

                    // create the path using bfs and get the bacon number
                    List<String> path = GraphLib.getPath(centerBFS, actor);
                    int baconNumber = path.size() - 1;
                    System.out.println(actor + "'s number is: " + baconNumber);

                    // print out the relationships between actors using their labels
                    for (int i = 0; i < baconNumber; i++){
                        String actor1 = path.get(i);
                        String actor2 = path.get(i + 1);

                        System.out.println(actor1 + " appeared in: " + baconGraph.getLabel(actor1, actor2) + " with " + actor2 + "\n");
                    }
                }
                // otherwise, print out that there is no such actor in the graph
                else {
                    System.out.println("There is no actor with the given name\n");
                }
            }

            // if you're finding the number of connected actors...
            else if (Objects.equals(items[0], "n") && items.length < 2){

                // find out the number of connected actors and total actors
                int connectedActors = centerBFS.numVertices() - 1;
                int totalActors = baconGraph.numVertices() - 1;

                System.out.println(centerOfUniverse + " is connected to " + connectedActors + " out of " + totalActors + " possible actors\n");
            }

            // if you're trying to find the average separation for the center...
            else if (Objects.equals(items[0], "s") && items.length < 2){

                // calculate the average separation
                double averageSeparation = GraphLib.averageSeparation(centerBFS, centerOfUniverse);

                System.out.println(centerOfUniverse + "'s average separation is: " + averageSeparation + "\n");
            }

            // if you're wanting the top 25 best actors to be Bacons, based off of inDegree...
            else if (Objects.equals(items[0], "d") && items.length < 2){
                System.out.println("The best actors to be Bacons, sorted by in degree (max to min), are: " + inDegreeTop25 + "\n");
            }

            // if you're wanting the top 25 best actors to be Bacons, based off of average separation...
            else if (Objects.equals(items[0], "a") && items.length < 2){
                System.out.println("The best actors to be Bacons, sorted by average separation (min to max), are: " + averageSeparationTop25 + "\n");
            }

            // if you want to quit the game...
            else if (Objects.equals(items[0], "q") && items.length < 2){

                // set the boolean quit to true
                quit = true;

                System.out.println("Game quit");
            }

            // otherwise the input was invalid
            else System.out.println("Not a valid input\n");
        }

    }
}
