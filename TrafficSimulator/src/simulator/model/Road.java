package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject{
	
	protected Junction _srcJunc;
	protected Junction _destJunc;
	protected int _length;
	protected int _maxSpeed;
	protected int limitSpeed;
	protected int _contLimit;
	protected Weather _weather;
	protected int co2;
	protected List<Vehicle> vehicles;

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		  super(id);
		  if(maxSpeed<0||contLimit<0||length<0||srcJunc==null||destJunc==null||weather==null) {
			  throw new IllegalArgumentException("Error arguments of Road");
		  }
		  this._srcJunc=srcJunc;
		  this._destJunc=destJunc;
		  this._maxSpeed=maxSpeed;
		  this._contLimit=contLimit;
		  this._length=length;
		  this._weather=weather;
		  this.limitSpeed=0;
		  this.co2=0;
		  this.vehicles=new ArrayList<>();
	}
	
	void enter(Vehicle v) {
		if(v.getSpeed()!=0||v.getLocation()!=0) {
			 throw new IllegalArgumentException("speed and location of vehicle must be 0");
		}
		this.vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		this.vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		if(w==null) {
			throw new IllegalArgumentException("weather cannot be null");
		}
		this._weather=w;
	}
	
	void addContamination(int c) {
		if(c==0) {
			throw new IllegalArgumentException("c cannot be 0");
		}
		this.co2+=c;
	}
	
	abstract void reduceTotalContamination();
	
	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	

	@Override
	void advance(int time) {
		//a
		this.reduceTotalContamination();
		
		//b
		this.updateSpeedLimit();
		
		//c
		for(Vehicle v: vehicles) {
			
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
			
		}

		Collections.sort(this.vehicles, Comparator.comparingInt(Vehicle->Vehicle.getLocation())); //ordena la lista de vehículos por su localización 
		
	}

	@Override
	public JSONObject report() {
		JSONObject json=new JSONObject();
		JSONArray jArray=new JSONArray();
		
		for(Vehicle v:vehicles) {
			jArray.put(v.getId());
		}
		
		json.put("id", _id);
		json.put("speedlimit", limitSpeed);
		json.put("weather", _weather);
		json.put("co2", co2);
		json.put("vehicles", jArray);
		
		return null;
	}

	public Junction get_srcJunc() {
		return _srcJunc;
	}

	public Junction get_destJunc() {
		return _destJunc;
	}

	public int get_length() {
		return _length;
	}

	public int get_maxSpeed() {
		return _maxSpeed;
	}

	public int getLimitSpeed() {
		return limitSpeed;
	}

	public int get_contLimit() {
		return _contLimit;
	}

	public Weather get_weather() {
		return _weather;
	}

	public int getCo2() {
		return co2;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(vehicles);
	}
	
	

}
