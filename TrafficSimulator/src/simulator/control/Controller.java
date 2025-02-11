package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator _trafficSimulator;
	private Factory<Event> _eventsFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim==null||eventsFactory==null) {
			throw new IllegalArgumentException("error in controller, sim or eventsFactory are null");
		}
		  this._trafficSimulator=sim;
		  this._eventsFactory=eventsFactory;
	}
	
	public void loadEvents(InputStream in) {
		if(in==null) {
			throw new IllegalArgumentException("input cannot be null");
		}
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray array=jo.getJSONArray("events");
		for(int i=0;i<array.length();i++) {
			Event e=this._eventsFactory.create_instance(array.getJSONObject(i));
			this._trafficSimulator.addEvent(e);
		}
	}
	
	public void run(int n, OutputStream out) {
		JSONArray array=new JSONArray();
		for(int i=0;i<n;i++) {
			this._trafficSimulator.advance();
			JSONObject states=this._trafficSimulator.report();
			array.put(states);
		}
		JSONObject result=new JSONObject();
		result.put("states", array);
		
		try (PrintStream printStream = new PrintStream(out)) {
		    printStream.println(result.toString(2)); 
		}
	}
	
	public void reset() {
		this._trafficSimulator.reset();
	}

}
