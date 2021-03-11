import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.util.List;

public abstract class Enemy {
    // Protected so that subclasses can draw() their own sprites
    private Point currPoint;
    private int pointIndex;
    private List<Point> polyline;
    private boolean isAlive = true;
    private double rotationRad = 0;
    private double entitySpeed;
    private int entityHealth;
    private int entityReward;
    private int entityPenalty;
    private boolean reachedEnd = false;


    // Getter to determine rotation orientation of the slicer
    public double getRotationRad() {
        return rotationRad;
    }
    // Getter to check if Enemy is alive
    public boolean isAlive() {
        return isAlive;
    }
    // Getter for the currPoint
    public Point getCurrPoint() { return currPoint; }
    // Getter for the polyline
    public List<Point> getPolyline() {
        return polyline;
    }
    // Getter for nextPoint
    public int getPointIndex() { return pointIndex; }
    // Getter for entityPenalty
    public int getEntityPenalty(){
        return entityPenalty;
    }
    // Getter for entityReward
    public int getEntityReward(){
        return entityReward;
    }
    // Getter for reachedEnd
    public boolean reachedEnd(){
        return reachedEnd;
    }

    /**
     * Initialises each property of the slicer
     */
    public Enemy(List<Point> polyline, double entitySpeed, int entityHealth, int entityReward,
                 int entityPenalty, Point currPoint, int pointIndex){
        this.entitySpeed = entitySpeed;
        this.entityHealth = entityHealth;
        this.entityReward = entityReward;
        this.entityPenalty = entityPenalty;
        this.polyline = polyline;
        this.currPoint = currPoint;
        this.pointIndex = pointIndex;
    }

    /**
     * Default on elimination behaviour to set isAlive to false
     */
    public void onElimination(){
        this.isAlive = false;
    }

    /**
     * @return checks if point a is within int range
     */
    public boolean inRange(Point a, int range){
        return (this.currPoint.distanceTo(a) <= range);
    }

    /**
     * applies damageAmt to the enemy (subtracting from health)
     * @param damageAmt amount of damage to be applied
     */
    public void damage(int damageAmt){
        if(this.isAlive()) {
            this.entityHealth = this.entityHealth - damageAmt;
            if (this.entityHealth <= 0) {
                this.onElimination();
            }
        }
    }

    /**
     * Ensures all slicers have the ability to return their bounding box at their position
     * @return bounding box at currPosition
     */
    public abstract Rectangle getBoundingBox();

    /**
     * Updates the position every tick
     * @param timeScale ensures the game is running at the timeScale multiplier
     */
    public void updatePosition(double timeScale){
        double travelDist = timeScale*entitySpeed;

        // End has been reached
        if(pointIndex >= polyline.size()){
            this.isAlive = false;
            this.reachedEnd = true;
            return;
        }

        // Using point index, find the current velocity unit vector from the of the slicer from the polyline points
        Point nextPoint = polyline.get(pointIndex);
        Point prevPoint = polyline.get(pointIndex-1);

        // As the velocity vector is updated, update the rotation
        Vector2 unitVelocity = new Vector2 (nextPoint.x - prevPoint.x, nextPoint.y - prevPoint.y).normalised();
        if((unitVelocity.x != 0.00) && (unitVelocity.y != 0.00)){
            rotationRad = Math.atan2(unitVelocity.y,unitVelocity.x);
        }
        else if(unitVelocity.x == 0.00 && unitVelocity.y < 0.00){
            rotationRad = -(Math.PI/2);
        }
        else if(unitVelocity.x == 0.00 && unitVelocity.y > 0.00){
            rotationRad = (Math.PI/2);
        }
        else if(unitVelocity.y == 0.00 && unitVelocity.x > 0){
            rotationRad = 0;
        }
        else if(unitVelocity.y == 0.00 && unitVelocity.x < 0){
            rotationRad = Math.PI;
        }

        // If the distance to be travelled is greater than the distance left between the current point and the next
        // polyline point, using recursion set the currPoint to the next point and attempt to traverse the remaining
        // distance
        double distToNextPoint = currPoint.distanceTo(nextPoint);
        if (travelDist >= distToNextPoint){
            currPoint = polyline.get(pointIndex);
            pointIndex++;
            this.updatePosition(distToNextPoint - travelDist);
        }

        // Otherwise, simply use vector addition to determine the position of the slicer
        else{
            currPoint = currPoint.asVector().add(unitVelocity.mul(travelDist)).asPoint();
        }

    }
}
