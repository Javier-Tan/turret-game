import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class MegaSlicer extends Enemy {
    private Image slicerImg = new Image("./res/images/megaslicer.png"); // Image of the slicer
    DrawOptions rotation = new DrawOptions(); // Rotation of the image
    final static int MEGA_HEALTH = 2;
    final static double MEGA_SPEED = 1.5;
    final static int MEGA_REWARD = 10;
    final static int MEGA_PENALTY = 4;
    final static int CHILD_AMT = 2;
    ArrayList<Enemy> activeEnemies;

    /**
     * Setter for an megaSlicer that provides the enemy abstract class with the constants of an megaSlicer
     * @param polyline polyline to travel on
     * @param currPoint where the slicer is to spawn
     * @param nextPoint which index of the polyline the slicer must travel to next
     * @param activeEnemies activeEnemies list that it will be able to add children to
     */
    public MegaSlicer(List<Point> polyline, Point currPoint, int nextPoint, ArrayList<Enemy> activeEnemies) {
        super(polyline, MEGA_SPEED, MEGA_HEALTH, MEGA_REWARD, MEGA_PENALTY, currPoint, nextPoint);
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
            activeEnemies.add(new SuperSlicer(this.getPolyline(), currPoint, this.getPointIndex(), activeEnemies));
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
