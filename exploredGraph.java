import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Function;



// Here is the main application class:
public class ExploredGraph {
	Set<Vertex> Ve; // collection of explored vertices
	Set<Edge> Ee;   // collection of explored edges
	
	ArrayList<Vertex> path;		//declare var to keep track of path
	List<Operator> operators;	//declare List to keep track of operators
	
	public ExploredGraph() {
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		path = new ArrayList<>();
	}

	/*
	 *   Initializes the graph by calling the other initialize method
	 */
	public void initialize() {
		// Implement this
		Vertex startVertex = new Vertex("[[4,3,2,1],[],[]]");
		initialize(startVertex);
	}
	
	/*
	 * 	@param   vertex for which the graph should be initialized to
	 * 		constructs path and operator objects, adds initial vertex to list of Ve 
	 */
	public void initialize(Vertex vertex){
		Ve = new LinkedHashSet<Vertex>();
		Ee = new LinkedHashSet<Edge>();
		path = new ArrayList<>();
		operators = new ArrayList<>();
		Ve.add(vertex);
		for(int i = 0 ; i < 3; i++){
			for(int j = 0; j < 3; j++){
				operators.add(new Operator(i , j));
			}
		}
	}
	
	/*
	 * @return   return number of vertices checked
	 */
	public int nvertices() {
		return Ve.size();
	} 
	
	/*
	 * @return   return number of edges
	 */
	public int nedges() {
		return Ee.size();
	}
	
	/*
	 * @param   beginning vertex vi, and ending vertex vj
	 * 		performs a depth first search from Vertex vi to Vertex vj
	 */
	public void dfs(Vertex vi, Vertex vj) {
		
		Vertex currentVertex = vi;
		
		while(hasOptions(currentVertex)){
			for(Operator operator : operators){
				// If the operation is legal
				if(operator.getPrecondition().apply(currentVertex)){
					System.out.println("Precondition is legal");
					Vertex result = operator.getTransition().apply(currentVertex);
					
					if(!Ve.contains(result)){
						Edge edge = new Edge(currentVertex, result);
						Ee.add(edge);
						Ve.add(result);
						dfs(result, vj);
					}
				}
			}
		}
		
		
	}
	
