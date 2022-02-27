package me.miles.matthew.boids.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings({"serial", "unused"})
public class Runner extends JFrame implements ActionListener{
	private Timer timer;
	private long lastFrameTime;
	private BoidPlane b;
	private ControlPanel controlPanel;
	private JPanel mainWindow;
	
	public static void main(String[] args) {
		new Runner();
	}
	
	public Runner() {		
		int w = Constants.getWindowWidth();
		int h = Constants.getWindowHeight();
		b = new BoidPlane(w, h);
		controlPanel = new ControlPanel(this);
		
		mainWindow = new JPanel(new BorderLayout());
		mainWindow.add(b, BorderLayout.CENTER);
		mainWindow.add(controlPanel, BorderLayout.EAST);
		
		this.setTitle("Flocking Simulation");
		this.setContentPane(mainWindow);
		this.setSize(w, h);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		URL iconURL = getClass().getResource("BoidLogo.png");
		ImageIcon icon = new ImageIcon(iconURL);
		setIconImage(icon.getImage());
		
		timer = new Timer(17, this);
		timer.start();
		
		lastFrameTime = (new Date()).getTime();
		
		System.out.println(Utils.getAverageHueColor(new Color(255, 0, 255), new Color(0, 255, 0)));
	}

	public void updateBorders() {
		Constants.setWindowHeight(this.getHeight());
		Constants.setWindowWidth(this.getWidth()-200); // 200d is due to sidebar
	}
	
	public void startTimer() {
		timer.start();
		lastFrameTime = (new Date()).getTime();
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	@Override
	public void actionPerformed(ActionEvent a) {
		if (a.getSource() == timer) {
			long betweenTime = (new Date()).getTime()-lastFrameTime;
			
			b.update((betweenTime/1000d)*Constants.simulationSpeed); // seconds
			lastFrameTime = (new Date()).getTime();
			
			this.updateBorders();
		}
	}
}
