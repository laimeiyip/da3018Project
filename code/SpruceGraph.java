package proj1;

/**
 * If running in eclipse, do the following:
 * 
 * right click on the java file, go to:
 * Run As - Run Configuration - Arguments - Vm Arguments
 * then write:
 * -Xmx7168m
 * -Xss1024m
 * 
 * This is done to resolve memory issue. Also, see the main method to uncomment the right chunk.
 * Please make sure the data files are stored in the correct directory.
 * 
 * If running on command line, there are two ways:
 * 
 * 1) run Spruce.jar
 * java -Xmx7168m -Xss1024m -Djava.awt.headless=true -jar Spruce.jar <left vertices file> <rright vertices file>
 * 
 * 2) run SpruceGraph.java
 * first: javac proj1/SpruceGraph.java 
 * then: java -Xmx7168m -Xss1024m -Djava.awt.headless=true proj1/SpruceGraph <left vertices file> <rright vertices file>
 * 
 * For the convenience of evaluating my programme, user can find 
 * <left vertices file> as lfull1.txt
 * <right vertices file> as rfull1.txt
 * 
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SpruceGraph {
	
	Map<Integer, HashSet<Integer>> graph; // initialize an empty hash map of vertices and their neighbours
	
	// Constructor
	SpruceGraph(){
		graph = new HashMap<Integer, HashSet<Integer>>();
	}
	
	
	/**
	 * Create a hash map of contigs
	 * @param ID: txt file containing ID-contig key-val pairs
	 * @return a hash map of contigs
	 */
	
	// An earlier method to construct a hash map of contig IDs.
	// Not feasible so not used. For own reference.
	
//	private static Map<Integer, String> construct_ID_map(String ID) {
//		Map<Integer, String> ID_Map = new HashMap<Integer, String>();
//		File contig_ID = new File(ID);
//		try {
//			Scanner sc = new Scanner(contig_ID);
//			while (sc.hasNextLine()) {
//				String input = sc.nextLine();
//				String[] elem = input.split(" ");
//				ID_Map.put(Integer.parseInt(elem[0]), elem[1]);
//			}
//			sc.close();
//		} catch (FileNotFoundException e) {
//			System.err.println("File not found.");
//			System.exit(0);
//		}
//		return ID_Map;
//	}
	
	
	/**
	 * Assign ID to neighbours in the edge list
	 * @param f: file containing neighbours on one side, ID_map
	 * @return a list of neighbours identified by ID numbers
	 */
	
	// An earlier method to assign ID numbers to edge set.
	// Not feasible due thirst for memory. For own reference.
	
