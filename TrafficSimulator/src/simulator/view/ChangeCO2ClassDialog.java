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
import simulator.model.RoadMap;
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
	
	ChangeCO2ClassDialog(Controller ctrl){
		this._ctrl=ctrl;
		this._vehiclesCombo=new DefaultComboBoxModel<>();
		this._co2Combo=new DefaultComboBoxModel<>();
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
        JComboBox<String>vehiclesCombo=new JComboBox<>(_vehiclesCombo);
        JComboBox<Integer>co2Combo=new JComboBox<>(_co2Combo);
        JSpinner ticksSpinner = new JSpinner();

        vehiclesCombo.setPreferredSize(new Dimension(100, 30));
        co2Combo.setPreferredSize(new Dimension(100, 30));
        ticksSpinner.setPreferredSize(new Dimension(100, 30));
        
        
        
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
        setVisible(true);
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this._co2Combo.addElement(v.getContClass());
		}
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		//this._vehiclesCombo.setSelectedItem(map.getVehicles());
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this._co2Combo.addElement(v.getContClass());
		}
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		for(Vehicle v:map.getVehicles()) {
			this._vehiclesCombo.addElement(v.getId());
			this._co2Combo.addElement(v.getContClass());
		}
		
	}

}
