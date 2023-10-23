import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaconGraph {

    /**
     * Given a file of IDs to actors, create a map linking the two
     *
     * @param pathname - name of the file to read
     * @return - a map of IDs to actor names
     * @throws Exception
     */
    public Map<Integer, String> createActorMap(String pathname) throws Exception {
        // initializing map of IDs to actors
        Map<Integer, String> actorMap = new HashMap<>();
        BufferedReader input;

        // attempt to open the file
        try {
            input = new BufferedReader(new FileReader(pathname));
        } catch (FileNotFoundException e){
            System.err.println("Cannot open file");
            return actorMap;
        }

        try {

            // read through text file and add each ID mapped to the actor to the map
            String line;
            while ((line = input.readLine()) != null){
                String[] items = line.split("\\|");
                actorMap.put(Integer.parseInt(items[0]), items[1]);
            }

        } catch (Exception e){
            System.out.println(e);
        } finally {

            // close the buffered reader
            try {
                input.close();
            } catch(Exception e) {
                System.out.println(e);
            }
        }

        return actorMap;
    }

    /**
     * Given a file of IDs to movies, create a map linking the two (pretty much the same code as createActorMap)
     *
     * @param pathname - file to read
     * @return - a map of IDs to movies
     * @throws Exception
     */
    public Map<Integer, String> createMovieMap(String pathname) throws Exception{
        // initializing the map of IDs to movies
        Map<Integer, String> movieMap = new HashMap<>();
        BufferedReader input;

        // attempt to open the file
        try {
            input = new BufferedReader(new FileReader(pathname));
        } catch (FileNotFoundException e){
            System.err.println("Cannot open file");
            return movieMap;
        }

        try {

            // read through text file and add each ID mapped to the movie to the map
            String line;
            while ((line = input.readLine()) != null){
                String[] items = line.split("\\|");
                movieMap.put(Integer.parseInt(items[0]), items[1]);
            }

        } catch (Exception e){
            System.out.println(e);
        } finally {

            // close the buffered reader
            try {
                input.close();
            } catch(Exception e) {
                System.out.println(e);
            }
        }

        return movieMap;
    }

    /**
     * Given a map of IDs to actors, a map of IDs to movies, and a file of movie IDs and actor IDs, create a map
     * connecting each movie to the actors that starred in it
     *
     * @param actorMap - map of IDs to actors
     * @param movieMap - map of IDs to movies
     * @param pathname - file to read
     * @return - map of movies to a list of actors
     */
    public Map<String, List<String>> createMovieActorMap(Map<Integer, String> actorMap, Map<Integer, String> movieMap, String pathname) throws Exception{
        // initialize a map of movies to actors
        Map<String, List<String>> movieActormap = new HashMap<>();
        BufferedReader input;

        // if either of the given maps are empty, return an empty movie-actor map
        if (actorMap.isEmpty() || movieMap.isEmpty()){
            return movieActormap;
        }

        // attempt to initialize the reader
        try {
            input = new BufferedReader(new FileReader(pathname));
        } catch (FileNotFoundException e){
            System.err.println("Cannot open file");
            return movieActormap;
        }

        try {

            // while there is a line to read...
            String line;
            while ((line = input.readLine()) != null) {
                String[] items = line.split("\\|");

                // if the map doesn't already contain the movie...
                if (!movieActormap.containsKey(movieMap.get(Integer.parseInt(items[0])))){

                    // put that movie into the map with an empty array list
                    movieActormap.put(movieMap.get(Integer.parseInt(items[0])), new ArrayList<String>());
                }

                // add the actor to the list of actors associated with the movie
                movieActormap.get(movieMap.get(Integer.parseInt(items[0]))).add(actorMap.get(Integer.parseInt(items[1])));

            }
        } catch (Exception e){
            System.out.println(e);
        } finally {

            // close the buffered reader
            try {
                input.close();
            } catch(Exception e) {
                System.out.println(e);
            }
        }

        return movieActormap;
    }

    /**
     * Given a map of IDs to actors, and a map of movies to a list of actors, create a graph using the actors as
     * vertices and shared movies as edges between actors
     *
     * @param actorMap - map of IDs to actors
     * @param movieActorMap - map of movies to a list of actors
     * @return - a graph connecting each actor
     */
    public Graph<String, List<String>> createBaconGraph(Map<Integer, String> actorMap, Map<String, List<String>> movieActorMap){
        // initialize the graph
        Graph<String, List<String>> baconGraph = new AdjacencyMapGraph<>();

        // create an iterable key set for the actor map
        Iterable<Integer> keySet1 = actorMap.keySet();

        // for each key in the key set create a new vertex in the graph for the key's actor
        for (Integer ID : keySet1){
            baconGraph.insertVertex(actorMap.get(ID));
        }

        // create an iterable key set for the movie-actor map
        Iterable<String> keySet2 = movieActorMap.keySet();

        // for each movie in the movie-actor map...
        for (String movie : keySet2){

            // get the list of actors appearing in the movie
            List<String> actorList = movieActorMap.get(movie);

            // compare each actor in the list...
            for (int i = 0; i < actorList.size() - 1; i++){
                for (int j = i + 1; j < actorList.size(); j++){

                    // if the two actors don't already have an edge...
                    if (!baconGraph.hasEdge(actorList.get(i), actorList.get(j))){

                        // create an edge with an empty list
                        baconGraph.insertUndirected(actorList.get(i), actorList.get(j), new ArrayList<>());
                    }

                    // add movie to the edge
                    baconGraph.getLabel(actorList.get(i), actorList.get(j)).add(movie);
                }
            }
        }

        return baconGraph;
    }
}