package simulator.model;

public class CityRoad extends Road{
	
	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id,srcJunc,destJunc,maxSpeed,contLimit,length,weather);
	}

	@Override
	void reduceTotalContamination() {
		int x=0;
		
		if(this.getWeather()==Weather.WINDY||this.getWeather()==Weather.STORM) {
			x=10;
		}
		else {
			x=2;
		}
		this.co2=Math.max(0, this.getTotalCO2()-x);
	}

	@Override
	void updateSpeedLimit() {
		this.limitSpeed=this.getMaxSpeed();
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((11-v.getContClass())*this.getSpeedLimit())/11;
	}

}
