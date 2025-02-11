package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{

	public MostCrowdedStrategyBuilder() {
		super("most_crowded_lss", "Most Crowded");
	}

	@Override
	protected LightSwitchingStrategy create_instance(JSONObject data) {
		int timeSlot=1;
		if(data.has("timeslot")) {
			timeSlot=data.getInt("timeslot");
		}
		return new MostCrowdedStrategy(timeSlot);
	}

}
