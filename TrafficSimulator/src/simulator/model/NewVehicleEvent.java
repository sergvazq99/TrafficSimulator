package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event{
	
	private String _id;
	private int _maxSpeed;
	private int _contClass;
	private List<String> _itinerary;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this._id = id;
		this._maxSpeed = maxSpeed;
		this._contClass = contClass;
		this._itinerary = itinerary;
		//this._itinerary=new ArrayList<>(itinerary);
	}

	@Override
	void execute(RoadMap map) {
		List<Junction>junctions=new ArrayList<>();
		
		for(String id:this._itinerary) {
			junctions.add(map.getJunction(id));
		}
		Vehicle vehicle=new Vehicle(_id, _maxSpeed, _contClass, junctions);
		map.addVehicle(vehicle);
		vehicle.moveToNextRoad();
	}

}
