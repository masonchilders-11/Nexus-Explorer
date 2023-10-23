import java.util.*;

/**
 * Library for graph analysis
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2016
 * @author Mason Childers, CS 10, 23W
 */
public class GraphLib {

	/**
	 * BFS method returning the shortest path tree of children vertices
	 *
	 * @param g - the original graph
	 * @param source - the vertex you're searching from
	 * @return - a path tree of children vertices with their grandchildren and so on
	 */
	public static <V,E> Graph<V,E> bfs(Graph<V,E> g, V source) {
		// initializing the path tree and the queue used for BFS
		Graph<V, E> pathTree = new AdjacencyMapGraph<>();
		Queue<V> queue = new LinkedList<>();

		// add the source to the queue and insert it as a vertex into the path tree
		queue.add(source);
		pathTree.insertVertex(source);

		// while the queue isn't empty...
		while (!queue.isEmpty()) {

			// make new vertex what is at the front of the queue and loop over all of its neighbors
			V newVertex = queue.remove();
			for (V vertex : g.outNeighbors(newVertex)) {

				// if the path tree doesn't already have the neighbor...
				if (!pathTree.hasVertex(vertex)) {

					// add neighbor to the tree
					pathTree.insertVertex(vertex);

					// add the neighbor to the queue
					queue.add(vertex);

					// insert a directed edge from the neighbor to the initially dequeued vertex with the original graph's label
					pathTree.insertDirected(vertex, newVertex, g.getLabel(vertex, newVertex));
				}
			}
		}

		return pathTree;
	}

	/**
	 * Method to determine the shortest path from the given vertex to the source vertex that bfs was conducted on
	 *
	 * @param tree - the shortest path tree found using bfs
	 * @param v - vertex you want to trace back to source
	 * @return - a path from the given vertex to the source
	 */
	public static <V,E> List<V> getPath(Graph<V,E> tree, V v){
		// initialize path and the starting vertex
		List<V> path = new ArrayList<>();
		V current = v;

		// if the tree doesn't contain the given starting vertex, conclude it can't be reached and return an empty path
		if (!tree.hasVertex(v)){
			System.out.println(v + " is not in the path tree -- there is no connection");
			return path;
		}

		// while there is still an out neighbor to get...
		while (tree.outNeighbors(current).iterator().hasNext()){

			// add the current vertex to the path and set the new current vertex to the only out neighbor
			path.add(current);
			current = tree.outNeighbors(current).iterator().next();
		}

		// add the final vertex to the path
		path.add(current);

		return path;
	}

	/**
	 * Given an original graph and a subgraph of the same graph, return the vertices that aren't included in both
	 *
	 * @param graph - the original graph
	 * @param subgraph - the subgraph of the original graph
	 * @return - a set of the missing vertices
	 */
	public static <V,E> Set<V> missingVertices(Graph<V,E> graph, Graph<V,E> subgraph){
		// initialize the set of missing vertices
		Set<V> missingVertices = new HashSet<>();

		// create an iterable collection of all the vertices in the original graph
		Iterable<V> allVertices = graph.vertices();

		// for every vertex in the collection of all vertices...
		for (V vertex : allVertices){

			// if the subgraph doesn't contain a vertex...
			if (!subgraph.hasVertex(vertex)){

				// add that vertex to the list of missing vertices
				missingVertices.add(vertex);
			}
		}

		return missingVertices;
	}

	/**
	 * Uses a helper method to calculate the average separation of a given vertex and its bfs tree
	 * @param tree - the shortest path tree
	 * @param root - the center of the universe
	 * @return - the average separation as a double
	 */
	public static <V,E> double averageSeparation(Graph<V,E> tree, V root){
		// gets the number of possible paths and uses the helper function to find the total number of movement throughout those paths
		int connectedActors = tree.numVertices() - 1;
		int totalMovement = averageSeparationHelper(0, tree, root);

		// returns the average separation
		return (double) totalMovement/connectedActors;
	}

	/**
	 * Helper method that calculates the total movement throughout the paths using recursion
	 * @param totalMovement - the total amount of movement for that path
	 * @param tree - the shortest path tree
	 * @param root - the current root being used for recursion
	 * @return - the total movement adding up to the current path
	 */
	public static <V,E> int averageSeparationHelper(int totalMovement, Graph<V,E> tree, V root){
		int movement = totalMovement;

		for (V inVertex : tree.inNeighbors(root)){
			movement += averageSeparationHelper(totalMovement + 1, tree, inVertex);
		}

		return movement;
	}

	/**
	 * Takes a random walk from a vertex, up to a given number of steps
	 * So a 0-step path only includes start, while a 1-step path includes start and one of its out-neighbors,
	 * and a 2-step path includes start, an out-neighbor, and one of the out-neighbor's out-neighbors
	 * Stops earlier if no step can be taken (i.e., reach a vertex with no out-edge)
	 * @param g		graph to walk on
	 * @param start	initial vertex (assumed to be in graph)
	 * @param steps	max number of steps
	 * @return		a list of vertices starting with start, each with an edge to the sequentially next in the list;
	 * 			    null if start isn't in graph
	 */
	public static <V,E> List<V> randomWalk(Graph<V,E> g, V start, int steps) {
		// TODO: your code here

		// initializing the list to return
		List<V> walkedList = new ArrayList<>();
		walkedList.add(start);

		// if steps are 0, just return the list with the start in it
		if (steps == 0) ;

		// if the steps are 1 just get one neighbor and add it to the list, then return the list
		else if (steps == 1) {

			// if the start has neighbors...
			if (g.outDegree(start) != 0) {

				// define a random number that allows me to iterate randomly over the neighbors and initialize the first neighbor
				Iterator<V> iterator = g.outNeighbors(start).iterator();
				int neighborInt = (int) (Math.random() * (g.outDegree(start)));
				V neighbor = iterator.next();

				// while the number of iterations to do isn't 0...
				while (neighborInt != 0) {

					// set neighbor equal to the new iteration and decrement the number of iterations to do
					neighbor = iterator.next();
					neighborInt--;
				}

				// add the final neighbor to the list of walked neighbors
				walkedList.add(neighbor);

			}
		}

		// otherwise the steps are more than 1 and I must repeat the step above for every neighbor visited
		else {

			// initialize the current vertex as the start, and as long as there are neighbors and steps...
			for (V current = start; g.outDegree(current) != 0 && steps != 0; steps--) {

				// do the same steps as above...
				Iterator<V> iterator = g.outNeighbors(current).iterator();
				int neighborInt = (int) (Math.random() * (g.outDegree(current)));
				V neighbor = iterator.next();

				while (neighborInt != 0) {
					neighbor = iterator.next();
					neighborInt--;
				}

				// but also set the current to the neighbor that has been newly visited
				walkedList.add(neighbor);
				current = neighbor;
			}

		}

		return walkedList;
	}

	/**
	 * Orders vertices in decreasing order by their in-degree
	 * @param g		graph
	 * @return		list of vertices sorted by in-degree, decreasing (i.e., largest at index 0)
	 */
	public static <V,E> List<V> verticesByInDegree(Graph<V,E> g) {
		// TODO: your code here
		Iterable<V> vertexIterable = g.vertices();
		List<V> vertexList = new ArrayList<>();

		for (V vertex : vertexIterable) {
			vertexList.add(vertex);
		}

		Comparator<V> compare = new InDegreeComparator<V, E>(g);
		vertexList.sort(compare);
		return vertexList;
	}
}
