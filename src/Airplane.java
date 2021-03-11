import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Airplane {
    private static final int MAX_TIME = 3;
    private static final int TICKPERSEC = 60;
    private static final int WINDOW_X = 1024;
    private static final int WINDOW_Y = 768;
    private static final int OFFSCREEN_MARGIN = 50;
    private final static double AP_SPEED = 5;
    private final static Image AP_IMAGE = new Image("./res/images/airsupport.png");
    private static boolean isHorizontal = true;
    private boolean currOrientationHorizontal;
    private Point currPoint;
    private boolean isActive = true;
    private double timer;
    private ArrayList<Explosive> activeExplosives = new ArrayList<>();
    private DrawOptions rotation = new DrawOptions(); // Rotation of the image
    private Random rand = new Random();

    /**
     * Constructor for an airplane
     * @param target point at which the airplane was placed
     */
    public Airplane(Point target){
        // Choose if the travel of the airplane is horizontal
        currOrientationHorizontal = isHorizontal;

        // Set starting point depending on travel path
        if(currOrientationHorizontal){
            currPoint = new Point(-OFFSCREEN_MARGIN, target.y);
        }
        else{
            currPoint = new Point(target.x, -OFFSCREEN_MARGIN);
        }
        timer = MAX_TIME*rand.nextDouble()*TICKPERSEC;

        // Ensure that the next airplane travel path is alternating
        isHorizontal = !isHorizontal;
    }

    /**
     *
     * @return isActive state
     */
    public boolean getIsActive(){
        return isActive;
    }

    /**
     * Update that happens once every tick
     * @param timeScale ensures that the update is according to the timeScale multiplier
     * @param activeEnemies allows enemies to be passed down to explosives to determine which ones take damage
     */
    public void update(int timeScale, ArrayList<Enemy> activeEnemies){

        // Updating the position
        Vector2 unitVel;
        double travelDist = AP_SPEED*timeScale;
        if(currOrientationHorizontal){
            unitVel = Vector2.right;
            rotation.setRotation(Math.PI/2);
        }
        else{
            unitVel = Vector2.down;
            rotation.setRotation(-Math.PI);
        }
        AP_IMAGE.draw(currPoint.x, currPoint.y, rotation);
        currPoint = currPoint.asVector().add(unitVel.mul(travelDist)).asPoint();

        // Remove isActive tag if the Airplane is within bounds and all explosives have detonated
        if((currPoint.x > WINDOW_X + OFFSCREEN_MARGIN || currPoint.y > WINDOW_Y + OFFSCREEN_MARGIN)
                && activeExplosives.size() == 0){
            this.isActive = false;
        }

        // Timer for dropping bombs
        if(timer <= 0){
            timer = MAX_TIME*rand.nextDouble()*TICKPERSEC;
            activeExplosives.add(new Explosive(currPoint));
        }
        else{
            timer = timer - timeScale;
        }

        // Dropped/ActiveExplosive control
        for(int i = 0; i < activeExplosives.size(); i++){
            if (activeExplosives.get(i).isDetonated()){
                activeExplosives.remove(i);
                i = i - 1;
            }
            else {
                activeExplosives.get(i).update(timeScale, activeEnemies);
            }
        }
    }
}
