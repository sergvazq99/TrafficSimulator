package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.SetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{

	public SetContClassEventBuilder() {
		super("set_cont_class_event_e", "Set Cont Class Event");
	}

	@Override
	protected Event create_instance(JSONObject data) {
		int time=data.getInt("time");
		JSONArray info=data.getJSONArray("info");
		List<Pair<String,Integer>>pair=new ArrayList<>();
		
		for(int i=0;i<info.length();i++) {
			String vehicle=info.getJSONObject(i).getString("vehicle");
			Integer clas=info.getJSONObject(i).getInt("class");
			pair.add(new Pair<>(vehicle,clas));
		}
		
		return new SetContClassEvent (time,pair);
	}

}
