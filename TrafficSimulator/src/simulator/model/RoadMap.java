package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	private Map<String,Junction> junctionsMap;
	private Map<String,Road> roadsMap;
	private Map<String,Vehicle> vehiclesMap;
	
	RoadMap(){
		this.junctions=new ArrayList<>();
		this.roads=new ArrayList<>();
		this.vehicles=new ArrayList<>();
		this.junctionsMap=new HashMap<>();
		this.roadsMap=new HashMap<>();
		this.vehiclesMap=new HashMap<>();
	}
	
	void addJunction(Junction j) {
		if(this.junctionsMap.containsKey(j.getId())) {
			throw new IllegalArgumentException("it cannot exists a junction with the same id");
		}
		this.junctions.add(j);
		this.junctionsMap.put(j.getId(), j);
	}
	
	void addRoad(Road r) {
		if(this.roadsMap.containsKey(r.getId())) {
			throw new IllegalArgumentException("it cannot exists a junction with the same id");
		}
		Junction srcJunc = r.getSrc();
	    Junction destJunc = r.getDest();

	    if (!junctionsMap.containsKey(srcJunc.getId()) || !junctionsMap.containsKey(destJunc.getId())) {
	        throw new IllegalArgumentException("Error: Source or destination junction does not exist for road " + r.getId());
	    }
	    srcJunc.addOutGoingRoad(r);  
	    destJunc.addIncommingRoad(r);
		this.roads.add(r);
		this.roadsMap.put(r.getId(), r);
		
	}
	
	void addVehicle(Vehicle v) {
		if (vehiclesMap.containsKey(v.getId())) {
	        throw new IllegalArgumentException("Error: Vehicle with id " + v.getId() + " already exists.");
	    }

	    List<Junction> itinerary = v.getItinerary();
	    
	    for (int i = 0; i < itinerary.size()-1; i++) {
	        Junction current = itinerary.get(i);
	        Junction next = itinerary.get(i+1);
	        if (current.roadTo(next) == null) {
	            throw new IllegalArgumentException("Error: No road connects " + current.getId() + " to " + next.getId() + " in vehicle " + v.getId());
	        }
	    }

	    vehicles.add(v); 
	    vehiclesMap.put(v.getId(), v); 
	    //v.moveToNextRoad();
	}
	
	public Junction getJunction(String id) {
		return this.junctionsMap.get(id);
	}
	
	public Road getRoad(String id){
		return this.roadsMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return this.vehiclesMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctions);
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roads);
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicles);
	}
	
	void reset() {
		this.junctions.clear();
		this.roads.clear();
		this.vehicles.clear();
		this.junctionsMap.clear();
		this.roadsMap.clear();
		this.vehiclesMap.clear();
	}
	
	public JSONObject report() {
		JSONObject json=new JSONObject();
		JSONArray arrayJuntions=new JSONArray();
		JSONArray arrayRoads=new JSONArray();
		JSONArray arrayVehicles=new JSONArray();
		
		
		for(Junction j:this.junctions) {
			arrayJuntions.put(j.report());
		}
		for(Road r:this.roads) {
			arrayRoads.put(r.report());
		}
		for(Vehicle v:this.vehicles) {
			arrayVehicles.put(v.report());
		}
		
		
		json.put("roads", arrayRoads);
		json.put("vehicles", arrayVehicles);
		json.put("junctions", arrayJuntions);
		
		return json;
	}

}
