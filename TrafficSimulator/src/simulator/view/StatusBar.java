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
	private JLabel timeLabel=new JLabel("Time: ");
	private JLabel eventLabel=new JLabel("Welcome!");
	
	StatusBar(Controller ctrl){
		this._ctrl=ctrl;
		this._ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		JPanel mainPanel=new JPanel(new GridLayout(1,2));
		
		mainPanel.add(timeLabel);
		mainPanel.add(eventLabel);
		
		this.add(mainPanel);
		this.setVisible(true);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		this.timeLabel.setText("Time: "+String.valueOf(time));
		this.eventLabel.setText(null);
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		this.timeLabel.setText("Time: "+String.valueOf(time));
		this.eventLabel.setText("Event added "+e);
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		this.timeLabel.setText("Time: "+String.valueOf(0));
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		this.timeLabel.setText("Time: "+String.valueOf(time));
		
	}
	
	

}
