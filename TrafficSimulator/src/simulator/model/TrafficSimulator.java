package simulator.model;

import java.util.Iterator;

import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator {
	
	private RoadMap roadMap;
	private Queue<Event> eventsPriority;
	private int time;
	
	public TrafficSimulator() {
		this.roadMap=new RoadMap();
		this.eventsPriority=new PriorityQueue<Event>();
		this.time=0;
	}
	
	
	public void addEvent(Event e) {
		this.eventsPriority.add(e);
	}
	
	public void advance() {
		//i
		time++;
		
		//ii
		Iterator<Event>iterator=this.eventsPriority.iterator();
		while(iterator.hasNext()) {
			Event e=iterator.next();
			if(e.getTime()==time) {
				e.execute(roadMap);
				iterator.remove();
			}
		}
		
		//iii
		for(Junction junction:this.roadMap.getJunctions()) {
			junction.advance(time);
		}
		
		//iv
		for(Road road:this.roadMap.getRoads()) {
			road.advance(time);
		}
	}
	
	public void reset() {
		this.roadMap.reset();
		this.eventsPriority.clear();
		time=0;
	}
	
	public JSONObject report() {
		
		JSONObject json=new JSONObject();
		
		json.put("time", time);
		json.put("state", this.roadMap.report());
		
		return json;
	}

}
