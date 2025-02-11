package simulator.model;

public class CityRoad extends Road{
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id,srcJunc,destJunc,maxSpeed,contLimit,length,weather);
	}

	@Override
	void reduceTotalContamination() {
		int x=0;
		
		if(this.get_weather()==Weather.WINDY||this.get_weather()==Weather.STORM) {
			x=10;
		}
		else {
			x=2;
		}
		this.co2=Math.max(0, this.getCo2()-x);
	}

	@Override
	void updateSpeedLimit() {
		this.limitSpeed=this.get_maxSpeed();
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11-v.get_contClass())*this.getLimitSpeed())/11;
	}

}
