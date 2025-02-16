package simulator.model;

public class InterCityRoad extends Road{
	
	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather){
		super(id,srcJunc,destJunc,maxSpeed,contLimit,length,weather);
	}

	@Override
	void reduceTotalContamination() {
		int x=0;
		
		switch(this.getWeather()) {
			case SUNNY:
				x=2;
				break;
			case CLOUDY:
				x=3;
				break;
			case RAINY:
				x=10;
				break;
			case WINDY:
				x=15;
				break;
			case STORM:
				x=20;
				break;
		}
		
		int value=((100-x)*this.getTotalCO2())/100;
		this.addContamination(this.getTotalCO2()-value);
	}

	@Override
	void updateSpeedLimit() {
		if(this.getTotalCO2()>this.getContLimit()) {
			this.limitSpeed=this.getMaxSpeed()/2;
		}
		else {
			this.limitSpeed=this.getMaxSpeed();
		}
		
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		int speed=this.getSpeedLimit();
		if(this.getWeather()==Weather.STORM) {
			speed=(speed*8)/10;
		}
		return speed;
	}

}
