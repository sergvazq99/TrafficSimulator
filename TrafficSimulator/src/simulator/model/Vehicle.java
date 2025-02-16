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
		  if(maxSpeed<0||contClass<0||contClass>10||itinerary.size()<2) {
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
		if(this.state!=VehicleStatus.PENDING&&this.state!=VehicleStatus.WAITING) {
			throw new IllegalArgumentException("state must be PENDING or WAITING");
		}
		
		if(road!=null||cont>0) {
			road.exit(this);
		}
		
		if(cont==this._itinerary.size()-1) {
			this.state=VehicleStatus.ARRIVED;
			this.road=null;
			this.speed=0;
			this.location=0;
		}
		else {
			Junction actual=this._itinerary.get(cont);
			Junction next=this._itinerary.get(cont+1);
			Road nextRoad=actual.roadTo(next);
			road=nextRoad;
			road.enter(this);
			cont++;
			this.state=VehicleStatus.TRAVELING;
			this.location=0;
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
	    if(state==VehicleStatus.PENDING||state==VehicleStatus.PENDING)
	    json.put("road", road.getId());
	    json.put("location", location);
	    
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
