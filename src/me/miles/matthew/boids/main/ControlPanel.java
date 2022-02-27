package me.miles.matthew.boids.main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings({"serial", "unused"})
public class ControlPanel extends JPanel implements ActionListener, ChangeListener {
	private JButton pauseButton;
	private JButton centreButton;
	private JButton attractButton;
	
	private JButton viewingButton;
	private JButton tracerButton;
	
	private JButton herdButton;
	private JButton avoidButton;
	private JButton alignButton;
	
	private JButton highlightOneButton;
	private JButton varyButton;
	
	private JSlider viewRangeSlider;
	private JSlider viewAngleSlider;
	private JSlider simSpeedSlider;
	
	private Color resumeColor  = new Color(35, 206, 107);
	private Color offColor     = new Color(218, 62, 82);
	private Color centreColor  = new Color(34, 116, 165);
	private Color attractColor = new Color(255, 191, 0);
	private Color viewColor    = new Color(174, 173, 240);
	private Color traceColor   = new Color(188, 164, 176);
	private Color bgColor      = new Color(68, 69, 69);
	private Color herdColor    = new Color(0, 189, 157);
	private Color avoidColor   = new Color(0, 120, 120);
	private Color alignColor   = new Color(50, 180, 50);
	private Color varyColor	   = new Color(120, 120, 220);
	private Color h1bColor     = new Color(200, 200, 50);
	
	private Runner runner;
	
