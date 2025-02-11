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
	

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		  super(id);
		  if(maxSpeed<0||contClass<0||contClass>10||itinerary.size()<2) {
			  throw new IllegalArgumentException("Error arguments of Vehicle");
		  }
		  
		  this._maxSpeed=maxSpeed;
		  this._contClass=contClass;
		  this._itinerary=Collections.unmodifiableList(new ArrayList<>(itinerary));
		  this.location=0;
		  this.distance=0;
		  this.co2=0;
	}
	
	void setSpeed(int s) {
		if(s<0) {
			throw new IllegalArgumentException("s must be positive");
		}
		this.speed=Math.min(s, _maxSpeed);
	}
	
	void setContaminationClass(int c) {
		if(c<0||c>10) {
			throw new IllegalArgumentException("c must be between 0 and 10");
		}
		this.co2=c;
	}

	@Override
	void advance(int time) {
		if(this.state==VehicleStatus.TRAVELING) {
			int original_location=this.location;
			
			//a
			this.location=Math.min(this.location+this.speed, this.road.get_length());
			
			//b
			int c=(this.location-original_location)*this._contClass;
			this.co2+=c;
			road.addContamination(c);
			
			//c
			if(this.location>=this.road.get_length()) {
				this.road.get_destJunc().enter(this);
				this.state=VehicleStatus.WAITING;
			}
		}
		
	}
	
	void moveToNextRoad() { //revisar
		if(this.state!=VehicleStatus.PENDING&&this.state!=VehicleStatus.WAITING) {
			throw new IllegalArgumentException("state must be PENDING or WAITING");
		}
		road.exit(this);
		
		int index=this._itinerary.indexOf(road.get_destJunc());
		Junction actualJunction=this._itinerary.get(index);
		
		Road nextRoad=actualJunction.roadTo(this._itinerary.get(index+1));
		road=nextRoad;
		road.enter(this);
		location=0;
		speed=0;
		state=VehicleStatus.TRAVELING;
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
	    
	    return json;
	}

	public List<Junction> get_itinerary() {
		return _itinerary;
	}

	public int get_maxSpeed() {
		return _maxSpeed;
	}

	public int getSpeed() {
		return speed;
	}

	public VehicleStatus getState() {
		return state;
	}

	public Road getRoad() {
		return road;
	}

	public int getLocation() {
		return location;
	}

	public int getCo2() {
		return co2;
	}

	public int get_contClass() {
		return _contClass;
	}
	
	

}
