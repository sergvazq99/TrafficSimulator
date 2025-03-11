package simulator.view;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	ControlPanel(Controller ctrl){
		this._ctrl=ctrl;
		initGUI();
	}
	
	private void initGUI() {
		this.setPreferredSize(new Dimension(500,100));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JToolBar bar=new JToolBar();
		JButton fileButton=new JButton();
		JButton co2Button=new JButton();
		JButton weatherButton=new JButton();
		JButton execButton=new JButton();
		JButton stopButton=new JButton();
		JButton exitButton=new JButton();
		
		JLabel labelSpinner=new JLabel("Ticks: ");
		JSpinner spinner=new JSpinner();
		spinner.setValue(10);
		spinner.setMaximumSize(new Dimension(100,40));
		labelSpinner.add(spinner);
		
		fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		co2Button.setIcon(new ImageIcon("resources/icons/co2class.png"));
		weatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		execButton.setIcon(new ImageIcon("resources/icons/run.png"));
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		
		//file button
		
		/*fileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser file=new JFileChooser();
				try {
					_ctrl.reset();
					_ctrl.loadEvents(new FileInputStream(file.getSelectedFile()));
				} catch (FileNotFoundException ex) {
					JOptionPane.showMessageDialog(this, "File not found!","Error",JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				
			}
			
		});*/
		
		//co2 button
		
		//weather button
		
		//run button
		
		//stop button
		
		/*mainPanel.add(fileButton);
		mainPanel.add(co2Button);
		mainPanel.add(weatherButton);
		mainPanel.add(execButton);
		mainPanel.add(stopButton);
		
		this.add(mainPanel);*/
		
		bar.add(fileButton);
		bar.addSeparator();
		bar.add(co2Button);
		bar.add(weatherButton);
		bar.addSeparator();
		bar.add(execButton);
		bar.add(stopButton);
		bar.add(labelSpinner);
		bar.add(spinner);
		bar.addSeparator();
		bar.add(exitButton);
		this.add(bar);
		
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
