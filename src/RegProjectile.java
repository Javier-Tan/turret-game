import bagel.Image;
import bagel.util.Point;

public class RegProjectile extends Projectile {
    private final static int REGPROJ_DMG = 1;
    private final static Image REG_TANK_IMG = new Image("./res/images/tank_projectile.png");

    /**
     * Constructor for projectile
     * @param originPoint starting point of projectile
     * @param targetEnemy target enemy to track
     */
    public RegProjectile(Point originPoint, Enemy targetEnemy){
        super(originPoint, REGPROJ_DMG, targetEnemy, REG_TANK_IMG);
    }
}
