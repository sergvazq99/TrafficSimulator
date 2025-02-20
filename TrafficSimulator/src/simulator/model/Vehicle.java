package simulator.model;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject{
	
	private List<Junction> _itinerary;
	private int _maxSpeed;
	private int speed;
	private VehicleStatus state;
	private Road road;
	private int location;
	private int co2;
	private int _contClass;
	private int distance;
	private int cont;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		  super(id);
		  if(maxSpeed<=0||contClass<0||contClass>10||itinerary.size()<2) {
			  throw new IllegalArgumentException("Error arguments of Vehicle");
		  }
		  this.speed=0;
		  
		  this.state=VehicleStatus.PENDING;
		  
		  this._maxSpeed=maxSpeed;
		  this._contClass=contClass;
		  this._itinerary=Collections.unmodifiableList(new ArrayList<>(itinerary));;
		  this.location=0;
		  this.distance=0;
		  this.co2=0;
		  this.cont=0;
	}
	
	void setSpeed(int s) {
		if(s<0) {
			throw new IllegalArgumentException("s must be positive");
		}
		this.speed=Math.min(s, _maxSpeed);
	}
	
	void setContClass(int c) {
		if(c<0||c>10) {
			throw new IllegalArgumentException("c must be between 0 and 10");
		}
		this.co2=c;
	}

	@Override
	void advance(int time) {
		if(this.state==VehicleStatus.TRAVELING) {
			int original_location=this.location;
			this.location=this.location+this.speed;
			
			if(this.location>this.road.getLength()) {
				this.location=this.road.getLength();
			}
			
			int d=this.location-original_location;
			this.distance+=d;
			
			int c=d*this._contClass;
			this.co2+=c;
			road.addContamination(c);
			
			
			if(this.location>=this.road.getLength()) {
				this.road.getDest().enter(this);
				this.state=VehicleStatus.WAITING;
				this.speed=0;
				this.cont++;
			}
		}
		
	}
	
	void moveToNextRoad() { 
		if (this.state != VehicleStatus.PENDING && this.state != VehicleStatus.WAITING) {
	        throw new IllegalStateException("The vehicle must be in PENDING or WAITING state.");
	    }

	    // Si el vehículo no está en una carretera, lo asignamos a la primera carretera de su itinerario.
	    if (this.road == null) {
	        // Si el vehículo no está en una carretera, asignamos la primera carretera del itinerario
	        Junction currentJunction = this._itinerary.get(cont);
	        Road nextRoad = currentJunction.roadTo(this._itinerary.get(cont + 1));
	        this.road = nextRoad; // Asignamos la carretera

	        // Cambiar estado a TRAVELING y poner la ubicación en 0
	        this.state = VehicleStatus.TRAVELING;
	        this.location = 0;
	    } else {
	        // Si ya está en una carretera, llama a exit en la carretera actual y mueve al siguiente cruce
	        this.road.exit(this);
	        cont++; // Avanzamos al siguiente cruce en el itinerario

	        // Ahora que ha salido de la carretera actual, entra en la siguiente carretera
	        Junction nextJunction = this._itinerary.get(cont);
	        Road nextRoad = nextJunction.roadTo(this.road.getDest());
	        this.road = nextRoad; // Asignamos la siguiente carretera

	        // Cambiar estado a TRAVELING
	        this.state = VehicleStatus.TRAVELING;
	        this.location = 0; // Colocamos al vehículo al principio de la carretera
	    }
	}

	@Override
	public JSONObject report() {
		JSONObject json = new JSONObject();
	    
	    json.put("id", _id);
	    json.put("speed", speed);
	    json.put("distance", distance);
	    json.put("co2", co2);
	    json.put("class", _contClass);
	    json.put("status", state.toString());
	    if(state == VehicleStatus.TRAVELING || state == VehicleStatus.WAITING) {
		    json.put("road", road.getId());
		    json.put("location", location);
	    }
	    
	    return json;
	}

	public List<Junction> getItinerary() {
		return _itinerary;
	}

	public int getMaxSpeed() {
		return _maxSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public VehicleStatus getStatus() {
		return state;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public int getTotalCO2() {
		return co2;
	}

	public int getContClass() {
		return _contClass;
	}
	
	public int getCont() {
		return cont;
	}

}
