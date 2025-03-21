package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
	private List<Event>eventList;
	private String[]eventsTable= {"Time","Desc."};
	
	EventsTableModel(Controller ctrl){
		this._ctrl=ctrl;
		this.eventList=new ArrayList<>();
		this._ctrl.addObserver(this);
		
	}

	@Override
	public int getRowCount() {
		return eventList.size();
	}

	@Override
	public int getColumnCount() {
		return eventsTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Event e=eventList.get(rowIndex);
		if(columnIndex==0) {
			return e.getTime();
		}
		else {
			return e.toString();
		}
	}
	
	@Override
    public String getColumnName(int column) {
        return eventsTable[column];
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		this.eventList.clear();
		this.eventList.addAll(events);
		fireTableDataChanged();
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		this.eventList.add(e);
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		this.eventList.clear();
		fireTableDataChanged();
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		this.eventList.clear();
		this.eventList.addAll(events);
		fireTableDataChanged();
		
	}

}
