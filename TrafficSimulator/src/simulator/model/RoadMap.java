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
		if(this.roadsMap.containsKey(r.getId())||this.roadsMap.containsKey(r.get_destJunc().getId())||this.roadsMap.containsKey(r.get_srcJunc().getId())) {
			throw new IllegalArgumentException("it cannot exists a junction with the same id");
		}
		this.roads.add(r);
		this.roadsMap.put(r.getId(), r);
		
	}
	
	void addVehicle(Vehicle v) {
		
		if(this.vehiclesMap.containsKey(v.getId())) {
			throw new IllegalArgumentException("it cannot exists a vehicle with the same id");
		}
		
		List<Junction>listJunctions=v.get_itinerary();
		
		for(int i=0;i<listJunctions.size();i++) {
			Junction actual=listJunctions.get(i);
			Junction next=listJunctions.get(i+1);
			if(actual.roadTo(next)==null) {
				throw new IllegalArgumentException("No road connect "+actual.getId()+" with "+next.getId());
			}
		}
		
		//existen carreteras que conecten los cruces consecutivos de su itinerario
		
		this.vehicles.add(v);
		this.vehiclesMap.put(v.getId(), v);
		
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
			arrayJuntions.put(j.getId());
		}
		for(Road r:this.roads) {
			arrayRoads.put(r.getId());
		}
		for(Vehicle v:this.vehicles) {
			arrayVehicles.put(v.getId());
		}
		
		json.put("junctions", arrayJuntions);
		json.put("road", arrayRoads);
		json.put("vehicles", arrayVehicles);
		
		
		return null;
	}

}
