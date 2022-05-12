/* # Haider Tank - Embrace-it
 * Copyright (c) 2022 Kamal Uddin Panhwar, Jazib and Misbha
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
// Required references
package sample;
import robocode.*;
import java.awt.*;

/**
 * HaiderTank - A tank created by Kamal, Jazib and Misbah.
 * <p>
 * The tank keep moving, when it sees tank fires, and move quickly to different location.
 * Algorithm
 * Our Algorithm
 * Our robot start moving with scanner and when it sees target, it fire on target, after fire it quickly change location. Then again fire on target.
 * The way it moves has two moments and try to move little right and then again come to locaion with raddical moment.
 * When it hit wall it should return back
 * If it got hit we want to it to change its location quickly so other robots can not destroy it easily.
 * - Tracker still was beating us, but then we manage to dodge him, but random bullets still kill our robot quickly
 * 
 * issues:
 * 1. Energy level , it is hitting with small power, but if it gets closer it can use moreEnergy to hit
 * 2. Testing with many different bots.
 * @author Jazib Mehmood(original)
 * @author Kamal Uddin Panhwar (contributor)
 * @author Misbah (contributor)
 */

 // Using AdvanceRobot
public class HaideryTank extends AdvancedRobot {
	int directionOfTank =1;
	public void run() {
        setAdjustRadarForRobotTurn(true);//keep the radar still while we turn
        setBodyColor(new Color(18, 12, 0));
        setGunColor(new Color(20, 30, 20));
        setRadarColor(new Color(20, 20, 50));
        setScanColor(Color.white);
        setBulletColor(Color.blue);
        setAdjustGunForRobotTurn(true); // Keep the gun still when we turn
        turnRadarRightRadians(Double.POSITIVE_INFINITY);//keep turning radar right
    }

    /**
     * onScannedRobot:  If enemy is visible
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        double absoluteBearing=e.getBearingRadians()+getHeadingRadians();//enemies absolute bearing
        double velocityOfEnemy=e.getVelocity() * Math.sin(e.getHeadingRadians() -absoluteBearing);//enemies later velocity
        double gunMoveAmount;// How much gun turn
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());//lock on the radar
        if(Math.random()>.9){
            setMaxVelocity((12*Math.random())+12);
        }
		//if the distance between enemy and us is > 150
        if (e.getDistance() > 150) {
			
            gunMoveAmount = robocode.util.Utils.normalRelativeAngle(absoluteBearing- getGunHeadingRadians()+velocityOfEnemy/22);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunMoveAmount); //turn our gun
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(absoluteBearing-getHeadingRadians()+velocityOfEnemy/getVelocity()));//drive towards the enemies predicted future location
            setAhead((e.getDistance() - 140)*directionOfTank);//move forward
            setFire(3);
        }
        else{
            gunMoveAmount = robocode.util.Utils.normalRelativeAngle(absoluteBearing- getGunHeadingRadians()+velocityOfEnemy/15);//amount to turn our gun, lead just a little bit
            setTurnGunRightRadians(gunMoveAmount);//turn our gun
            setTurnLeft(-90-e.getBearing()); //turn perpendicular to the enemy
            setAhead((e.getDistance() - 140)*directionOfTank);//move forward
            setFire(3);
        }
    }
    public void onHitWall(HitWallEvent e){
        directionOfTank=-directionOfTank;
    }
    /**
     * onWin:  Do a victory dance
     */
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }
}