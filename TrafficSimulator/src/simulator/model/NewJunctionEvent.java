package simulator.model;

public class NewJunctionEvent extends Event{
	
	private String _id;
	private LightSwitchingStrategy _lsStrategy;
	private DequeuingStrategy _dqStrategy;
	private int _xCoor;
	private int _yCoor;

	public NewJunctionEvent(int time, String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		  super(time);
		  this._id=id;
		  this._lsStrategy=lsStrategy;
		  this._dqStrategy=dqStrategy;
		  this._xCoor=xCoor;
		  this._yCoor=yCoor;
	}

	@Override
	void execute(RoadMap map) {
		Junction junction=new Junction(_id,_lsStrategy,_dqStrategy,_xCoor,_yCoor);
		map.addJunction(junction);
	}

}
