package simulator.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	
	private List<Road> roads; // Carreteras entrantes
	private Map<Junction, Road> dest_road; // Carreteras Salientes
	private List<List<Vehicle>> queues; // Colas del cruce
	
	private int curr_green;
	private int last_green; // Ultimo paso de cambio de semaforo
	
	private LightSwitchingStrategy light_strategy;
	private DequeuingStrategy queue_strategy;
	
	private int x;
	private int y;
	
	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) 
			throws IllegalArgumentException{
		  super(id);
		  
		  if (lsStrategy != null)
			  light_strategy = lsStrategy;
		  else
			  throw new IllegalArgumentException("LightSwitchStrategy is null");
		  
		  if (dqStrategy != null)
			  queue_strategy = dqStrategy;
		  else
			  throw new IllegalArgumentException("DequeuingStrategy is null");
		  
		  if (xCoor >= 0 && yCoor >= 0) {
			  x = xCoor;
			  y = yCoor;
		  }else
			  throw new IllegalArgumentException("Positions must be positive");
		  
		  curr_green = -1; // Revisar si esto es asi
		  last_green = 0;
		  roads = new ArrayList<>();
		  dest_road = new HashMap<>();
		  queues = new ArrayList<>();
		}
	
	//* Añadir carreteras
	void addIncommingRoad(Road r) throws IllegalArgumentException{
		if (r.getDest().getId() == this.getId()) { // Revisamos que la carretera tenga como destino este cruce
			roads.add(r); // Se añade al final de las carreteras entrantes
			
			
			List<Vehicle> queue = new LinkedList<>(); 
			queues.add(queue);
		}
		else
			throw new IllegalArgumentException("The road to add needs to end at this Junction: " + this.getId());
	}
	
	void addOutGoingRoad(Road r) throws IllegalArgumentException{
		if (r.getSrc().getId() == this.getId()) { // Revisamos que la carretera tenga como origen este cruce
			if(dest_road.containsKey(r.getDest()))
				throw new IllegalArgumentException("OutGoingRoad ends in the same Junction as another road in this Junction: " + r.getDest());
			// Si no salta la excepcion
			dest_road.put(r.getDest(), r);
		}
		else
			throw new IllegalArgumentException("The road to add needs to start at this Junction: " + this.getId());
	}
	
	// Otras metodos
	void enter(Vehicle v) throws IllegalArgumentException{
		int i = 0;
		boolean esta = false;
		while (!esta && i < roads.size()) {
			if (roads.get(i).getId() == v.getRoad().getId()) {
				queues.get(i).add(v);
				esta = true;
			}
			i++;
		}
		if (!esta) throw new IllegalArgumentException("Vehicle does not have this junction as its destination");
	}
	
	Road roadTo(Junction j) throws IllegalArgumentException{
		for(Map.Entry<Junction, Road> entry: dest_road.entrySet()) {
			if (entry.getKey().getId() == j.getId())
				return entry.getValue();
		}
		return null;
	}
	
	@Override
	void advance(int time) {
		if (curr_green > -1) {
			List<Vehicle> v = new ArrayList<>();
			// Movemos los coches de la cola curr_green
			v = queue_strategy.dequeue(queues.get(curr_green));
			for (int i = 0; i < v.size(); i++) {
				v.get(i).moveToNextRoad();
				queues.get(curr_green).remove(i);
			}
		}
		// Cambiamos los semaforos
		int change = light_strategy.chooseNextGreen(roads, queues, curr_green, last_green, time);
		if (change != curr_green) {
			last_green = time;
			curr_green = change;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("green", (curr_green == -1)? "none" : roads.get(curr_green).getId() );
		JSONArray json_queues = new JSONArray();
		for(int i = 0; i < queues.size(); i++) {
			JSONObject json_roads = new JSONObject();
			json_roads.put("road", roads.get(i).getId());
			List<String> vehicle_ids = new ArrayList<>();
			for(int j = 0; j < queues.get(i).size(); j++) {
				vehicle_ids.add(queues.get(i).get(j).getId());
			}
			json_roads.put("vehicles", vehicle_ids);
			
			json_queues.put(json_roads);
		}
		json.put("queues", json_queues);
		return json;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getGreenLightIndex() {
		return curr_green;
	}

	public List<Road> getInRoads() {
		return roads;
	}

	public List<List<Vehicle>> getQueues() {
		return queues;
	}
	
}
