import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SuperSlicer extends Enemy {
    private Image slicerImg = new Image("./res/images/superslicer.png"); // Image of the slicer
    DrawOptions rotation = new DrawOptions(); // Rotation of the image
    final static int SUP_HEALTH = 1;
    final static double SUP_SPEED = 1.5;
    final static int SUP_REWARD = 15;
    final static int SUP_PENALTY = 1;
    final static int CHILD_AMT = 2;
    ArrayList<Enemy> activeEnemies;

    /**
     * Setter for an superSlicer that provides the enemy abstract class with the constants of an superSlicer
     * @param polyline polyline to travel on
     * @param currPoint where the slicer is to spawn
     * @param nextPoint which index of the polyline the slicer must travel to next
     * @param activeEnemies activeEnemies list that it will be able to add children to
     */
    public SuperSlicer(List<Point> polyline, Point currPoint, int nextPoint, ArrayList<Enemy> activeEnemies) {
        super(polyline, SUP_SPEED, SUP_HEALTH, SUP_REWARD, SUP_PENALTY, currPoint, nextPoint);
        this.activeEnemies = activeEnemies;
    }

    /**
     * @return Bounding box of the current enemy at it's position
     */
    @Override
    public Rectangle getBoundingBox(){
        return slicerImg.getBoundingBoxAt(this.getCurrPoint());
    }

    /**
     * OnElimination set isAlive tag to false and spawns children
     */
    @Override
    public void onElimination(){
        super.onElimination();
        Point currPoint = this.getCurrPoint();
        for(int i = 0; i < CHILD_AMT; i++){
            activeEnemies.add(new RegSlicer(this.getPolyline(), currPoint, this.getPointIndex()));
        }
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
