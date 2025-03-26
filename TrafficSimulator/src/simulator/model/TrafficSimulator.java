package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	private RoadMap map;
	private Queue<Event> event_queue;
	private int time;
	private List<TrafficSimObserver> observer;
	
	public TrafficSimulator() {
		map = new RoadMap();
		event_queue = new PriorityQueue<>();
		time = 0;
		observer=new ArrayList<>();
	}

	public void addEvent(Event e) {
		event_queue.add(e);
		for(TrafficSimObserver o: observer) {
			o.onEventAdded(map, event_queue, e, time);
		}
		
	}
	
	public void advance() {
		this.time++;
		
		for(int i = event_queue.size()-1; i >= 0 ; i--) {
			Event e = event_queue.poll();
			if (time == e.time) {
				e.execute(map);
			}else
				event_queue.add(e);
		}
		
		for (Junction j: map.getJunctions()) {
			j.advance(time);
		}
		
		for (Road r: map.getRoads()) {
			r.advance(time);
		}
		
		for(TrafficSimObserver o: observer) {
			o.onAdvance(map, event_queue, time);
		}
		
	}
	
	public void reset() {
		map.reset();
		event_queue.clear();
		time = 0;
		for(TrafficSimObserver o: observer) {
			o.onReset(map, event_queue, time);
		}
		
	}

	public JSONObject report() {
		JSONObject json = new JSONObject();
		json.put("time", time);
		json.put("state", map.report());
		return json;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		this.observer.add(o);
		o.onRegister(map, event_queue, time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		this.observer.remove(o);
	}
	
	// 
}