	public ControlPanel(Runner runner) {
		super();
		this.runner = runner;
		
		pauseButton = new JButton("Pause");  
		pauseButton.setBackground(offColor);
		pauseButton.addActionListener(this);
		
		centreButton = new JButton("Attract to Centre");
		centreButton.setBackground(Constants.attractToCentre ? centreColor : offColor);
		centreButton.addActionListener(this);
		
		attractButton = new JButton("Attract to Mouse");
		attractButton.setBackground(Constants.attractToMouse ? attractColor : offColor);
		attractButton.addActionListener(this);
		
		viewingButton = new JButton("Show range of sight");
		viewingButton.setBackground(Constants.distanceVisuals ? viewColor : offColor);
		viewingButton.addActionListener(this);
		
		tracerButton = new JButton("Show tracers");
		tracerButton.setBackground(Constants.magnetVisuals ? traceColor : offColor);
		tracerButton.addActionListener(this);
		
		avoidButton = new JButton("Collision avoidance");
		avoidButton.setBackground(Constants.enableSeparation ? avoidColor : offColor);
		avoidButton.addActionListener(this);
		
		herdButton = new JButton("Herd boids");
		herdButton.setBackground(Constants.enableCohesion ? herdColor : offColor);
		herdButton.addActionListener(this);
		
		alignButton = new JButton("Align boids");
		alignButton.setBackground(Constants.enableAlignment ? alignColor : offColor);
		alignButton.addActionListener(this);
		
		varyButton = new JButton("Variation");
		varyButton.setBackground(Constants.enableVariation ? varyColor : offColor);
		varyButton.addActionListener(this);
		
		highlightOneButton = new JButton("Highlight A single boid");
		highlightOneButton.setBackground(Constants.highlightOne ? h1bColor : offColor);
		highlightOneButton.addActionListener(this);
		
		viewAngleSlider = new JSlider(0, 100,
				(int) Math.round((Constants.boidViewAngle/Constants.viewAngleRange[1]*100)));
		viewAngleSlider.addChangeListener(this);
		viewAngleSlider.setBorder(BorderFactory.createTitledBorder("Boid view angle"));
		
		viewRangeSlider = new JSlider(0, 100,
				(int) Math.round((Constants.boidViewDistance/Constants.viewDistanceRange[1]*100)));
		viewRangeSlider.addChangeListener(this);
		viewRangeSlider.setBorder(BorderFactory.createTitledBorder("Boid view range"));
		
		simSpeedSlider = new JSlider(0, 100,
				(int) Math.round(Utils.logerp(0, 100, Constants.simulationSpeed/Constants.maxSimulationSpeed)));
		simSpeedSlider.addChangeListener(this); // USE LOGERP
		simSpeedSlider.setBorder(BorderFactory.createTitledBorder("Simultaion Speed"));
		
		this.setLayout(new GridLayout(13, 1));
		this.setBackground(bgColor);
		this.add(centreButton);
		this.add(pauseButton);
		this.add(attractButton);
		this.add(viewingButton);
		this.add(tracerButton);
		this.add(highlightOneButton);
		
		this.add(herdButton);
		this.add(avoidButton);
		this.add(alignButton);
		this.add(varyButton);
		this.add(viewAngleSlider);
		this.add(viewRangeSlider);
		this.add(simSpeedSlider);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == pauseButton) {
			if (pauseButton.getBackground().equals(offColor)) {
				runner.stopTimer();
				pauseButton.setText("Resume");
				pauseButton.setBackground(resumeColor);
			} else {
				runner.startTimer();
				pauseButton.setText("Pause");
				pauseButton.setBackground(offColor);
			}
		} else if (ae.getSource() == centreButton) {
			if (centreButton.getBackground().equals(offColor)) {
				Constants.attractToCentre = true;
				centreButton.setBackground(centreColor);
			} else {
				Constants.attractToCentre = false;
				centreButton.setBackground(offColor);
			}
		} else if (ae.getSource() == attractButton) {
			if (attractButton.getBackground().equals(offColor)) {
				Constants.attractToMouse = true;
				attractButton.setBackground(attractColor);
			} else {
				Constants.attractToMouse = false;
				attractButton.setBackground(offColor);
			}
		} else if (ae.getSource() == tracerButton) {
			if (tracerButton.getBackground().equals(offColor)) {
				Constants.magnetVisuals = true;
				tracerButton.setBackground(traceColor);
			} else {
				Constants.magnetVisuals = false;
				tracerButton.setBackground(offColor);
			}
		} else if (ae.getSource() == viewingButton) {
			if (viewingButton.getBackground().equals(offColor)) {
				Constants.distanceVisuals = true;
				viewingButton.setBackground(viewColor);
			} else {
				Constants.distanceVisuals = false;
				viewingButton.setBackground(offColor);
			}
		} else if (ae.getSource() == herdButton) {
			if (herdButton.getBackground().equals(offColor)) {
				Constants.enableCohesion = true;
				herdButton.setBackground(herdColor);
			} else {
				Constants.enableCohesion = false;
				herdButton.setBackground(offColor);
			}
		} else if (ae.getSource() == avoidButton) {
			if (avoidButton.getBackground().equals(offColor)) {
				Constants.enableSeparation = true;
				avoidButton.setBackground(avoidColor);
			} else {
				Constants.enableSeparation = false;
				avoidButton.setBackground(offColor);
			}
		} else if (ae.getSource() == alignButton) {
			if (alignButton.getBackground().equals(offColor)) {
				Constants.enableAlignment = true;
				alignButton.setBackground(alignColor);
			} else {
				Constants.enableAlignment = false;
				alignButton.setBackground(offColor);
			}
		} else if (ae.getSource() == varyButton) {
			if (varyButton.getBackground().equals(offColor)) {
				Constants.enableVariation = true;
				varyButton.setBackground(varyColor);
			} else {
				Constants.enableVariation = false;
				varyButton.setBackground(offColor);
			}
		} else if (ae.getSource() == highlightOneButton) {
			if (highlightOneButton.getBackground().equals(offColor)) {
				Constants.highlightOne = true;
				highlightOneButton.setBackground(h1bColor);
			} else {
				Constants.highlightOne = false;
				highlightOneButton.setBackground(offColor);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent ae) {
		if (ae.getSource() == viewAngleSlider) {
			//DEBUG: S/ystem.out.println("Changing to "+viewAngleSlider.getValue());
			Constants.boidViewAngle = Utils.lerp(Constants.viewAngleRange[0],
					Constants.viewAngleRange[1], ((double) viewAngleSlider.getValue())/100d);
		} else if (ae.getSource() == viewRangeSlider) {
			//System.out.println(viewAng);
			Constants.boidViewDistance = (int) Math.round(Utils.lerp(Constants.viewDistanceRange[0],
					Constants.viewDistanceRange[1], ((double) viewRangeSlider.getValue())/100d));
		} else if (ae.getSource() == simSpeedSlider) {
			//S/ystem.out.println(Constants.simulationSpeed);
			Constants.simulationSpeed = (int) Math.round(Utils.lerp(0, Constants.maxSimulationSpeed,
					((double) simSpeedSlider.getValue())/100d)
					*(Math.log(simSpeedSlider.getValue()))/Math.log(100d));
		}
	}
}
