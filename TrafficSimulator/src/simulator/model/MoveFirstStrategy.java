package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveFirstStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		if (q.isEmpty()) {
	        return new ArrayList<>();
	    }
		List<Vehicle> list = new ArrayList<>();
	    list.add(q.get(0));  
	    return list;
	}

}
