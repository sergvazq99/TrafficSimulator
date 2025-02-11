package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	public NewVehicleEventBuilder() {
		super("new_vehicle_event_e", "New Vehicle Event");
	}

	@Override
	protected Event create_instance(JSONObject data) {
		int time=data.getInt("time");
		String id=data.getString("id");
		int maxspeed=data.getInt("maxspeed");
		int clas=data.getInt("class");
		
		JSONArray itinerary=data.getJSONArray("itinerary");
		List<String>it=new ArrayList<>();
		for(int i=0;i<itinerary.length();i++) {
			it.add(itinerary.getString(i));
		}
		return new NewVehicleEvent(time,id,maxspeed,clas,it);
	}

}
