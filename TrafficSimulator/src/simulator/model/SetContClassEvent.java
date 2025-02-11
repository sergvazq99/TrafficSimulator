package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event{
	
	private List<Pair<String,Integer>> _cs;
	
	public SetContClassEvent(int time, List<Pair<String,Integer>> cs)  {
		  super(time);
		  if(cs==null) {
			  throw new IllegalArgumentException("cs is null");
		  }
		  this._cs=cs;
	}



	@Override
	void execute(RoadMap map) {
		for(Pair<String,Integer>pair:this._cs) {
			if(map.getVehicle(pair.getFirst())==null) {
				throw new IllegalArgumentException("the vehicle doesn't exists in the road map");
			}
			map.getVehicle(pair.getFirst()).setContaminationClass(pair.getSecond());
		}
		
	}}
