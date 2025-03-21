package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap{
	/**
	 * 
	 * Atributos
	 * 
	 * */
	
	// private
	private List<Junction> junctions;
	private List<Road> roads;
	private List<Vehicle> vehicles;
	
	private Map<String,Junction> junction_ids;
	private Map<String, Road> road_ids;
	private Map<String, Vehicle> vehicle_ids;
	
	
	/** Constructora package protected */
	RoadMap(){
		junctions = new ArrayList<>();
		roads = new ArrayList<>();
		vehicles = new ArrayList<>();
		
		junction_ids = new HashMap<>();
		road_ids = new HashMap<>();
		vehicle_ids = new HashMap<>();
	}
	
	// Metodos de añadir
	void addJunction(Junction j) throws IllegalArgumentException{
		if(junction_ids.containsKey(j.getId()))
			throw new IllegalArgumentException("Junction is already in the list: " + j.getId());
		
		junctions.add(j);
		junction_ids.put(j.getId(), j);
	}
	
	void addRoad(Road r) throws IllegalArgumentException{
		if(road_ids.containsKey(r.getId()))
			throw new IllegalArgumentException("Road is already in the list: " + r.getId());
		
		if(junction_ids.containsValue(r.getSrc()) && junction_ids.containsValue(r.getDest())) {
			roads.add(r);
			road_ids.put(r.getId(), r);
		}else
			throw new IllegalArgumentException("Src or Dest Junction does not exist in the map junction: " + r.getSrc() + ", " + r.getDest());
	}
	
	void addVehicle(Vehicle v) throws IllegalArgumentException{
		if (vehicle_ids.containsKey(v.getId()))
			throw new IllegalArgumentException("Vehicle already in the list: " + v.getId());
		for (int i = 0; i < v.getItinerary().size()-1; i++) {
			Junction current = v.getItinerary().get(i);
			Junction next = v.getItinerary().get(i+1);
			if(current.roadTo(next) == null) {
				throw new IllegalArgumentException("Itinerary not valid because not found road between: " + current + " and " + next + " for vehicle " + v.getId());
			}
		}
		vehicles.add(v);
		vehicle_ids.put(v.getId(), v);
	}
	
	// Metodos get
	public Junction getJunction(String id) {
		return junction_ids.get(id);
	}
	
	public Road getRoad(String id) {
		return road_ids.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return vehicle_ids.get(id);
	}
	
	public List<Junction> getJunctions(){
		return junctions;
	}
	
	public List<Road> getRoads(){
		return roads;
	}
	
	public List<Vehicle> getVehicles(){
		return vehicles;
	}

	
	// Otros metodos
	void reset() {
		junctions.removeAll(junctions);
		roads.removeAll(roads);
		vehicles.removeAll(vehicles);
		
		junction_ids.clear();
		road_ids.clear();
		vehicle_ids.clear();
	}
	
	public JSONObject report() {
		JSONObject json=new JSONObject();
		
		JSONArray json_junctions=new JSONArray();
		for(Junction j: junctions) {
			json_junctions.put(j.report());
		}
		json.put("junctions", json_junctions);
		
		JSONArray json_roads=new JSONArray();
		for(Road r: roads) {
			json_roads.put(r.report());
		}
		json.put("roads", json_roads);
		
		JSONArray json_vehicles=new JSONArray();
		for(Vehicle v: vehicles) {
			json_vehicles.put(v.report());
		}
		
		json.put("vehicles", json_vehicles);
		return json;
	}
}
