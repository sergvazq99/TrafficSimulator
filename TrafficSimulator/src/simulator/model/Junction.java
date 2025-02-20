package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{
	
	
	private List<Road> inRoads;
	private Map<Junction,Road> outRoads;
	private List<List<Vehicle>> queueList;
	private Map<Road,List<Vehicle>> roadQueue;
	private int inGreen;
	private int lastStep;
	private LightSwitchingStrategy _lsStrategy;
	private DequeuingStrategy _dqStrategy;
	private int _xCoor;
	private int _yCoor;
	

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		  super(id);
		  if(lsStrategy==null||dqStrategy==null||xCoor<0||yCoor<0) {
			  throw new IllegalArgumentException("Error arguments of Junction");
		  }
		  this._lsStrategy=lsStrategy;
		  this._dqStrategy=dqStrategy;
		  this._xCoor=xCoor;
		  this._yCoor=yCoor;
		  this.inRoads=new ArrayList<>();
		  this.outRoads=new HashMap<>();
		  this.queueList=new ArrayList<>();
		  this.roadQueue=new HashMap<>();
		  this.inGreen=-1;
		  this.lastStep=0;
	}
	
	void addIncommingRoad(Road r) {
		if(r.getDest()!=this) {
			throw new IllegalArgumentException("junction dest and actual junction must be the same");
		}
		this.inRoads.add(r);
		List<Vehicle> newQueue = new LinkedList<>();
		this.queueList.add(newQueue);
		this.roadQueue.put(r, newQueue);
	}
	
	void addOutGoingRoad(Road r) {
		if(r.getSrc()!=this) {
			throw new IllegalArgumentException("any other road goes from this to dest junction and 'r' is an exit road from actual junction ");
		}
		this.outRoads.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) {
		Road r=v.getRoad();
		List<Vehicle> queue = this.roadQueue.get(r);
		queue.add(v);
	}
	
	Road roadTo(Junction j) {
		return this.outRoads.get(j);
	}

	@Override
	void advance(int time) {
		
		if(this.inGreen!=-1) {
			List<Vehicle>queue=this.roadQueue.get(this.inRoads.get(inGreen)); //Consigue la cola q de la carretera con la luz verde.
			List<Vehicle>advance=this._dqStrategy.dequeue(queue);//lista de vehículos que deben avanzar
			for(Vehicle v: advance) {
				v.moveToNextRoad();
				
				queue.remove(v);
				
			}
		}
		
		int index=this._lsStrategy.chooseNextGreen(inRoads, queueList, inGreen, lastStep, time);
		
		if(index!=inGreen) {
			this.inGreen=index;
			this.lastStep=time;
		}
		
	}

	@Override
	public JSONObject report() {
		JSONObject json=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		json.put("id", _id);
		
		if(inGreen==-1) {
			json.put("green", "none");
		}
		else {
			json.put("green", this.inRoads.get(inGreen).getId());
		}
		
		for(Road r:this.inRoads) {
			JSONObject queueJson=new JSONObject();
			queueJson.put("road", r.getId());
			
			JSONArray jArray=new JSONArray();
			
			for(Vehicle v: this.roadQueue.get(r)) {
				jArray.put(v.getId());
			}
			
			queueJson.put("vehicles", jArray);
			jsonArray.put(queueJson);
		}
		
		json.put("queues", jsonArray);
		
		return json;
	}

}
