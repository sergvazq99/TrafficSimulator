package simulator.view;

import java.awt.Color;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Weather;

public class MapByRoadComponent extends JComponent implements TrafficSimObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller _ctrl;
	
	
	
	
	private RoadMap _map;
	private Image _car;
	private Image _cloudy;
	private Image _rainy;
	private Image _storm;
	private Image _windy;
	private Image _sunny;
	private Image _contClass;
	
	MapByRoadComponent(Controller ctrl){
		this._ctrl=ctrl;
		this._ctrl.addObserver(this);
		this._car=loadImage("car.png");
		this._cloudy=loadImage("cloud.png");
		this._rainy=loadImage("rain.png");
		this._storm=loadImage("storm.png");
		this._windy=loadImage("wind.png");
		this._sunny=loadImage("sun.png");
		setPreferredSize(new Dimension (300, 200));
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g.setColor(Color.BLACK);
		
		drawRoads(graphics);
		drawVehicles(graphics);
	}
	
	private void drawRoads(Graphics graphics) {
		for(int i=0;i<_map.getRoads().size();i++) {
			int x1=50;
			int x2=getWidth()-100;
			int y=(i+1)*50;
			graphics.setColor(new Color(183, 96, 8));
			graphics.drawString(_map.getRoads().get(i).getSrc().getId(), x1-10, y-7);
			graphics.setColor(Color.BLUE);
			graphics.fillOval(x1-10, y-5, 10, 10);
			graphics.setColor(Color.BLACK);
			graphics.drawLine(x1, y, x2, y);
			graphics.setColor(setColor(_map.getRoads().get(i).getDest().getGreenLightIndex()));
			graphics.fillOval(x1+330, y-5, 10, 10);
			graphics.setColor(new Color(183, 96, 8));
			graphics.drawString(_map.getRoads().get(i).getDest().getId(), x2, y-7);
			graphics.drawImage(selectWeather(_map.getRoads().get(i).getWeather()), x2, y-15, 32, 32, this);
			graphics.drawImage(selectContClass(_map.getRoads().get(i).getTotalCO2(),_map.getRoads().get(i).getContLimit()), x2+35, y-15, 32, 32, this);
		}
		
	}
	
	private void drawVehicles(Graphics graphics) {
		for(int i=0;i<_map.getVehicles().size();i++) {
			int x1=50;
			int x2=getWidth()-100;
			int y=(i+1)*50;
			graphics.setColor(Color.GREEN);
			graphics.drawString(_map.getVehicles().get(i).getId(), x1 + (int) ((x2 - x1) * ((double) _map.getVehicles().get(i).getLocation() / (double) _map.getRoads().get(i).getLength())), y-5);
			graphics.drawImage(_car, x1 + (int) ((x2 - x1) * ((double) _map.getVehicles().get(i).getLocation() / (double) _map.getRoads().get(i).getLength())), y, 16, 16,this);
		}
	}
	
	private Image selectWeather(Weather weather) {
		if(weather==Weather.CLOUDY) {
			return _cloudy;
		}
		else if(weather==Weather.RAINY) {
			return _rainy;
		}
		else if(weather==Weather.STORM) {
			return _storm;
		}
		else if(weather==Weather.WINDY) {
			return _windy;
		}
		else if(weather==Weather.SUNNY) {
			return _sunny;
		}
		return null;
	}
	
	private Image selectContClass(int totalCO2,int contLimit) {
		this._contClass=loadImage("cont_"+calculateContClass(totalCO2,contLimit)+".png");
		return _contClass;
	}
	
	public int calculateContClass(int totalCO2,int contLimit) {
		return (int) Math.floor(Math.min((double) totalCO2/(1.0 + (double) contLimit),1.0) / 0.19);
	}
	
	private Color setColor(int colorGreen) {
		if(colorGreen==-1) {
			return Color.RED;
		}
		else {
			return Color.GREEN;
		}
	}
	
	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}
	
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		update(map);
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		update(map);
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		update(map);
		
	}

}
