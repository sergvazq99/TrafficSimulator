package simulator.model;

public class NewInterCityRoadEvent extends NewRoadEvent {
	
	public NewInterCityRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed, Weather weather) {
	  super(time, id, srcJun, destJunc, length, co2Limit, maxSpeed, weather);
	}
	
	@Override
	void execute(RoadMap map) {
		Junction srcJunc = map.getJunction(origin_junc);
		Junction destJunc = map.getJunction(dest_junc);
		Road r = new InterCityRoad(id, srcJunc, destJunc, max_speed, max_pollution, length, weather);
		map.addRoad(r);
	}
	
	@Override
	public String toString() {
		return "New Inter City Road Event '"+id+"'";
	}

}
