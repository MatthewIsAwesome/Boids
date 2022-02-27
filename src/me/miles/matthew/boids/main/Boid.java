package me.miles.matthew.boids.main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Boid {
	private double x;
	private double y;
	
	private double[] totalMove;
	
	private double goalAngle;
	
	private double angle;
	private double speed;
	private double angularVelocity;
	@SuppressWarnings("unused")
	private double previousTurn = 0d;
	private double vary = 0d;
	
	private Color color;

	// points to be attracted to, average taken for overall change
	private ArrayList<double[]> magnets = new ArrayList<double[]>();

	public Boid(int x, int y) {
		this.x = x;
		this.y = y;
		this.angle = (float) Constants.defaultAngle;
		this.speed = (float) Constants.minSpeed;
		this.goalAngle = this.angle;
		this.totalMove = new double[] {this.x, this.y};
	}
	
	// Getters and Setters
	public ArrayList<double[]> getMagnets() {
		return magnets;
	}

	public void setMagnets(ArrayList<double[]> magnets) {
		this.magnets = magnets;
	}
	
	public void addMagnet(double x, double y, double b) {
		addMagnet(new double[] {x, y, b});
	}
	
	public void addMagnet(double[] magnet) {
		this.magnets.add(magnet);
	}
	
	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity >
			Constants.maxTurningSpeed ? Constants.maxTurningSpeed : angularVelocity;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setRandomColor() {
		this.color = Color.getHSBColor((new Random()).nextFloat(), 1f, 1f);
	}
	
	public double[] getVelocity() {
		return new double[] {speed, angle};
	}
	
	public double getX() {
		return x;
	}

	public void setX(double d) {
		this.x = d;
	}

	public double getY() {
		return y;
	}

	public void setY(double d) {
		this.y = d;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double d) {
		this.angle = d;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	
	// Obstactle Avoidance System
	public void updateGoal() {	
		double totalX = 0;
		double totalY = 0;
		
		int s = magnets.size();
		//S/ystem.out.println("Number of magnets: "+s);
		//S/ystem.out.println("Magnets: "+Arrays.deepToString(magnets.toArray()));
		
		if (s == 0) {
			//S/ystem.out.println("No magnets, keeping speed and angle");
			goalAngle = this.angle;
			setTotalMove(new double[] {Math.cos(goalAngle)*10+x, Math.sin(goalAngle)*10+y});
			return;
		}
		
		//int magnitude = 0;
		
		for (int i = 0; i < s; i++) {
			double[] m = magnets.get(i);
			totalX += (m[0]-this.getX()); // *m[2] was in (bias)
			totalY += (m[1]-this.getY());
			//magnitude += m[2];
		}
		
		double xMove = totalX/s;
		double yMove = totalY/s;
		
		goalAngle = Math.atan(yMove/xMove);
		
		if (xMove < 0) { // If trying to go backwards, rotate PI rad (180deg)
			goalAngle += Math.PI;
		}
		
		setTotalMove(new double[] {xMove+this.getX(), yMove+this.getY()});
	}
	
	public double getGoal() {
		return goalAngle;
	}
	
	public void avoid(Boid b, double bias) {
		avoid((int) Math.round(b.getX()), (int) Math.round(b.getY()), bias);
	}
	
	public void avoid(int xPos, int yPos, double bias) { // create magnet in opposite direction to source
		double angle = Utils.angleTo(this.x, this.y, 0d, xPos, yPos) + Math.PI;
		this.seek((int) Math.round(this.getX()+Math.cos(angle)*50d),
				(int) Math.round(this.getY()+Math.sin(angle)*50d), bias);
	}
	
	public void seek(int xPos, int yPos, double bias) {
		this.addMagnet((new double[] {xPos, yPos, bias}));
		// create magnet towards object
	}
	
	public void align(double x, double y, double angle, double bias) {
		this.addMagnet(new double[] {Math.cos(angle)+x, Math.sin(angle)+y, bias});
	}
	
	public void align(Boid b, double bias) {
		this.addMagnet(new double[] {Math.cos(b.getAngle())*b.getSpeed()+b.getX(),
				Math.sin(b.getAngle())*b.getSpeed()+b.getY(), bias});
	}
	
	public void resetGoal() { // resets goal to 
		goalAngle = this.angle;
	}
	
	public void resetMagnets() {
		magnets = new ArrayList<double[]>();
	}
	
	public void alterCourse(double timePassed) {
		Random r = new Random();
		
		if (Constants.enableVariation) {
			vary += r.nextBoolean() ? Constants.varyAmount : -Constants.varyAmount;		
			if (vary > Constants.maxVary) 
				vary = Constants.maxVary;
			else if (vary < -Constants.maxVary)
				vary = -Constants.maxVary;
			this.goalAngle += vary;
		}
		
		double difference = (this.goalAngle - this.angle) % (2*Math.PI);
		
		if (difference > Math.PI) // make range of difference -pi<r<pi
			difference -= 2*Math.PI;
		
		// http://www.paulboxley.com/blog/2012/05/angles
		
		if (difference > Math.PI)
			difference -= 2*Math.PI;
		if (difference < -Math.PI)
			difference += 2*Math.PI;
		
		//NOTE: if maxTurnSpeed is replaced by maxAngularAcceleration, a smoother curve is acieved.					 (A lot of coding required but very smooth)
		double possibleTurnR = /*previousTurn*/+(Constants.maxTurningSpeed*timePassed);
		double possibleTurnL = /*previousTurn*/-(Constants.maxTurningSpeed*timePassed);
		
		// if desired turn > possibleDecelleration
		// 		desired turn *= -1
		
		
		
		// if (possibleTurnR > Constants.maxTurningSpeed)
		// 	possibleTurnR = Constants.maxTurningSpeed;
		// 
		// if (possibleTurnL < -Constants.maxTurningSpeed)
		// 	possibleTurnL = -Constants.maxTurningSpeed;
		
		// check if over maximum turn
		if (difference < possibleTurnR && difference > possibleTurnL) {
			previousTurn = 0d;
			this.angle = this.goalAngle;
		} else if (difference > 0) {
			previousTurn = possibleTurnR;
			this.angle += possibleTurnR; // turn right
		} else {
			previousTurn = possibleTurnL;
			this.angle += possibleTurnL; // turn left
		}
		
	}

	public double[] getTotalMove() {
		return totalMove;
	}

	public void setTotalMove(double[] totalMove) {
		this.totalMove = totalMove;
	}
	
	public boolean canSee(Boid c) {
		return canSee(c.getX(), c.getY());
	}
	
	public boolean canSee(double x1, double y1) {
		double distanceTo = Utils.distanceTo(this.x, this.y, x1, y1);
		double angleTo = Utils.angleTo(this.getX(), this.getY(), this.getAngle(),
				x1, y1) - this.getAngle();
		
		if (angleTo < (0))
			angleTo += 2*Math.PI;
		
		this.setAngle(this.getAngle() % (2*Math.PI));
		
		angleTo = angleTo % (2*Math.PI);
		
		boolean inDistance = distanceTo < Constants.boidViewDistance;
		boolean inAngle = angleTo < (Constants.boidViewAngle/2) ||
				angleTo > (2*Math.PI)-(Constants.boidViewAngle/2);
		return inDistance && inAngle;
	}
}
	