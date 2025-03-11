package simulator.view;

import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]eventsTable= {"Time","Desc."};
	
	EventsTableModel(Controller ctrl){
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
		return eventsTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return eventsTable[columnIndex];
	}
	
	@Override
    public String getColumnName(int column) {
        return eventsTable[column];
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
