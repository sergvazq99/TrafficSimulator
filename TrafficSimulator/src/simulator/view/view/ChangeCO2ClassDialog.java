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
import simulator.model.RoadMap;
import simulator.model.SetContClassEvent;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog implements TrafficSimObserver{

	/**
	 * 
	 */	
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	private DefaultComboBoxModel<String>_vehiclesCombo;
	private DefaultComboBoxModel<Integer>_co2Combo;
	private JComboBox<String>vehiclesCombo;
	private JComboBox<Integer>co2Combo;
	private JSpinner ticksSpinner;
	private Map<String,Integer>vehicleCO2Map;
	
	private int time = 0;
	
	ChangeCO2ClassDialog(Controller ctrl, Frame f){
		super(f, true);
		this._ctrl=ctrl;
		this._vehiclesCombo=new DefaultComboBoxModel<>();
		this._co2Combo=new DefaultComboBoxModel<>();
		this.vehiclesCombo=new JComboBox<>(_vehiclesCombo);
        this.co2Combo=new JComboBox<>(_co2Combo);
        this.ticksSpinner=new JSpinner();
        this.vehicleCO2Map=new HashMap<>();
		this._ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		this.setTitle("Change CO2 Class");
		
		setPreferredSize(new Dimension(500, 250));
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JLabel text = new JLabel("<html><h5>Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now</h5></html>");
        mainPanel.add(text);
        this.add(mainPanel, BorderLayout.NORTH);

        JPanel spinnersPanel = new JPanel();
        spinnersPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); 
        JLabel vehicleLabel=new JLabel("Vehicle: ");
        JLabel classLabel=new JLabel("CO2 Class: ");
        JLabel ticksLabel=new JLabel("Ticks: ");

        vehiclesCombo.setPreferredSize(new Dimension(100, 30));
        co2Combo.setPreferredSize(new Dimension(100, 30));
        ticksSpinner.setPreferredSize(new Dimension(100, 30));
        
        
        this.vehiclesCombo.addActionListener(new ActionListener() {
        	
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectVehicle=(String)vehiclesCombo.getSelectedItem();
				if(selectVehicle!=null&&vehicleCO2Map.containsKey(selectVehicle)) {
					int co2=vehicleCO2Map.get(selectVehicle);
					co2Combo.setSelectedItem(co2);
				}
				
			}
        	
        });
        
        ticksSpinner.setValue(1);
        
        spinnersPanel.add(vehicleLabel);
        spinnersPanel.add(vehiclesCombo);
        spinnersPanel.add(classLabel);
        spinnersPanel.add(co2Combo);
        spinnersPanel.add(ticksLabel);
        spinnersPanel.add(ticksSpinner);
        this.add(spinnersPanel, BorderLayout.CENTER);

       
        JPanel buttonsPanel = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        
        ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
        	
        });
        
        cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
        	
        });

        buttonsPanel.add(cancel);
        buttonsPanel.add(ok);
        this.add(buttonsPanel, BorderLayout.SOUTH); 

        pack(); 
        setLocationRelativeTo(null); 
        setVisible(false);
	}
	
	public void open(Component f) {
		this.setLocationRelativeTo(f);
		setVisible(true);
	}
	
	private void okAction() {
		String id=(String)vehiclesCombo.getSelectedItem();
		Integer classCO2=(Integer)co2Combo.getSelectedItem();
		int ticks=(int)ticksSpinner.getValue();
		
		if(id==null||classCO2==null) {
			JOptionPane.showMessageDialog(this, "Seleccione un vehículo y una clase de CO2.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		else {
			List<Pair<String, Integer>> changes = List.of(new Pair<>(id, classCO2));
			this._ctrl.addEvent(new SetContClassEvent(time + ticks, changes));
			dispose();
		}
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		_vehiclesCombo.removeAllElements();
	    vehicleCO2Map.clear();

	    for (Vehicle v : map.getVehicles()) {
	        _vehiclesCombo.addElement(v.getId());
	        vehicleCO2Map.put(v.getId(), v.getContClass());
	    }

	    this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		_vehiclesCombo.removeAllElements();
	    vehicleCO2Map.clear();
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this.vehicleCO2Map.put(v.getId(), v.getContClass());
		}
		_co2Combo.removeAllElements();
		for(int i=0;i<11;i++) {
			this._co2Combo.addElement(i);
		}
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		_vehiclesCombo.removeAllElements();
	    vehicleCO2Map.clear();
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this.vehicleCO2Map.put(v.getId(), v.getContClass());
		}
		_co2Combo.removeAllElements();
		for(int i=0;i<11;i++) {
			this._co2Combo.addElement(i);
		}
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		_vehiclesCombo.removeAllElements();
	    vehicleCO2Map.clear();
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this.vehicleCO2Map.put(v.getId(), v.getContClass());
		}
		 _co2Combo.removeAllElements();
		for(int i=0;i<11;i++) {
			this._co2Combo.addElement(i);
		}
		this.time = time;
	}

}
