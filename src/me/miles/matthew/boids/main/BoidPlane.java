package me.miles.matthew.boids.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BoidPlane extends JPanel implements MouseListener {
	private ArrayList<Boid> boids = new ArrayList<Boid>();
	private Random r = new Random();
	AffineTransform zeroTransform;
	
	public BoidPlane(int width, int height) {
		super();
		this.setSize(width, height);
		this.setBackground(new Color(24, 25, 25));
		
		this.addMouseListener(this);
		
		Boid b;
		
		for (int i = 0; i < Constants.boidCount; i++) {
			b = new Boid(
					Utils.lerp(Constants.getWindowWidth(),  Constants.spawnPadding, r.nextDouble()),
					Utils.lerp(Constants.getWindowHeight(), Constants.spawnPadding, r.nextDouble())
					);
			b.setRandomColor();
			b.setAngle(Utils.lerp(2*Math.PI, 0, r.nextDouble()));
			boids.add(b);
		}
	}
	
	public void update(double timePassed) {
		updatePhysics(timePassed);
		repaint();
	}
	
	@SuppressWarnings("unused")
	public void updatePhysics(double timePassed) {
		int w = Constants.getWindowWidth();
		int h = Constants.getWindowHeight();
		
		//System.out.println(boids[0].getGoal()[1]);
		
		
		for (Boid b : boids) {
			// Refresh boids
			b.resetMagnets();
			
			b.setX(b.getX() + timePassed*b.getSpeed()*Math.cos(b.getAngle()));
			b.setY(b.getY() + timePassed*b.getSpeed()*Math.sin(b.getAngle()));
			
			// Mouse position
			Point mPos = MouseInfo.getPointerInfo().getLocation();
			mPos.setLocation(mPos.getX()-this.getLocationOnScreen().getX(),
					mPos.getY()-this.getLocationOnScreen().getY());
			
			// Mouse attraction
			if (Constants.attractToMouse) {
				//double distance2M = Utils.distanceTo(b.getX(), b.getY(), mPos.getX(), mPos.getY());
				if (b.canSee(mPos.getX(), mPos.getY())) {
					// -ves align with tip of pointer
					b.seek((int) Math.round(mPos.getX()), (int) Math.round(mPos.getY()), 5d);
					//b.updateGoal();
					//b.alterCourse();
					//continue;
				}
			}
			
			// Attract to centre
			if (Constants.attractToCentre) {
				double distance2C = Utils.distanceTo(b.getX(), b.getY(), w/2, h/2);
				b.seek(w/2, h/2, 1d);
			}
			
			// Find how many boids are visible
			ArrayList<Boid> visibleBoids = new ArrayList<Boid>();
			for (Boid c : boids) {
				if (b.equals(c)) // Don't attract to self
					continue;
				
				//S/ystem.out.println("Can I see?");
				if (b.canSee(c)) 
					visibleBoids.add(c);
			}
			
			double[] averageVisiblePosition = {0d, 0d};
			double averageVisibleAngle = 0d;
			double visibleBoidCount = visibleBoids.size();
			
			// 
			if (visibleBoidCount != 0)
			for (Boid c : visibleBoids) {
				Color bCol = b.getColor();
				Color cCol = c.getColor();
				
				
				/*
				 * b.setColor(new Color((int) Math.round(bCol.getRed()/2d+cCol.getRed()/2d),
				 *		(int) Math.round(bCol.getGreen()/2d+cCol.getGreen()/2d),
				 *		(int) Math.round(bCol.getBlue()/2d+cCol.getBlue()/2d)));
				 */
				
				/*
				 * if (boids.indexOf(b) > 2)
				 * b.setColor(Utils.getAverageHueColor(b.getColor(), c.getColor()));
				 */
				
				if (Constants.enableCohesion) {
					//if (Utils.distanceTo(b, c) < Constants.boidSeparation || !Constants.enableAlignment) {
						averageVisiblePosition[0] += c.getX()/(double) visibleBoidCount;
						averageVisiblePosition[1] += c.getY()/(double) visibleBoidCount;
					//}
				}
				
				if (Constants.enableAlignment) {
					averageVisibleAngle += c.getAngle()/ (double) visibleBoidCount;
				}
				
				if (Constants.enableSeparation && Utils.distanceTo(b, c) < Constants.boidSeparation) {
					b.avoid((int) Math.round(c.getX()), (int) Math.round(c.getY()),
							Utils.distanceTo(b, c)/Constants.boidViewDistance);
				}
			}
			
			
			if (Constants.enableCohesion && visibleBoidCount > 0) {
				b.seek((int) Math.round(averageVisiblePosition[0]),
						(int) Math.round(averageVisiblePosition[1]), 1d);
			}
			
			if (Constants.enableAlignment && visibleBoidCount > 0) {
				b.seek((int) Math.round((Math.cos(averageVisibleAngle)*200d)+b.getX()),
						(int) Math.round((Math.sin(averageVisibleAngle)*200d)+b.getY()),
						1d); // *200 is to show up on traces
			}
				
			if (h-b.getY() < Constants.edgeGuardRange)
				b.avoid((int) Math.round(b.getX()), h, 1d);//Utils.lerp(0, 2, (h-b.getY())/h));
			
			if (b.getY() < Constants.edgeGuardRange)
				b.avoid((int) Math.round(b.getX()), 0, 1d);
			
			if (w-b.getX() < Constants.edgeGuardRange)
				b.avoid(w, (int) Math.round(b.getY()), 1d);//Utils.lerp(0, 2, (h-b.getY())/h));
			
			if (b.getX() < Constants.edgeGuardRange)
				b.avoid(0, (int) Math.round(b.getY()), 1d);
			
			// If out of bounds, overwrite all previous with attraction to centre
			
			if (h-b.getY() < 0 || b.getY() < 0 || w-b.getX() < 0 || b.getX() < 0) {
				b.resetMagnets();
				b.setAngle(Utils.angleTo(b.getX(), b.getY(), b.getAngle(), w/2, h/2));
			}
			
			b.updateGoal();
			b.alterCourse(timePassed);
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		zeroTransform = g2.getTransform();
		g2.setColor(Color.white);
		RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_ANTIALIASING,
	             RenderingHints.VALUE_ANTIALIAS_ON
	             );
	    g2.setRenderingHints(rh);
	    g2.setStroke(new BasicStroke(Constants.tailWidth));
		
		
		
		if (Constants.attractToCentre) {
			double diameter = 100f;
			g2.setColor(Color.black);
			g2.fillOval((int) Math.round((Constants.getWindowWidth()/2)-(diameter/2)),
					(int) Math.round((Constants.getWindowHeight()/2)-(diameter/2)),
							(int) Math.round(diameter), (int) Math.round(diameter));
		}
		
		
		
		if (Constants.magnetVisuals) {
			/*//debug
			for (double[] m : boids[0].getMagnets()) {
				//System.out.println(Arrays.toString(m));
				g2.fillOval(((int) Math.round(m[0]))-3, ((int) Math.round(m[1]))-3, 6, 6);
			}*/
			
			for (Boid b : boids) {
				
				for (double[] d : b.getMagnets()) {
					g2.setColor(b.getColor().darker().darker().darker());

					if (true) {//b.getColor().getGreen() > 254 && b.getColor().getBlue() > 230) {
						g2.drawLine((int) Math.round(b.getX()),
								(int) Math.round(b.getY()),
								(int) Math.round(d[0]),
								(int) Math.round(d[1])
								);
					}
				}
				
				g2.setColor(Color.gray);
				g2.drawLine(
						(int) Math.round(b.getTotalMove()[0]),
						(int) Math.round(b.getTotalMove()[1]),
						(int) Math.round(b.getX()),
						(int) Math.round(b.getY()));
				
			}
		} else if (Constants.highlightOne) {
			Boid b = boids.get(0);
			for (double[] d : b.getMagnets()) {
				g2.setColor(b.getColor().darker().darker().darker());

				if (true) {//b.getColor().getGreen() > 254 && b.getColor().getBlue() > 230) {
					g2.drawLine((int) Math.round(b.getX()),
							(int) Math.round(b.getY()),
							(int) Math.round(d[0]),
							(int) Math.round(d[1])
							);
				}
			}
			
			g2.setColor(Color.gray);
			g2.drawLine(
					(int) Math.round(b.getTotalMove()[0]),
					(int) Math.round(b.getTotalMove()[1]),
					(int) Math.round(b.getX()),
					(int) Math.round(b.getY()));
		}
		
		if (Constants.distanceVisuals) {
			for (Boid b : boids) {
				g2.setColor(new Color(255, 255, 255, 5));
				g2.fillArc((int) Math.round(b.getX()-Constants.boidViewDistance),
						(int) Math.round(b.getY()-Constants.boidViewDistance),
						Constants.boidViewDistance*2, Constants.boidViewDistance*2,
						(int) Math.round(Math.toDegrees(	-(b.getAngle()+(Constants.boidViewAngle/2))	)),
						(int) Math.round(Math.toDegrees(	Constants.boidViewAngle	))
						);
				//g2.fillOval((int) Math.round(b.getX()-Constants.boidViewDistance),
				//		(int) Math.round(b.getY()-Constants.boidViewDistance),
				//		Constants.boidViewDistance*2, Constants.boidViewDistance*2);
			}
		} else if (Constants.highlightOne) {
			Boid b = boids.get(0);
			g2.setColor(new Color(255, 255, 255, 5));
			g2.fillArc((int) Math.round(b.getX()-Constants.boidViewDistance),
					(int) Math.round(b.getY()-Constants.boidViewDistance),
					Constants.boidViewDistance*2, Constants.boidViewDistance*2,
					(int) Math.round(Math.toDegrees(	-(b.getAngle()+(Constants.boidViewAngle/2))	)),
					(int) Math.round(Math.toDegrees(	Constants.boidViewAngle	))
					);
		}
		
		for (Boid b : boids) { 
			g2.setColor(b.getColor());
			drawBoid(g2, (int) Math.round(b.getX()), (int) Math.round(b.getY()), b.getSpeed(), b.getAngle());
		}
	}
	
	private void drawBoid(Graphics2D g2, int x, int y, double magnitude, double angle) {
		if (Constants.arrowBoid) {
			g2.translate(x, y); // move g2 to the position of the poly temporarily
			g2.rotate(angle);
			g2.scale(0.75, 0.75);
			g2.fillPolygon(Constants.arrowShape[0], Constants.arrowShape[1], Constants.arrowShape[0].length);
			// outline
			g2.setColor(g2.getColor().darker().darker());
			g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2.drawPolygon(Constants.arrowShape[0], Constants.arrowShape[1], Constants.arrowShape[0].length);
			g2.setTransform(zeroTransform); // reset transformation
		} else {
			g2.fillOval(x-5, y-5, 10, 10);
			g2.drawLine(x, y, (int) Math.round(x+Constants.tailSizeMultiplier*magnitude*Math.cos(Math.PI+angle)),
					(int) Math.round(y+Constants.tailSizeMultiplier*magnitude*Math.sin(Math.PI+angle)));
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent me) {
	    if (true) { //SwingUtilities.isRightMouseButton(me)){
	    	//S/ystem.out.println("click");
	    	Boid tmp = new Boid((int) Math.round(me.getX()), (int) Math.round(me.getY()));
		    tmp.setRandomColor();
		    tmp.setAngle(r.nextDouble()*2*Math.PI);
		    boids.add(tmp);
		    Constants.boidCount++;
	    }
	}

	@Override
	public void mouseClicked(MouseEvent me) {}

	@Override
	public void mouseEntered(MouseEvent me) {}

	@Override
	public void mouseExited(MouseEvent me) {}

	@Override
	public void mousePressed(MouseEvent me) {}
}
