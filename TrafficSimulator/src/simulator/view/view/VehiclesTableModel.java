package simulator.view;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]vehiclesTable= {"Id","Location","Itinerary","CO2 Class","Max. Speed","Speed","Total CO2","Distance"};
	private List<Vehicle>vehiclesList;
	
	VehiclesTableModel(Controller ctrl){
		this._ctrl=ctrl;
		this.vehiclesList=new ArrayList<>();
		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return vehiclesList.size();
	}

	@Override
	public int getColumnCount() {
		return vehiclesTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vehicle v=vehiclesList.get(rowIndex);
		if(columnIndex==0) {
			return v.getId();
		}
		else if(columnIndex==1) {
			return v.getRoad().getId()+":"+v.getLocation();
		}
		else if(columnIndex==2) {
			return v.getItinerary();
		}
		else if(columnIndex==3) {
			return v.getContClass();
		}
		else if(columnIndex==4) {
			return v.getMaxSpeed();
		}
		else if(columnIndex==5) {
			return v.getSpeed();
		}
		else if(columnIndex==6) {
			return v.getTotalCO2();
		}
		else if(columnIndex==7) {
			return v.getTotal_distance();
		}
		return null;
	}
	
	@Override
    public String getColumnName(int column) {
        return vehiclesTable[column];
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		this.vehiclesList.clear();
		for(Vehicle v: map.getVehicles()) {
			this.vehiclesList.add(v);
		}
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		this.vehiclesList.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Vehicle v: map.getVehicles()) {
			this.vehiclesList.add(v);
		}
		fireTableDataChanged();
		
	}

}
