import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.ArrayList;

public abstract class Tank {
    private double cooldownTime;
    private double cdTimer = 0;
    private Point placePoint;
    private int attackRadius;
    private Image tankImg;
    private static final int TICKPERSEC = 60;
    private DrawOptions rotation = new DrawOptions();

    /**
     * Constructor for the tank class which initialises variables depending on the tank type
     * @param placePoint point at which it is placed
     */
    public Tank(Point placePoint, double cooldownTime, int attackRadius, Image tankImg){
        this.cooldownTime = cooldownTime;
        this.placePoint = placePoint;
        this.attackRadius = attackRadius;
        this.tankImg = tankImg;
        rotation.setRotation(0);
    }

    /**
     * @return the point of placement
     */
    public Point getPlacePoint(){
        return placePoint;
    }

    /**
     * @return the bounding box at point of placement
     */
    public Rectangle getBoundingBox(){
        return tankImg.getBoundingBoxAt(placePoint);
    }

    // Scans for any enemies within the radius and returns the first it finds, null if none

    /**
     * Scans for any enemies within the radius
     * @param activeEnemies used to scan enemies
     * @return first enemy within range it finds, if none are found return null
     */
    public Enemy scanRadius(ArrayList<Enemy> activeEnemies){
        for(Enemy enemy:activeEnemies){
            if(enemy.inRange(placePoint, attackRadius)){
                return enemy;
            }
        }
        return null;
    }

    /**
     * All tank types must have launchProj capabilities
     * @param targetEnemy enemy for projectile to target
     */
    public abstract void launchProj(Enemy targetEnemy);

    /**
     * Draws the tank
     */
    public void draw(){
        tankImg.draw(placePoint.x, placePoint.y, rotation);
    }

    /**
     * Uses maths to set rotation of the tank to face the enemy it fires at
     * @param targetPoint the enemy point at time of firing
     */
    private void facePoint(Point targetPoint){
        double rotationRad = 0; // 0 by default (failsafe)
        Vector2 diffVel = new Vector2 (targetPoint.x - placePoint.x, targetPoint.y - placePoint.y);
        if((diffVel.x != 0.00) && (diffVel.y != 0.00)){
            rotationRad = Math.atan2(diffVel.y, diffVel.x)+(Math.PI/2);
        }
        else if(diffVel.x == 0.00 && diffVel.y < 0.00){
            rotationRad = 0;
        }
        else if(diffVel.x == 0.00 && diffVel.y > 0.00){
            rotationRad = (Math.PI);
        }
        else if(diffVel.y == 0.00 && diffVel.x > 0){
            rotationRad = (Math.PI/2);
        }
        else if(diffVel.y == 0.00 && diffVel.x < 0){
            rotationRad = -(Math.PI/2);
        }
        rotation.setRotation(rotationRad);
    }

    /**
     * Update which happens every tick
     * @param timescale ensures that the update is compliant with the timeScale of the game
     * @param activeEnemies Used to scan radius every time the tank is off cooldown
     */
    public void update(int timescale, ArrayList<Enemy> activeEnemies){
        this.draw();
        if(cdTimer <= 0){
            //Scan for enemies within radius
            Enemy targetEnemy = this.scanRadius(activeEnemies);
            if(!(targetEnemy == null)){
                this.facePoint(targetEnemy.getCurrPoint());
                this.launchProj(targetEnemy);
                cdTimer = cooldownTime*TICKPERSEC;
            }
        }
        else{
            cdTimer = cdTimer - timescale;
        }
    }
}
