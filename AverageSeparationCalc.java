import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is for calculating the average separation for each actor. It's in a separate class for organization
 * purposes
 *
 * @author Mason Childers, CS10, 23W
 */
public class AverageSeparationCalc {

    /**
     * Method to create a graph that maps each actor to their average separation
     *
     * @param baconGraph - graph of every actor
     * @param baconBFS - graph of all actors connected to Kevin Bacon
     * @param actorMap - map of every actor
     * @return - map of actors to their average separation
     */
    public Map<String, Double> createAverageSeparationMap(Graph<String, List<String>> baconGraph, Graph<String, List<String>> baconBFS, Map<Integer, String> actorMap) {
        // initializing the map and key set
        Map<String, Double> averageSeparationMap = new HashMap<>();
        Iterable<Integer> keySet = actorMap.keySet();

        // for each actor in the key set...
        for (int key : keySet) {

            // get the actor
            String actor = actorMap.get(key);

            // if that actor is connected to Kevin Bacon...
            if (baconBFS.hasVertex(actor)) {

                // make the actor the center of the universe and calculate the average separation
                Graph<String, List<String>> actorBFS = GraphLib.bfs(baconGraph, actor);
                double actorSeparation = GraphLib.averageSeparation(actorBFS, actor);

                // map the actor to their separation
                averageSeparationMap.put(actor, actorSeparation);
            }
        }

        return averageSeparationMap;
    }
}
