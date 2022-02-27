package me.miles.matthew.boids.main;

public final class Constants {
	// Public (finals)
	public static final double defaultAngle = 0d; // radians
	public static final double minSpeed = 20d; // units per second (pixels)
	public static final double maxSpeed = 60d; // units per second (pixels)
	public static final double maxTurningSpeed = 1d; // radians per second
	public static final double influenceRadius = 0d; // sphere of influence radius
	public static double simulationSpeed = 4d; // ratio
	public static final double maxSimulationSpeed = 100d;
	public static final double maxAngularAcceleration = 0.05d; // radians per second squared
	public static final double tailSizeMultiplier = 0.5d; // 1 = 1 pixel per unit per second
	public static final float tailWidth = 2f; // pixels
	public static final double edgeGuardRange = 100d;
	public static final double boidSeparation = 100d;
	
	public static final int[][] arrowShape = {
			{20, -20, -10, -20},
			{00, 20, 0, -20}
	};
	
	public static final double[] viewAngleRange = {0d, Math.PI*2d};
	public static final double[] viewDistanceRange = {100d, 500d};
	
	public static boolean attractToCentre = false;
	public static boolean attractToMouse = false;
	
	public static boolean highlightOne = false;
	
	// Rules
	public static boolean enableCohesion = false; 	// Go to centre of nearby boids
	public static boolean enableSeparation = false; // Avoid collisions
	public static boolean enableAlignment = false;	// Align Direction
	
	public static boolean enableVariation = false;
	public static double varyAmount = 0.001d; // random radians turned per second
	public static final double maxVary = 0.03d; // max vary turning
	
	public static boolean arrowBoid = true;
	public static boolean magnetVisuals = false;
	public static boolean distanceVisuals = false;
	//public static boolean edgeTP = false;
	
	public static int boidCount = 20; // How many boids
	public static final int spawnPadding = 10; // pixels
	public static final int boidPadding = 100; // pixels	
	
	public static int boidViewDistance = 200; // pixels (radius)
	public static double boidViewAngle = Math.PI*(5d/3d); // Radians (full view)
	
	// Private (non-final)
	private static int windowWidth = 1900; // Pixels
	private static int windowHeight = 1000; // Pixels
	
	
	// Getters and setters
	public static void setWindowSize(int x, int y) {
		windowWidth = x;
		windowHeight = y;
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}
	
	public static void setWindowWidth(int windowWidth) {
		Constants.windowWidth = windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}
	
	public static void setWindowHeight(int windowHeight) {
		Constants.windowHeight = windowHeight;
	}
}
