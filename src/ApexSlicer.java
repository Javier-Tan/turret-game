import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ApexSlicer extends Enemy {
    private Image slicerImg = new Image("./res/images/apexslicer.png"); // Image of the slicer
    DrawOptions rotation = new DrawOptions(); // Rotation of the image
    final static int APEX_HEALTH = 25;
    final static double APEX_SPEED = 0.75;
    final static int APEX_REWARD = 150;
    final static int APEX_PENALTY = 16;
    final static int CHILD_AMT = 4;
    ArrayList<Enemy> activeEnemies;

    /**
     * Setter for an apexSlicer that provides the enemy abstract class with the constants of an apexSlicer
     * @param polyline polyline to travel on
     * @param currPoint where the slicer is to spawn
     * @param nextPoint which index of the polyline the slicer must travel to next
     * @param activeEnemies activeEnemies list that it will be able to add children to
     */
    public ApexSlicer(List<Point> polyline, Point currPoint, int nextPoint, ArrayList<Enemy> activeEnemies) {
        super(polyline, APEX_SPEED, APEX_HEALTH, APEX_REWARD, APEX_PENALTY, currPoint, nextPoint);
        this.activeEnemies = activeEnemies;
    }

    /**
     * OnElimination set isAlive tag to false and spawns children
     */
    @Override
    public void onElimination(){
        super.onElimination();
        Point currPoint = this.getCurrPoint();
        for(int i = 0; i < CHILD_AMT; i++){
            activeEnemies.add(new MegaSlicer(this.getPolyline(), currPoint, this.getPointIndex(), activeEnemies));
        }
    }

    /**
     * @return Bounding box of the current enemy at it's position
     */
    @Override
    public Rectangle getBoundingBox(){
        return slicerImg.getBoundingBoxAt(this.getCurrPoint());
    }

    /**
     * Use abstract class enemy logic to update the position and draw slicerImg with the overridden method
     */
    @Override
    public void updatePosition(double timeScale){
        // If alive, update position
        if (this.isAlive()) {
            super.updatePosition(timeScale);
            rotation.setRotation(this.getRotationRad());
            Point currPoint = this.getCurrPoint();
            slicerImg.draw(currPoint.x, currPoint.y, rotation);
        }
    }
}
