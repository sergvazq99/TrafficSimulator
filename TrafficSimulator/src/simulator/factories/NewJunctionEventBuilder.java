package simulator.factories;


import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	private Factory<LightSwitchingStrategy> _lssFactory;
	private Factory<DequeuingStrategy> _dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction", "New Junction Event");
		this._lssFactory=lssFactory;
		this._dqsFactory=dqsFactory;
	}


	@Override
	protected Event create_instance(JSONObject data) {
		int time=data.getInt("time");
		String id=data.getString("id");
		JSONArray coor=data.getJSONArray("coor");
		int x=coor.getInt(0);
		int y=coor.getInt(1);
		LightSwitchingStrategy ls_strategy=this._lssFactory.create_instance(data.getJSONObject("ls_strategy"));
		DequeuingStrategy dq_strategy=this._dqsFactory.create_instance(data.getJSONObject("dq_strategy"));
		return new NewJunctionEvent(time,id,ls_strategy,dq_strategy,x,y);
	}

}
