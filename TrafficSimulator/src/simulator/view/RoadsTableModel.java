package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]roadsTable= {"Id","Length","Weather","Max. Speed","Speed Limit","Total CO2","CO2 Limit"};
	private List<Road>roadList;
	
	RoadsTableModel(Controller ctrl){
		this._ctrl=ctrl;
		this.roadList=new ArrayList<>();
		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return roadList.size();
	}

	@Override
	public int getColumnCount() {
		return roadsTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Road r=this.roadList.get(rowIndex);
		if(columnIndex==0) {
			return r.getId();
		}
		else if(columnIndex==1) {
			return r.getLength();
		}
		else if(columnIndex==2) {
			return r.getWeather();
		}
		else if(columnIndex==3) {
			return r.getMaxSpeed();
		}
		else if(columnIndex==4) {
			return r.getSpeedLimit();
		}
		else if(columnIndex==5) {
			return r.getTotalCO2();
		}
		else if(columnIndex==6) {
			return r.getContLimit();
		}
		return null;
	}
	
	@Override
    public String getColumnName(int column) {
        return roadsTable[column];
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		this.roadList.clear();
		for(Road r:map.getRoads()) {
			this.roadList.add(r);
		}
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		this.roadList.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Road r:map.getRoads()) {
			this.roadList.add(r);
		}
		fireTableDataChanged();
		
	}

}
