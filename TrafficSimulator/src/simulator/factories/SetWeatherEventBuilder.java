package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{

	public SetWeatherEventBuilder() {
		super("set_weather", "Set Weather Event");
	}

	@Override
	protected Event create_instance(JSONObject data) {
		int time=data.getInt("time");
		JSONArray info=data.getJSONArray("info");
		List<Pair<String,Weather>>pair=new ArrayList<>();
		
		for(int i=0;i<info.length();i++) {
			
			String road=info.getJSONObject(i).getString("road");
			Weather weather=Weather.valueOf(info.getJSONObject(i).getString("weather").toUpperCase());
			pair.add(new Pair<>(road,weather));
			
		}
		return new SetWeatherEvent (time,pair);
	}

}
