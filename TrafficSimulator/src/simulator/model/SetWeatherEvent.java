package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event{
	
	private List<Pair<String,Weather>> _ws;

	public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		if(ws==null) {
			throw new IllegalArgumentException("ws is null");
		}
		this._ws = ws;
	}

	@Override
	void execute(RoadMap map) {
		
		for(Pair<String,Weather> pair: this._ws) {
			if(map.getRoad(pair.getFirst())==null) {
				throw new IllegalArgumentException("the road doesn't exists in the road map");
			}
			map.getRoad(pair.getFirst()).setWeather(pair.getSecond());
		}
		
	}
	
	

}