//	private static List<Integer> assign_id(String f, Map<Integer, String> m){
//		List<Integer> neighbour_ID = new ArrayList<Integer>();
//		File contigs = new File(f);
//		try {
//			Scanner sc = new Scanner(contigs);
//			while (sc.hasNextLine()) {
//				String inputString = sc.nextLine();
//				for (Integer id : m.keySet()) {
//					if (inputString.compareTo(m.get(id))==0);
//					neighbour_ID.add(id);
//				}
//			}sc.close();
//		} catch (FileNotFoundException e) {
//			System.err.println("File not found.");
//			System.exit(0);
//		}
//		return neighbour_ID;
//	}
	
	
	/**
	 * Reads a text file of contig IDs and put them into an array list
	 * @param f: file name
	 * @return: an array list of contig IDs
	 */
	private static List<Integer> ConstructNeighbours(String f){
		List<Integer> neighbour_IDs = new ArrayList<Integer>();
		File neighs = new File(f);
		try {
			Scanner sc = new Scanner(neighs);
			while (sc.hasNextLine()) {
				String inputString = sc.nextLine();
				neighbour_IDs.add(Integer.parseInt(inputString));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.exit(0);
		}
		return neighbour_IDs;
	}
	
	
	/**
	 * Take two vertices, construct an edge between them and add it to the undirected graph. 
	 * @param l: left vertex
	 * @param r: right vertex
	 */
	private void AddEdge(int l, int r) {
		graph.putIfAbsent(l, new HashSet<Integer>()); // if this vertex is not in the graph yet, include it and give it an empty HashSet to store its neighbours
		graph.putIfAbsent(r, new HashSet<Integer>());
		graph.get(l).add(r); // add vertex r as a neighbour of vertex l
		graph.get(r).add(l);
	}
	
	
	/**
	 * DFS traversal. Counts also the size of the component.
	 * Time complexity: O(number of edges connected to a vertex) 
	 * @param: start vertex; a map of the visit statuses of all vertices
	 * @return: size of component
	 */
	private int DFS(int vertex, Map<Integer, Integer> visited) {
		visited.put(vertex, 1); // check the visited button for this vertex
		int size = 1;
		for (Integer neighbour : graph.get(vertex)) {	// iterate over the neighbours of this vertex
			if(visited.get(neighbour) == 0) {
				size += DFS(neighbour, visited); // recursively add the size output into the size count of the component 
			}
		}
		return size;
	}
	
	/**
	 * Compute component size distribution
	 * Time complexity: O(|V|) + O(|V|) + O(|E|) = O(2|V|) = O(|V|) + O(|E|)
	 * @param keys: the set of keys to the graph
	 * @return: a hash map of the component size distribution
	 */
	private Map<Integer, Long> ComponentDistr(List<Integer> keys) {
		List<Integer> comp_sizes = new ArrayList<Integer>();
		Map<Integer, Integer> visited = new HashMap<Integer, Integer>(); // initialize a hash map of visit statuses of all vertices
		for (Integer k : keys) { // iterate over all vertices. O(|V|) amount of work.
			visited.put(k, 0); // initialize visit status of every vertex
		}
		for (Integer vertex : visited.keySet()) {// iterate over all vertices. O(|V|) amount of work.
			if (visited.get(vertex) == 0) {
				comp_sizes.add(DFS(vertex, visited)); // DFS has worst case time complexity O(|E|)
			}
		}
		Map<Integer, Long> size_dist = comp_sizes.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting())); //not totally sure of its time complexity. But it should be O(n) if it iterates over all elements and keep counters for each one of them. 
		return size_dist;
	}
	
	/** Overall time complexity: O(|V|) + O(|V|) + O(|E|) = O(2|V|) + O(|E|). So time complexity is O(|V|) + O(|E|).
	 * Counts number of connected components in the graph by doing DFS traversal through the whole graph
	 * @return: an integer which is the number of components
	 */
	private int CountComponent(List<Integer> keys) {
		Map<Integer, Integer> visited = new HashMap<Integer, Integer>(); // initialize a hash map of visit statuses of all vertices
		for (Integer k : keys) { // iterate over all vertices so O(|V|) amt of work.
			visited.put(k, 0); // initialize visit status of every vertex
		}
		int count = 0;
		for (Integer vertex : visited.keySet()) { // iterate over all vertices so O(|V|) amount of work done
			if (visited.get(vertex) == 0) { // if vertex is not visited, 
				DFS(vertex, visited); // visit it. The recursive calls in DFS will visit all of its neighbours and once this is done, we know that this is one component. DFS has O(|E|)
				count += 1;
			}
		}
		return count;
	}
	
	
	/** Overall time complexity is O(n) + O(n) = O(2n). So time complexity is O(n)
	 * Computes the degree distribution of the graph
	 * @return: a map of (number of neighbours, frequency)
	 */
	
	private Map<Integer, Long> DegreeDist(){
		List<Integer> neigh_deg = new ArrayList<Integer>(); //uses dynamic memory.
		for (Integer vertex : graph.keySet()) { // iterate over all vertices. So O(V) amount of work
			int degree = graph.get(vertex).size();
			neigh_deg.add(degree);
		}
		Map<Integer, Long> counts = neigh_deg.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		return counts;
	}
	
	
	/**
	 * Utility function that gets the whole key set of the graph.
	 * @return: key set of the graph
	 */
	private List<Integer> GetKeys(){
		List<Integer> keys = new ArrayList<Integer>();
		for (Integer k : graph.keySet()) {
			keys.add(k);
		}
		return keys;
	}
	
	
	/**
	 * Utility function that prints the neighbour set of every vertex
	 * Print the graph as: vertex -> [neighbours]
	 */
	private void PrintGraph() {
		for (Integer vertex : graph.keySet()) {
			System.out.println(vertex.toString() + ": " + graph.get(vertex));
		}
	}
	
	

	public static void main(String[] args) { //arg[0]:left neighbours; arg[1]:right neighbours
		SpruceGraph graph = new SpruceGraph();
		
		// for running with file path and file name in eclipse
		String left_neigh_file = "src/proj1/lfull1.txt"; // corresponds to column 1 in data set
		String right_neigh_file = "src/proj1/rfull1.txt"; // corresponds to column 2 in data set
		
		// for running at command line
		//String left_neigh_file = args[0]; // corresponds to column 1 in data set
		//String right_neigh_file = args[1]; // corresponds to column 2 in data set
		
		List<Integer> left_neigh_ID = ConstructNeighbours(left_neigh_file);
		List<Integer> right_neigh_ID = ConstructNeighbours(right_neigh_file);
		
		
		for (int i=0; i<left_neigh_ID.size(); i++) {
			graph.AddEdge(left_neigh_ID.get(i), right_neigh_ID.get(i));
		}
		
		//graph.PrintGraph();
		
		// compute degree distribution
		System.out.println("Degree distribution: " + graph.DegreeDist());
		
		// prints out the number of counts per degree type. For plotting figures.
//		System.out.println(graph.DegreeDist().keySet());
//		System.out.println(graph.DegreeDist().values());
		
		System.out.println("");
		
		// print number of components
		List<Integer> keyset = graph.GetKeys(); // retrieve the keys to the graph 
		System.out.println("Number of components: " + graph.CountComponent(keyset));
		
		System.out.println("");
		
		// print component size distribution
		System.out.println("Component size distribution: " + graph.ComponentDistr(keyset));
		
		// prints out the number of counts per component size. For plotting figures.
//		System.out.println(graph.ComponentDistr(keyset).keySet());
//		System.out.println(graph.ComponentDistr(keyset).values());


		
	}

}
