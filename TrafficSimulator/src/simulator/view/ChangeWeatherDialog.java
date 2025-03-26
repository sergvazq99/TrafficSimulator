package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;


public class ChangeWeatherDialog extends JDialog implements TrafficSimObserver{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private DefaultComboBoxModel<String> _roadCombo;
	private DefaultComboBoxModel<String> _weatherCombo;

	ChangeWeatherDialog(Controller ctrl){
		this._ctrl=ctrl;
		this._roadCombo=new DefaultComboBoxModel<>();
		this._weatherCombo=new DefaultComboBoxModel<>();
		this._ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		this.setTitle("Change Road Weather");
		setPreferredSize(new Dimension(500, 250));
        setLayout(new BorderLayout());
		
		JPanel mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		JLabel text=new JLabel("<html><h5>Schedule an event to change the weather of a road after a given number of simulation ticks from now</h5></html>");
		mainPanel.add(text);
		this.add(mainPanel,BorderLayout.NORTH);
		
		JPanel spinnersPanel=new JPanel();
		spinnersPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel roadLabel=new JLabel("Road: ");
		JLabel weatherLabel=new JLabel("Weather: ");
		JLabel ticksLabel=new JLabel("Ticks: ");
		
		JComboBox<String>roadCombo=new JComboBox<>(_roadCombo);
		JComboBox<String>weatherCombo=new JComboBox<>(_weatherCombo);
		JSpinner ticksSpinner=new JSpinner();
		
		roadCombo.setPreferredSize(new Dimension(100,30));
		weatherCombo.setPreferredSize(new Dimension(100,30));
		ticksSpinner.setPreferredSize(new Dimension(100,30));
		
		
		ticksSpinner.setValue(1);
		
		spinnersPanel.add(roadLabel);
		spinnersPanel.add(roadCombo);
		spinnersPanel.add(weatherLabel);
		spinnersPanel.add(weatherCombo);
		spinnersPanel.add(ticksLabel);
		spinnersPanel.add(ticksSpinner);
		this.add(spinnersPanel,BorderLayout.CENTER);
		
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JButton cancel=new JButton("Cancel");
		JButton ok=new JButton("OK");
		
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
        	
        });
		
		buttonsPanel.add(cancel);
		buttonsPanel.add(ok);
		this.add(buttonsPanel,BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null); 
		this.setVisible(true);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
			this._weatherCombo.addElement(r.getWeather().toString());
		}
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
			this._weatherCombo.addElement(r.getWeather().toString());
		}
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
			this._weatherCombo.addElement(r.getWeather().toString());
		}
		
	}

}
