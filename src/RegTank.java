import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;

public class RegTank extends Tank {
    private final static Image REG_TANK_IMG = new Image("./res/images/tank.png");
    private final static int REG_TANK_RADIUS = 100;
    private final static double REG_TANK_CD = 1000.0/1000.0; // in terms of seconds
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    /**
     * constructor for reg tank
     * @param placePoint point for super tank to be placed
     */
    public RegTank(Point placePoint) {
        super(placePoint, REG_TANK_CD, REG_TANK_RADIUS, REG_TANK_IMG);
    }

    /**
     * Specific launch projectile for the reg tank (reg tank launches reg projectile)
     * @param targetEnemy enemy for projectile to target
     */
    @Override
    public void launchProj(Enemy targetEnemy){
        projectiles.add(new RegProjectile(this.getPlacePoint(), targetEnemy));
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
