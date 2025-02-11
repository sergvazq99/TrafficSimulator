package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	
	private int _timeSlot;
	
	public MostCrowdedStrategy(int timeSlot){
		this._timeSlot=timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		
		if(roads.isEmpty()) {
			return -1;
		}
		if(currGreen==-1) {
			int maxQueue=-1;
			int index=0;
			for(int i=0;i<qs.size();i++) {
				int queueSize=qs.get(i).size();
				if(queueSize>maxQueue) {
					maxQueue=queueSize;
					index=i;
				}
			}
			return index;
		}
		if(currTime-lastSwitchingTime < this._timeSlot) {
			return currGreen;
		}
		
		int maxQueue=-1;
		int index=0;
		for(int i=(currGreen+1)%(roads.size());i!=currGreen;i=(i+1)%roads.size()) {
			int queueSize=qs.get(i).size();
			if(queueSize>maxQueue) {
				maxQueue=queueSize;
				index=i;
			}
		}
		
		return index;
		
	}

}
