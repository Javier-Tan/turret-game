import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.awt.*;
import java.util.List;

public class RegSlicer extends Enemy {
    private Image slicerImg = new Image("./res/images/slicer.png"); // Image of the slicer
    DrawOptions rotation = new DrawOptions(); // Rotation of the image
    private final static int REG_HEALTH = 1;
    private final static double REG_SPEED = 2.0;
    private final static int REG_REWARD = 2;
    private final static int REG_PENALTY = 1;


    /**
     * Setter for an slicer that provides the enemy abstract class with the constants of an slicer
     * @param polyline polyline to travel on
     * @param currPoint where the slicer is to spawn
     * @param nextPoint which index of the polyline the slicer must travel to next
     */
    public RegSlicer(List<Point> polyline, Point currPoint, int nextPoint) {
        super(polyline, REG_SPEED, REG_HEALTH, REG_REWARD, REG_PENALTY, currPoint, nextPoint);
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
        if(this.isAlive()){
            super.updatePosition(timeScale);
            rotation.setRotation(this.getRotationRad());
            Point currPoint = this.getCurrPoint();
            slicerImg.draw(currPoint.x, currPoint.y, rotation);
        }
    }
}
