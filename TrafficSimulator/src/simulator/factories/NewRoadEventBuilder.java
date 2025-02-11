package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event>{

	public NewRoadEventBuilder() {
		super("new_road_event_e", "New Road Event");
	}

	@Override
	protected Event create_instance(JSONObject data) {
		int time=data.getInt("time");
		String id=data.getString("id");
		String src=data.getString("src");
		String dest=data.getString("dest");
		int length=data.getInt("length");
		int co2limit=data.getInt("co2limit");
		int maxspeed=data.getInt("maxspeed");
		Weather weather=Weather.valueOf(data.getString("weather"));
		return create(time, id, src, dest, length, co2limit, maxspeed, weather);
	}
	
	abstract Event create(int time,String id,String src,String dest,int length,int co2limit,int maxspeed,Weather weather);

}
