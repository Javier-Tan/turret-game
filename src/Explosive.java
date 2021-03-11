import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class Explosive {
    private static final int TICKPERSEC = 60;
    private int detonateTime = 2*TICKPERSEC;
    private Point detonatePosition;
    private ArrayList<Enemy> inRadius = new ArrayList<>();
    private boolean detonated = false;
    private final static int EXP_RANGE = 200;
    private final static int EXP_DAMAGE = 500;
    private final static Image EXP_IMAGE = new Image("./res/images/explosive.png");

    /**
     * Constructor for explosive
     * @param detonatePosition determines the position that it will be rendered and detonate at
     */
    public Explosive(Point detonatePosition){
        this.detonatePosition = detonatePosition;
    }

    /**
     * @return the detonated flag
     */
    public boolean isDetonated(){
        return detonated;
    }

    /**
     * Update which happens every tick
     * @param timeScale ensures that the timer is affected by the timeScale of the game
     * @param activeEnemies used to check which enemies are to be damaged within range of detonation point
     */
    public void update(int timeScale, ArrayList<Enemy> activeEnemies){
        // Draw the explosive
        EXP_IMAGE.draw(detonatePosition.x, detonatePosition.y);

        // If detonate timer is done, detonate
        if(detonateTime <= 0){
            for(Enemy enemy:activeEnemies){
                // Find all enemies in range
                if(enemy.inRange(detonatePosition, EXP_RANGE)){
                    inRadius.add(enemy);
                }
            }
            // Damage all enemies in range
            for(Enemy enemy:inRadius){
                enemy.damage(EXP_DAMAGE);
            }
            // Set detonated tag to true
            detonated = true;
        }
        // If detonate timer is not done, continue the timer
        else{
            detonateTime = detonateTime - timeScale;
        }
    }

}
