package simulator.view;






import java.awt.Dimension;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

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
	private JToolBar bar;
	private boolean _stopped;
	private JButton fileButton;
	private JButton co2Button;
	private JButton weatherButton;
	private JButton execButton;
	private JButton stopButton;
	private JButton exitButton;
	
	private ChangeCO2ClassDialog co2Dialog;
	private ChangeWeatherDialog weatherDialog;
	
	ControlPanel(Controller ctrl){
		this._ctrl=ctrl;
		this.bar=new JToolBar();
		fileButton=new JButton();
		co2Button=new JButton();
		weatherButton=new JButton();
		execButton=new JButton();
		stopButton=new JButton();
		exitButton=new JButton();
		this._ctrl.addObserver(this);
		initGUI();
	}
	
	private void initGUI() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JLabel labelSpinner=new JLabel("Ticks: ");
		JSpinner spinner=new JSpinner();
		spinner.setValue(10);
		spinner.setMaximumSize(new Dimension(2000, 50));
		labelSpinner.add(spinner);
		
		fileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		fileButton.setToolTipText("Choose a file");
		co2Button.setIcon(new ImageIcon("resources/icons/co2class.png"));
		co2Button.setToolTipText("Change CO2 Class of a Vehicle");
		weatherButton.setIcon(new ImageIcon("resources/icons/weather.png"));
		weatherButton.setToolTipText("Change Weather of a Road");
		execButton.setIcon(new ImageIcon("resources/icons/run.png"));
		execButton.setToolTipText("Run the simulator");
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.setToolTipText("Stop the simulation");
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.setToolTipText("Exit");
		
		//file button
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser file=new JFileChooser("./resources/examples");
				if(file.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
					try {
						_ctrl.reset();
						_ctrl.loadEvents(new FileInputStream(file.getSelectedFile()));
					} catch (FileNotFoundException ex) {
						JOptionPane.showInputDialog(this);
						ex.printStackTrace();
					}
				}
			}
			
		});
		
		//co2 button
		co2Dialog=new ChangeCO2ClassDialog(_ctrl, ViewUtils.getWindow(this));
		co2Button.addActionListener(e -> {
				co2Dialog.open(ViewUtils.getWindow(this));
		});
		
		//weather button
		weatherDialog = new ChangeWeatherDialog(_ctrl, ViewUtils.getWindow(this));
		weatherButton.addActionListener(e -> {
			weatherDialog.open(ViewUtils.getWindow(this));
		});
		
		//run button
		execButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				co2Button.setEnabled(false);
				execButton.setEnabled(false);
				exitButton.setEnabled(false);
				fileButton.setEnabled(false);
				stopButton.setEnabled(true);
				weatherButton.setEnabled(false);
				run_sim((int)(spinner.getValue()));
				
			}
			
		});
		
		//stop button
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped=true;
			}
		});
		
		//exit button
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice=JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
		            System.exit(0);
		        }
			}
		});
		
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
		bar.add(Box.createHorizontalGlue());
		bar.add(exitButton);
		this.add(bar);
		this.setVisible(true);
	}
	
	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
	         		SwingUtilities.invokeLater(() -> run_sim(n - 1));
			} catch (Exception e) {
				// TODO show error message
				_stopped = true;
				this.co2Button.setEnabled(true);
				this.execButton.setEnabled(true);
				this.exitButton.setEnabled(true);
				this.fileButton.setEnabled(true);
				this.stopButton.setEnabled(true);
				this.weatherButton.setEnabled(true);
			}
		} else {
			_stopped = true;
			this.co2Button.setEnabled(true);
			this.execButton.setEnabled(true);
			this.exitButton.setEnabled(true);
			this.fileButton.setEnabled(true);
			this.stopButton.setEnabled(true);
			this.weatherButton.setEnabled(true);
		}
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		
		
	}

}
