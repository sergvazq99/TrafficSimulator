package simulator.model;

public abstract class NewRoadEvent extends Event{
	
	protected String _id;
	protected String _srcJun;
	protected String _destJunc;
	protected int _length;
	protected int _co2Limit;
	protected int _maxSpeed;
	protected Weather _weather;
	
	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit,
			int maxSpeed, Weather weather) {
		super(time);
		this._id = id;
		this._srcJun = srcJun;
		this._destJunc = destJunc;
		this._length = length;
		this._co2Limit = co2Limit;
		this._maxSpeed = maxSpeed;
		this._weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		Junction src=map.getJunction(_srcJun);
		Junction dest=map.getJunction(_destJunc);
		
		Road road=create(_id, src, dest, _length, _co2Limit, _maxSpeed, _weather);
		map.addRoad(road);
	}
	
	abstract Road create(String id, Junction srcJun, Junction destJunc, int length, int co2Limit, int maxSpeed, Weather weather);
			

}
