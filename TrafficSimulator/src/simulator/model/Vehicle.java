package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	
	/*
	 *
	 * Atributos
	 *
	 */
	
	// private
	private List<Junction> itinerary;
	private int cont_iter;
	private Road road;
	private int location;
	private int total_distance;
	
	private int max_speed;
	private int act_speed;
	private VehicleStatus state;
	
	private int grade_pollution;
	private int total_pollution;
	
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException {
		super(id);
		
		if (maxSpeed <= 0)
			throw new IllegalArgumentException("maxSpeed can´t be lower than 0");
		else 
			this.max_speed = maxSpeed;
		if (contClass < 0 || contClass > 10)
			throw new IllegalArgumentException("Contamination Pollution must be between 0 and 10 (both included)");
		else 
			this.grade_pollution = contClass;
		if (itinerary.size() < 2)
			throw new IllegalArgumentException("There must be at least 2 Junctions");
		else 
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		
		this.cont_iter = 0;
		this.location = 0;
		this.act_speed = 0;
		this.total_distance = 0;
		this.total_pollution = 0;
		this.state = VehicleStatus.PENDING;
	}
	
	void setSpeed(int s) throws IllegalArgumentException {
		if (s < 0)
			throw new IllegalArgumentException("the speed to be set needs to be positive");
		else
			if (state == VehicleStatus.TRAVELING)
				act_speed = (s > max_speed)? max_speed : s;
	}
	
	void setContClass(int c) throws IllegalArgumentException {
		if (c < 0 || c > 10)
			throw new IllegalArgumentException("the contamination grade to set needs to be between 0 and 10 (both included)");
		else 
			grade_pollution = c;
	}
	
	public int getLocation() {
		return location;
	}
	public int getSpeed() {
		return act_speed;
	}
	public int getMaxSpeed() {
		return max_speed;
	}
	public int getContClass() {
		return grade_pollution;
	}
	public VehicleStatus getStatus() {
		return state;
	}
	public int getTotalCO2() {
		return total_pollution;
	}
	public List<Junction> getItinerary(){
		return itinerary;
	}
	public Road getRoad() {
		return road;
	}
	public int getTotal_distance() {
		return total_distance;
	}

	// Movimiento y comportamiento de Vehicle	
	@Override
	void advance(int time) {
		if (state == VehicleStatus.TRAVELING) {
			int traveled = act_speed;
			if (location + act_speed <= road.getLength()) { // comprobar que no se salga de la longitud de la carretera
				location += act_speed;
				total_distance += act_speed;
			}else {
				traveled = road.getLength() - location;
				location = road.getLength();
				total_distance += traveled;
			}
			
			// Calculo de la pollution
			int c = traveled * grade_pollution;
			total_pollution += c;
			road.addContamination(c);
			
			// Si se acaba la carretera se une al siguiente cruce
			if (location >= road.getLength()) {
				road.getDest().enter(this);
				act_speed = 0;
				state = VehicleStatus.WAITING;
			}
		}
	}
	
	void moveToNextRoad() {
		if(cont_iter < itinerary.size()) {
			if (road != null)
				road.exit(this); // Salimos de la carretera
			if (state == VehicleStatus.PENDING) {
				road = itinerary.get(0).roadTo(itinerary.get(1)); // Buscar la siguiente carretera
				road.enter(this); // Entramos en la carretera
				cont_iter = 2;
			}else if(state == VehicleStatus.WAITING) {
				// se necesita un contador para evitar busquedas innecesarias
				road = road.getDest().roadTo(itinerary.get(cont_iter));
				location = 0;
				road.enter(this);
				cont_iter++;
			}else{
                throw new IllegalArgumentException("Vehicle state is neither PENDING nor WAITING");
            }
			state = VehicleStatus.TRAVELING;
		}else {
			state = VehicleStatus.ARRIVED;
			road.exit(this);
		}
	}

	// Metodos JSON
	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("id", getId());
		json.put("speed", act_speed);
		json.put("distance", total_distance);
		json.put("co2", total_pollution);
		json.put("class", grade_pollution);
		json.put("status", state.toString());
		if (state == VehicleStatus.TRAVELING || state == VehicleStatus.WAITING) {
			json.put("road", road.getId());
			json.put("location", location);
		}
		return json;
	}


}
