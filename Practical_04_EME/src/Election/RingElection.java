package Election;

import java.util.ArrayList;
import java.util.List;

class Process{
	int id;
	boolean active;
	Process(int id) {
		this.id=id;
		this.active=true;
	}
}
public class RingElection {
	static List<Process> ringList = new ArrayList<>();
	static int coordinator = -1;
	
	static void startElection(int startId) {
		System.out.println("\nProcess "+startId+ " starts elction...");
		List<Integer> activeIds = new ArrayList<>();
		
		int n = ringList.size();
		int current = startId;
		do {
			Process p = ringList.get(current);
			if (p.active) {
				activeIds.add(p.id);
				System.out.println("Election Message at process"+p.id);
			}
		}while()
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
