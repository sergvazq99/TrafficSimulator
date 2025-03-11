package simulator.view;

import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	StatusBar(Controller ctrl){
		this._ctrl=ctrl;
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JPanel mainPanel=new JPanel(new GridLayout(1,2));
		
		JLabel time=new JLabel("Time: ");
		JLabel event=new JLabel("Event added: ");
		
		mainPanel.add(time);
		mainPanel.add(event);
		
		this.add(mainPanel);
		
		this.setVisible(true);
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
