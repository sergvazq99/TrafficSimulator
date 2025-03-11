package simulator.view;

import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]vehiclesTable= {"Id","Location","Itinerary","CO2 Class","Max. Speed","Speed","Total CO2","Distance"};
	
	VehiclesTableModel(Controller ctrl){
		this._ctrl=ctrl;
		initGUI();
	}

	private void initGUI() {
		
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public int getColumnCount() {
		return vehiclesTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return vehiclesTable[columnIndex];
	}
	
	@Override
    public String getColumnName(int column) {
        return vehiclesTable[column];
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

}
