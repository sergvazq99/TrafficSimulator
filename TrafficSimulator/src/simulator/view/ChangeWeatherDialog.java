package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.SetWeatherEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;


public class ChangeWeatherDialog extends JDialog implements TrafficSimObserver{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private DefaultComboBoxModel<String> _roadCombo;
	private DefaultComboBoxModel<Weather> _weatherCombo;
	private JComboBox<String>roadCombo;
	private JComboBox<Weather>weatherCombo;
	private JSpinner ticksSpinner;
	private Map<String,Weather>roadWeatherMap;
	
	private int time = 0;

	ChangeWeatherDialog(Controller ctrl, Frame f){
		super(f, true);
		this._ctrl=ctrl;
		this._roadCombo=new DefaultComboBoxModel<>();
		this._weatherCombo=new DefaultComboBoxModel<>();
		this.roadCombo=new JComboBox<>(_roadCombo);
		this.weatherCombo=new JComboBox<>(_weatherCombo);
		this.ticksSpinner=new JSpinner();
		this.roadWeatherMap=new HashMap<>();
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
		
		roadCombo.setPreferredSize(new Dimension(100,30));
		weatherCombo.setPreferredSize(new Dimension(100,30));
		ticksSpinner.setPreferredSize(new Dimension(100,30));
		
		this.roadCombo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedRoad=(String)roadCombo.getSelectedItem();
				if(selectedRoad!=null&&roadWeatherMap.containsKey(selectedRoad)) {
					Weather weather=roadWeatherMap.get(selectedRoad);
					roadCombo.setSelectedItem(weather);
				}
				
			}
			
		});
		
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
		
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
        	
        });
		
		buttonsPanel.add(cancel);
		buttonsPanel.add(ok);
		this.add(buttonsPanel,BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null); 
		this.setVisible(false);
	}
	
	public void open(Component f) {
		this.setLocationRelativeTo(f);
		setVisible(true);
	}
	
	private void okAction() {
		String id=(String)this.roadCombo.getSelectedItem();
		Weather weather=(Weather) this.weatherCombo.getSelectedItem();
		int ticks=(int)this.ticksSpinner.getValue();
		
		if(id==null||weather==null) {
			JOptionPane.showMessageDialog(this, "Seleccione una carretera y un tiempo", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			List<Pair<String,Weather>> changes=List.of(new Pair<>(id,weather));
			this._ctrl.addEvent(new SetWeatherEvent(time + ticks, changes));
			dispose();
		}
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
		}
		for(Weather w:Weather.values()) {
			this._weatherCombo.addElement(w);
		}
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
		}
		for(Weather w:Weather.values()) {
			this._weatherCombo.addElement(w);
		}
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Road r:map.getRoads()) {
			this._roadCombo.addElement(r.getId());
		}
		for(Weather w:Weather.values()) {
			this._weatherCombo.addElement(w);
		}
		this.time = time;
	}

}
