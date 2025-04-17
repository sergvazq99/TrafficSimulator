package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private String[]junctionsTable= {"Id","Green","Queues"};
	private List<Junction>junctionsList;
	
	JunctionsTableModel(Controller ctrl){
		this._ctrl=ctrl;
		this.junctionsList=new ArrayList<>();
		this._ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return junctionsList.size();
	}

	@Override
	public int getColumnCount() {
		return junctionsTable.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Junction j=this.junctionsList.get(rowIndex);
		if(columnIndex==0) {
			return j.getId();
		}
		else if(columnIndex==1) {
			if(j.getGreenLightIndex()==-1) {
				return "NONE";
			}
			else {
				return j.getInRoads().get(j.getGreenLightIndex()).getId();
			}
		}
		else if(columnIndex==2) {
			String queues="";
			for(int i=0;i<j.getInRoads().size();i++) {
				queues+=j.getInRoads().get(i).getId()+":[";
				for(int k=0;k<j.getQueues().get(i).size();k++) {
					queues+=j.getQueues().get(i).get(k).getId();
				}
				queues+="] ";
			}
			return queues;
		}
		return null;
	}
	
	@Override
    public String getColumnName(int column) {
        return junctionsTable[column];
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		this.junctionsList.clear();
		for(Junction j:map.getJunctions()) {
			this.junctionsList.add(j);
		}
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		this.junctionsList.clear();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Junction j:map.getJunctions()) {
			this.junctionsList.add(j);
		}
		fireTableDataChanged();
		
	}

}
