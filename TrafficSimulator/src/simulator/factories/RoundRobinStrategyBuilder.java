package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public RoundRobinStrategyBuilder() {
		super("round_robin_lss", "Round Robin");
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		int timeSlot=1;
		if(data.has("timeslot")) {
			timeSlot=data.getInt("timeslot");
		}
		return new RoundRobinStrategy(timeSlot);
	}

}
