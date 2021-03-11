import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

public abstract class Projectile {
    private static final double SPEED = 10;
    private Point currPoint;
    private int damage;
    private boolean isActive = true;
    private Enemy targetEnemy;
    private Image projImg;

    /**
     * Constructor for projectile, initialises variables depending on type of projectile
     * @param originPoint starting point of projectile
     * @param targetEnemy targetEnemy to track
     */
    public Projectile(Point originPoint, int damage, Enemy targetEnemy, Image projImg){
        this.currPoint = originPoint;
        this.damage = damage;
        this.targetEnemy = targetEnemy;
        this.projImg = projImg;
    }

    /**
     * @return the isActive flag
     */
    public boolean getIsActive(){
        return isActive;
    }

    /**
     * Updates the projectile to move towards and eventually hit the target every tick
     * @param timeScale ensures that the projectile speed is affected by the timeScale of the game
     */
    public void update(int timeScale){
        projImg.draw(currPoint.x, currPoint.y);
        //Move towards enemy
        Point enemyPoint = targetEnemy.getCurrPoint();
        double travelDist = SPEED*timeScale;
        Vector2 unitVel = new Vector2(enemyPoint.x - currPoint.x, enemyPoint.y - currPoint.y).normalised();
        currPoint = currPoint.asVector().add(unitVel.mul(travelDist)).asPoint();

        //Check collision
        if(projImg.getBoundingBoxAt(currPoint).intersects(targetEnemy.getBoundingBox())){
            targetEnemy.damage(damage);
            this.isActive = false;
        }
    }
}
