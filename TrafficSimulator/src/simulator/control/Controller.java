package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	
	private TrafficSimulator simulator;
	private Factory<Event> event_factory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim == null)
			throw new IllegalArgumentException("The TrafficSimulator is null");
		else
			simulator = sim;
		if(eventsFactory == null)
			throw new IllegalArgumentException("The event factory is null");
		else
			event_factory = eventsFactory;
	}
	
	public  void loadEvents(InputStream in) {
		JSONObject json = new JSONObject(new JSONTokener(in));
		
		if(json.has("events")) {
			JSONArray json_events = json.getJSONArray("events");
			for(int i = 0; i < json_events.length(); i++) {
				Event e = event_factory.create_instance(json_events.getJSONObject(i));
				simulator.addEvent(e);
			}
		}else
			throw new IllegalArgumentException("The Input given does not have the required format");
	}
	
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);
		p.println("{");
		p.println("  \"states\": [");
		for(int i = 0; i < n; i++) {
			simulator.advance();
			p.print(simulator.report());
			if (i < n-1)
				p.print(",\n");
		}
		p.println("]");
		p.println("}");
	}
	
	public void reset() {
		simulator.reset();
	}
	
	public void addObserver(TrafficSimObserver o) {
		simulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		simulator.addEvent(e);
	}
	
	public void run(int n) {
		for(int i=0;i<n;i++) {
			simulator.advance();
		}
	}
	
}
