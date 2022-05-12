/* # Haider Tank - Embrace-it
 * Copyright (c) 2022 Kamal Uddin Panhwar, Jazib and Misbha
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package sample;
import robocode.*;
import java.util.Random;
import java.awt.*;
import robocode.util.Utils;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 * HaiderTank - A tank created by Kamal, Jazib and Misbah.
 * <p>
 * The tank keep moving, when it sees tank fires, and move quickly to different location.
 *
 * @author Jazib Mehmood(original)
 * @author Kamal Uddin Panhwar (contributor)
 * @author Misbah (contributor)
 */
// Using AdvanceRobot
public class HaideryTank extends AdvancedRobot {
	double dest_x = 0, dest_y = 0;
	int enemy_x, enemy_y;
	int count = 0; // Counter to find targeted tank how long
	double gunTurnAmt; // When searching how much we move tank
	String trackName; // Name of the robot we're currently tracking
	private byte moveDirection = 1;


	private void move(double x, double y) {
		dest_x = x;
		dest_y = y;
		double cur_x = getX();
		double cur_y = getY();
		System.out.println("move (" + cur_x + ", " + cur_y + ") -> (" + x + ", " + y + ")");
		/* vector from us to there */
		x -= cur_x;
		y -= cur_y;

		/* Calculate the angle to the target position */
		// atan2 converts vector in cartesian coordinates to the angle (in radians) in
		// polar coordinates
		double angleToTarget = Math.atan2(x, y);

		/* Calculate the turn required get there */
		double targetAngle = Utils.normalRelativeAngle(angleToTarget - Math.toRadians(getHeading()));
		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));

		setTurnRight(Math.toDegrees(turnAngle));
		waitFor(new TurnCompleteCondition(this));
		if (targetAngle == turnAngle) {
			//setAhead(distance);
		} else {
			//setBack(distance);
		}
		waitFor(new MoveCompleteCondition(this));
	}
	/**
	 * TrackFire's run method
	 */
	public void run() {
		// Set colors
		setBodyColor(Color.green);
		setGunColor(Color.yellow);
		setRadarColor(Color.white);
		setScanColor(Color.blue);
		setBulletColor(Color.red);
		// set radar
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		Random  r = new Random();
		double tank_width; 
		double tank_height;
		double tank_max_dim;
		double bf_width;
		double bf_height;


	while (true) {
		setTurnRadarRight(Double.MAX_VALUE);
		ahead(100);
		turnGunRight(360);
		turnGunRight(360);
		setTurnRadarRight(49);
		 tank_width = getWidth();
		 tank_height = getHeight();
		 tank_max_dim = Math.max(tank_width, tank_height);
		 bf_width = getBattleFieldWidth();
		 bf_height = getBattleFieldHeight();
		
			move(tank_max_dim + r.nextDouble(bf_width - tank_max_dim ),
				 tank_max_dim + r.nextDouble(bf_height - tank_max_dim));
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
				setTurnRight(e.getBearing() + 90);
				setAhead(1000 * moveDirection);
			}
		}
		else {
			turnGunRight(bearingFromGun);
		}
		if (bearingFromGun == 0) {
			scan();
		}

		// Victory dance
		//turnRight(36000);
	}

public void onHitWall(HitWallEvent e) {
	// demonstrate feature of debugging properties on RobotDialog
	//setDebugProperty("lastHitBy", e.getName() + " with power of bullet " + e.getPower() + " at time " + getTime());

	// show how to remove debugging property
	setDebugProperty("lastScannedRobot", null);

	// gebugging by painting to battle view
	Graphics2D g = getGraphics();

	g.setColor(Color.blue);
	g.drawOval((int) (getX() - 55), (int) (getY() - 55), 110, 110);
	g.drawOval((int) (getX() - 56), (int) (getY() - 56), 112, 112);
	g.drawOval((int) (getX() - 59), (int) (getY() - 59), 118, 118);
	g.drawOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);
	
	turnLeft(90 - e.getBearing());
}

public void onPaint(Graphics2D g) {
	g.setColor(Color.red);
	g.drawOval((int) (getX() - 50), (int) (getY() - 50), 100, 100);
	g.setColor(new Color(0, 0xFF, 0, 30));
	g.fillOval((int) (getX() - 60), (int) (getY() - 60), 120, 120);
}
}
