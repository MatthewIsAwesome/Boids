package me.miles.matthew.boids.main;

import java.awt.Color;
import java.util.Arrays;

public class Utils {
	
	// lerp
	public static double lerp(double a, double b, double f) {
	    return (a * (1.0d - f)) + (b * f);
	}
	
	public static int lerp(int a, int b, double f) {
		f = f % 1d;
	    return (int) Math.round((a * (1.0d - f)) + (b * f));
	}
	
	public static double logerp(double a, double b, double f){
		  return a*Math.pow(b/a, f);
	}
	
	public static double logscale(double value) {
	    if (value > 1.0)
	        return Math.log(value);
	    else
	        return value - 1.0;
	}
	
	public static double angleTo(double x1, double y1, double a1r, double x2, double y2) {
		// NOT Method verified good 8.5/10 confidence
		// Find angle between x1,y1 and x2,y2
		double angle = Math.atan2(y2-y1, x2-x1); // angle from 0 (right)
		//angle += a1r;
		
		if (angle < 0)
			angle += 2*Math.PI;
		
		angle = angle % (2*Math.PI);
		
		// subtract? a1r
		//angle -= a1r;
		return angle;
	}
	
	public static double distanceTo(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
	}
	
	public static double distanceTo(Boid b1, Boid b2) {
		return distanceTo(b1.getX(), b1.getY(), b2.getX(), b2.getY());
	}
	
	public static double getMeanAngle(double... anglesRad) {
		// Note this is from https://rosettacode.org/wiki/Averages/Mean_angle#Java
        double x = 0.0;
        double y = 0.0;
 
        for (double angleR : anglesRad) {
            x += Math.cos(angleR);
            y += Math.sin(angleR);
        }
        
        double avgR = Math.atan2(y / anglesRad.length, x / anglesRad.length);
        return avgR;
    }
	
	public static Color getAverageHueColor(Color a, Color b) {
		float[] aHSB = new float[3];
		float[] bHSB = new float[3];
		Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), aHSB);
		Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), bHSB);
		//S/ystem.out.println(Arrays.toString(aHSB)+Arrays.toString(bHSB));
		
		return Color.getHSBColor((float) (aHSB[0]+bHSB[0])/2f, 1f, 1f);
	}
}
