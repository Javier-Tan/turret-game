import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class SuperTank extends Tank {
    private final static Image SUP_TANK_IMG = new Image("./res/images/supertank.png");
    private final static int SUP_TANK_RADIUS = 150;
    private final static double SUP_TANK_CD = 500.0/1000.0; // in terms of seconds
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * constructor for super tank
     * @param placePoint point for super tank to be placed
     */
    public SuperTank(Point placePoint) {
        super(placePoint, SUP_TANK_CD, SUP_TANK_RADIUS, SUP_TANK_IMG);
    }

    /**
     * Specific launch projectile for the super tank (super tank launches super projectile)
     * @param targetEnemy enemy for projectile to target
     */
    @Override
    public void launchProj(Enemy targetEnemy){
        projectiles.add(new SuperProjectile(this.getPlacePoint(), targetEnemy));
    }

    /**
     * Update which happens every tick
     * @param timeScale ensure update happens complaint with timeScale of the game
     * @param activeEnemies Used to scan radius every time the tank is off cooldown
     */
    @Override
    public void update(int timeScale, ArrayList<Enemy> activeEnemies){
        super.update(timeScale, activeEnemies);
        for(int i = 0; i < projectiles.size(); i++){
            if(!projectiles.get(i).getIsActive()){
                projectiles.remove(i);
                i = i - 1;
            }
            else{
                projectiles.get(i).update(timeScale);
            }
        }
    }
}
