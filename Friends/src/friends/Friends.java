package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;


public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		if(g==null || p1 == null || p2 == null) {
			return null;
		}
		if(g.map.get(p1)==null || g.map.get(p2)==null) {
			return null;
		}
		String Hold = p1;
		p1 = p2;
		p2 = Hold;
		ArrayList<String> tmp = new ArrayList<String>();
		ArrayList<Integer> Prev = new ArrayList<Integer>();
		for(int i = 0; i < g.members.length; i++) {
			Prev.add(null);
		}
		Queue<Integer> TrevQue = new Queue<Integer>();
		TrevQue.enqueue(g.map.get(p1));
		Prev.set(g.map.get(p1), g.map.get(p1));
		
		
		while (!TrevQue.isEmpty()){
			int PreHold = TrevQue.dequeue();
			Friend PtrPreHold = g.members[PreHold].first;
			while(PtrPreHold != null) {
				if(Prev.get(PtrPreHold.fnum) == null) {
					Prev.set(PtrPreHold.fnum, PreHold);
					TrevQue.enqueue(PtrPreHold.fnum);
				}
				PtrPreHold = PtrPreHold.next;
			}
		}
		if(Prev.get(g.map.get(p2)) != null) {
			int ptr = g.map.get(p2);
			while(ptr != g.map.get(p1)) {
				tmp.add(g.members[ptr].name);
				ptr = Prev.get(ptr);
			}
			tmp.add(g.members[ptr].name);
			
			return tmp;
		}
		
		return null;
	}
	/** COMPLETE THIS METHOD **/

	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		/** COMPLETE THIS METHOD **/

		if(g==null) {
			return null;
		}
		ArrayList<ArrayList<String>> tmp = new ArrayList<ArrayList<String>>();
		ArrayList<Boolean> Visited = new ArrayList<Boolean>();
		ArrayList<Person> PList;
		for(int i = 0; i < g.members.length; i++) {
			if(g.members[i].school == null) {
				Visited.add(true);
			}
			else if(!(g.members[i].school.equals(school))) {
				Visited.add(true);
			}
			else {
				Visited.add(false);
			}
		}
		
		
		ArrayList<String> add;
		for(int i = 0; i < g.members.length; i++) {
			if(!Visited.get(i)) {
				PList = new ArrayList<Person>();
				Queue<Person> Que = new Queue<Person>();
				Que.enqueue(g.members[i]);
				Visited.set(i, true);
				while(!Que.isEmpty()) {
					Person Hold = Que.dequeue();
					PList.add(Hold);
					Friend PtrHold = Hold.first;
					while(PtrHold != null) {
						if(!Visited.get(PtrHold.fnum)) {
							Que.enqueue(g.members[PtrHold.fnum]);
							Visited.set(PtrHold.fnum, true);
						}
						PtrHold = PtrHold.next;
					}
				}
				if(!PList.isEmpty()) {
					add = Convert(PList, school);
					if(add != null) {
						tmp.add(add);
					}
				}
			}
		}
		if(tmp.isEmpty()) {
			return null;
		}
		
		return tmp;
		
	}
	
	private static ArrayList<String> Convert(ArrayList<Person> P, String school){
		ArrayList<String> tmp = new ArrayList<String>();
		if(P == null) {
			return null;
		}
		for(int i = 0; i < P.size(); i++) {
			if(P.get(i).school != null) {
				if(P.get(i).school.equals(school)) {
					tmp.add(P.get(i).name);
				}
			}
		}
		
		if(tmp.isEmpty()) {
			return null;
		}
		
		return tmp;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		/** COMPLETE THIS METHOD **/

		ArrayList<String> tmp = new ArrayList<String>();
		if(g == null || g.members.length <= 2) 
			return null;
		ArrayList<Integer> counter = new ArrayList<Integer>();
		counter.add(1);
		ArrayList<ArrayList<ArrayList<Integer>>> reIn = new ArrayList<ArrayList<ArrayList<Integer>>>();
		for(int i = 0; i < g.members.length; i++) {
			ArrayList<ArrayList<Integer>> Input = new ArrayList<ArrayList<Integer>>();
			for(int j = 0; j < g.members.length; j++) {
				Input.add(null);
			}
			Input.add(counter);
			Input = Connect(Input, true, i, g);
			reIn.add(Input);
		}
		if(reIn.size() == 0) {
			return null;
		}
		for(int i = 0; i < reIn.size(); i++) {
			ArrayList<ArrayList<Integer>> Input = reIn.get(i);
			for(int j = g.members.length +1; j < Input.size(); j++) {
				if(compress(g.members[Input.get(j).get(0)].name, tmp))
					tmp.add(g.members[Input.get(j).get(0)].name);
			}
		}
		if(tmp.size() == 0) {
			return null;
		}
		return tmp;
	}
	
	private static boolean compress(String test, ArrayList<String> tester) {
		if(tester == null) {
			return true;
		}
		for(int i = 0; i < tester.size(); i++) {
			if(tester.get(i).compareTo(test) == 0) {
				return false;
			}
		}
		return true;
	}
	
	private static ArrayList<ArrayList<Integer>> Connect(ArrayList<ArrayList<Integer>> List, boolean first, int Vertex, Graph g){
		ArrayList<Integer> New = new ArrayList<Integer>();
		New.add(List.get(g.members.length).get(0));
		New.add(List.get(g.members.length).get(0));
		List.set(Vertex, New);
		List.get(g.members.length).set(0, List.get(g.members.length).get(0) + 1);
		Friend ptr = g.members[Vertex].first;
		while(ptr != null) {
			int ptrIn = ptr.fnum;
			if(List.get(ptrIn) != null) {
				if(List.get(Vertex).get(1) > List.get(ptrIn).get(0)) {
					List.get(Vertex).set(1, List.get(ptrIn).get(0));
				}
			}
			else {
				List = Connect(List, false, ptrIn, g);
				if(List.get(Vertex).get(0) > List.get(ptrIn).get(1)) {
					if(List.get(Vertex).get(1) > List.get(ptrIn).get(1)) {
						List.get(Vertex).set(1, List.get(ptrIn).get(1));
					}
				}
				else if(List.get(Vertex).get(0) <= List.get(ptrIn).get(1)) {
					if(!first) {
						ArrayList<Integer> add = new ArrayList<Integer>();
						add.add(Vertex);
						List.add(add);
					}
				}
			}
			ptr = ptr.next;
		}
		return List;
	}
}
