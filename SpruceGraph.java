package proj1;

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
	Map<Integer, Integer> visited; // initialize an empty hash map of vertices and their traverse status
	
	// Constructor
	SpruceGraph(){
		graph = new HashMap<Integer, HashSet<Integer>>();
		visited = new HashMap<Integer, Integer>();
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
	 * Reads a txt file of contig IDs and put them into an array list
	 * @param f: file name
	 * @return: an array list of contig IDs
	 */
	private static List<Integer> constructNeighbours(String f){
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
//		int mid_pt = neighbour_IDs.size()/2;
//		List<Integer> left_neigh = neighbour_IDs.subList(0, mid_pt);
//		List<Integer> right_neigh = neighbour_IDs.subList(mid_pt, neighbour_IDs.size());
//		Map<String, List<Integer>> edge_set = new HashMap<String, List<Integer>>();
//		edge_set.put("left_neigh", left_neigh);
//		edge_set.put("right_neigh", right_neigh);
		return neighbour_IDs;
	}
	
	
	/**
	 * Take two vertices, construct an edge between them and add it to the undirected graph. Their traverse status are also registered.
	 * @param l: left vertex
	 * @param r: right vertex
	 */
	private void addEdge(int l, int r) {
		graph.putIfAbsent(l, new HashSet<Integer>()); // if this vertex is not in the graph yet, include it and give it an empty HashSet to store its neighbours
		graph.putIfAbsent(r, new HashSet<Integer>());
		graph.get(l).add(r); // add vertex r as a neighbour of vertex l
		//graph.get(l).add(l);
		graph.get(r).add(l);
		//graph.get(r).add(r);
		visited.put(l, 0); // at point of construction, register all vertices as unvisited
		visited.put(r, 0);
	}
	
	
	/**
	 * DFS traversal. Counts also the size of the component.
	 * Time complexity: O(component.size)
	 * @param vertex
	 * @return: size of component
	 */
	private int DFS(int vertex) {
		visited.put(vertex, 1); // check the visited button for this vertex
		int size = 1;
		for (Integer neighbour : graph.get(vertex)) {	// iterate over the neighbours of this vertex
			if(visited.get(neighbour) == 0) {
				size += DFS(neighbour); // recursively add the size output into the size count of the component 
			}
		}
		return size;
	}
	
	
	
	/** Overall time complexity is O(n) + O(n) = O(2n). So time complexity is O(n)
	 * Combo method that counts number of connected components in the graph by doing DFS traversal through the whole graph AND component size distribution
	 * @return: a list of object. Object 1: number of connected components; Object 2: component size distribution
	 */
	private List<Object> CountComponent() {
		List<Object> object = new ArrayList<Object>();
		List<Integer> comp_sizes = new ArrayList<Integer>();
		int count = 0;
		for (Integer vertex : visited.keySet()) { // iterate over all vertices so O(n) amount of work done
			if (visited.get(vertex) == 0) { // if vertex is not visited, 
				comp_sizes.add(DFS(vertex)); // visit it and get the size of the component
				count += 1;
			}
		}
		Map<Integer, Long> size_dist = comp_sizes.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting())); //not totally sure of its time complexity. But it should be O(n) if it iterates over all elements and keep counters for each one of them. 
		object.add(count);
		object.add(size_dist);
		return object;
	}
	
	
	/** Overall time complexity is O(n) + O(n) = O(2n). So time complexity is O(n)
	 * Computes the degree distribution of the graph
	 * @return: a map of (number of neighbours, frequency)
	 */
	
	private Map<Integer, Long> DegreeDist(){
		List<Integer> neigh_deg = new ArrayList<Integer>(); //uses dynamic memory.
		for (Integer vertex : graph.keySet()) { // iterate over all vertices. So O(n) amount of work
			int degree = graph.get(vertex).size();
			neigh_deg.add(degree);
		}
		Map<Integer, Long> counts = neigh_deg.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		return counts;
	}
	
	
	/**
	 * Print the graph as: vertex -> [neighbours]
	 */
	private void printGraph() {
		for (Integer vertex : graph.keySet()) {
			System.out.println(vertex.toString() + ": " + graph.get(vertex));
		}
	}
	

	public static void main(String[] args) { //arg[0]:left neighbours; arg[1]:right neighbours
		SpruceGraph graph = new SpruceGraph();
		String left_neigh_file = "src/proj1/l3.txt"; // corresponds to column 1 in data set
		String right_neigh_file = "src/proj1/r3.txt"; // corresponds to column 2 in data set
		
		List<Integer> left_neigh_ID = constructNeighbours(left_neigh_file);
		List<Integer> right_neigh_ID = constructNeighbours(right_neigh_file);
		
		
		for (int i=0; i<left_neigh_ID.size(); i++) {
			graph.addEdge(left_neigh_ID.get(i), right_neigh_ID.get(i));
		}
		
		//graph.printGraph();
		
		// compute degree distribution
		System.out.println("Degree distribution: " + graph.DegreeDist());
		
		System.out.println("");
		
		List<Object> comp_result = graph.CountComponent();
		// compute number of connected component
		System.out.println("Number of components: " + comp_result.get(0));
		
		System.out.println("");
		
		// compute component size distribution
		System.out.println("Component size distribution: " + comp_result.get(1));

		
	}

}