	/*
	 * @param   vertex to be checked
	 * @return   boolean, return true of the vertex has legal options, false otherwise
	 */
	private boolean hasOptions(Vertex vertex){
		for(Operator operator : operators){
			// If the operation is legal
			if(operator.getPrecondition().apply(vertex)){
				System.out.println("Precondition is legal");
				Vertex result = operator.getTransition().apply(vertex);
				
				if(!Ve.contains(result)){
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * @param   beginning vertex vi, ending vertex vj
	 *     performs a breadth first search from vertex vi to vertex vj
	 */
	public void bfs(Vertex vi, Vertex vj) {
		System.out.println("Starting search from " + vi + " to " + vj);
		Queue<Vertex> queue = new LinkedList<>();
		queue.add(vi);
		Ve.add(vi);
		
		while(!queue.isEmpty()){
			Vertex currentVertex = queue.poll();
			for(Operator operator : operators){
				// If the operation is legal
				if(operator.getPrecondition().apply(currentVertex)){
					System.out.println("Precondition is legal");
					Vertex result = operator.getTransition().apply(currentVertex);
					
					if(!Ve.contains(result)){
						System.out.println("Result added to queue");
						queue.add(result);
						Edge edge = new Edge(currentVertex, result);
						Ee.add(edge);
						Ve.add(result);
					} else {
						System.out.println("We have already visited " + result);
					}
					
					if(result.equals(vj)){
						System.out.println("Done! Result : " + result + " and vj : " + vj );
						return;
					}
				} else {
					System.out.println("Operator " + operator + " is illegal for " + currentVertex);
				}
			}
		}
		
		System.out.println("Not found!");
	}
	
	/*
	 * @param   beginning vertex vi
	 * @return ArrayList<Vertex> of the path 
	 * 		returns the path created by vertex vi
	 */
	public ArrayList<Vertex> retrievePath(Vertex vi) {
		ArrayList<Vertex> path = new ArrayList<>();
		
		for(Vertex vertex : Ve){
			if(vertex.equals(vi)){
				vi = vertex;
				System.out.println("Vertex found, edges size is : " + vi.getEdges().size());
			}
		}
		
		Vertex currentVertex = vi;
		List<Edge> edges = vi.getEdges();
		
		System.out.println(edges.get(0).toString());
		System.out.println(edges.get(0).getEndpoint2().equals(vi));

		while(!isRoot(currentVertex)){
			for(Edge edge : edges){
				if(edge.getEndpoint2().equals(currentVertex)){
					path.add(currentVertex);
					currentVertex = edge.getEndpoint1();
					edges = currentVertex.getEdges();
					System.out.println("Moving to vertex: " + currentVertex);
					break;
				}
			}
		}
		
		System.out.println("Done with retrieve path");
		
		path.add(currentVertex);
		
		return path;
	}
	
	/*
	 * @param   vertex
	 * @return   boolean, true if passed vertex is root
	 * 		returns whether the passed vertex is a root
	 */
	private boolean isRoot(Vertex vertex){
		for(Edge edge : vertex.getEdges()){
			if(edge.getEndpoint2().equals(vertex))
				return false;
		}
		return true;
	}
	
	/*
	 * @param   beginning vertex vi, ending vertex vj
	 * @return   ArrayList<Vertex> 
	 * 		returns the shortest path between the two passed vertices
	 */
	public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
		bfs(vi, vj);
		
		return retrievePath(vj);
	}
	
	/*
	 * @return 	 Set<Vertex>
	 * 		returns the vertices that have been previously visited 
	 */
	public Set<Vertex> getVertices() {
		return Ve;
	} 
	
	/*
	 * @return    Set<Edge>
	 * 		returns the edges that have been stored in the set Ee
	 */
	public Set<Edge> getEdges() {
		return Ee;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExploredGraph eg = new ExploredGraph();
		// Test the vertex constructor: 
		Vertex v0 = eg.new Vertex("[[4,3,2,1],[],[]]");
		System.out.println(v0);

		Vertex v1 = eg.new Vertex("[],[],[4,3,2,1]");
		System.out.println("Vertex construction done");
		eg.initialize();
		
		eg.dfs(v0, v1);
		
		System.out.println(eg.retrievePath(v1));
	}
	
	class Vertex {
		ArrayList<Stack<Integer>> pegs; // Each vertex will hold a Towers-of-Hanoi state.
		// There will be 3 pegs in the standard version, but more if you do extra credit option A5E1.
		List<Edge> edges;
		
		// Constructor that takes a string such as "[[4,3,2,1],[],[]]":
		public Vertex(String vString) {
			String[] parts = vString.split("\\],\\[");
			pegs = new ArrayList<Stack<Integer>>(3);
			for (int i=0; i<3;i++) {
				pegs.add(new Stack<Integer>());
				try {
					parts[i]=parts[i].replaceAll("\\[","");
					parts[i]=parts[i].replaceAll("\\]","");
					List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
					//System.out.println("ArrayList for peg is: "+al);
					Iterator<String> it = al.iterator();
					while (it.hasNext()) {
						String item = it.next();
                                                if (!item.equals("")) {
                                                        //System.out.println("item is: "+item);
                                                        pegs.get(i).push(Integer.parseInt(item));
                                                }
					}
				}
				catch(NumberFormatException nfe) { nfe.printStackTrace(); }
			}		
			edges = new ArrayList<>();
		}
		
		public Stack<Integer> getPeg(int position){
			return pegs.get(position);
		}
		
		public int getTopOfPeg(int position){
			if(pegs.get(position).isEmpty())
				return Integer.MAX_VALUE;
			return pegs.get(position).peek();
		}
		
		public void move(int from, int to){
			int num = pegs.get(from).pop();
			pegs.get(to).push(num);
		}
		
		public String toString() {
			String ans = "[";
			for (int i=0; i<3; i++) {
			    ans += pegs.get(i).toString().replace(" ", "");
				if (i<2) { ans += ","; }
			}
			ans += "]";
			return ans;
		}
		
		public void addEdge(Edge edge){
			edges.add(edge);
		}
		
		public List<Edge> getEdges(){
			return edges;
		}
		/**
		 * Compares this Vertex to Object o
		 * @param o the object to be compared to
		 * @return a boolean indicating equality
		 */
		public boolean equals(Object o){
			try{
				Vertex v = (Vertex) o;
				
				return v.toString().equals(toString());
				
			} catch(ClassCastException e){
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return this.toString().hashCode();
		}
	}
	
	class Edge {
		private Vertex endpoint1;
		private Vertex endpoint2;
		
		/*
		 * @param   beginning vertex vi and ending vertex vj
		 * 		stores value of vertices in variables 
		 */
		public Edge(Vertex vi, Vertex vj) {
			// Add whatever you need to here.
			endpoint1 = vi;
			endpoint2 = vj;
			vi.addEdge(this);
			vj.addEdge(this);
		}
		
		/*
		 * @return   Vertex
		 * 		return Vertex of endpoint1
		 */
		public Vertex getEndpoint1(){
			return endpoint1;
		}
		
		/*
		 * @return    Vertex
		 * 		return Vertex of endpoint2
		 */
		public Vertex getEndpoint2(){
			return endpoint2;
		}
		
		/*
		 * @return   String
		 * 		returns String representation of endpoint to endpoint
		 */
		public String toString(){
			return "Edge from " + endpoint1.toString() + " to " + endpoint2.toString(); 
		}
		
	}
	
	class Operator {
		private int i, j;

		public Operator(int i, int j) {
			this.i = i;
			this.j = j;
		}

		Function<Vertex, Boolean> getPrecondition() {
			return new Function<Vertex, Boolean>() {
				@Override
				public Boolean apply(Vertex vertex) {
					return vertex.getTopOfPeg(i) < vertex.getTopOfPeg(j) ;
				}
			};
		}

		Function<Vertex, Vertex> getTransition() {
			return new Function<Vertex, Vertex>() {
				@Override
				public Vertex apply(Vertex vertex) {
					Vertex toReturn = new Vertex(vertex.toString());
					
					toReturn.move(i, j);
					
					return toReturn;
				}
			};
		}

		/*
		 * @return   String
		 * 		return string representation of operator
		 */
		public String toString() {
			return "From : " + i + " , To : " + j;
		}
	}

}
