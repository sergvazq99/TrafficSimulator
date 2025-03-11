package simulator.view;

import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]roadsTable= {"Id","Length","Weather","Max. Speed","Speed Limit","Total CO2","CO2 Limit"};
	
	RoadsTableModel(Controller ctrl){
		this._ctrl=ctrl;
	}

	@Override
	public int getRowCount() {
		return 0;
	}

	@Override
	public int getColumnCount() {
		return roadsTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return roadsTable[columnIndex];
	}
	
	@Override
    public String getColumnName(int column) {
        return roadsTable[column];
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
