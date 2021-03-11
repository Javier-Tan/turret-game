import bagel.Image;
import bagel.util.Point;

public class SuperProjectile extends Projectile {
    private final static int SUPPROJ_DMG = 3;
    private final static Image SUP_TANK_IMG = new Image("./res/images/supertank_projectile.png");

    /**
     * Constructor for projectile
     * @param originPoint starting point of projectile
     * @param targetEnemy target enemy to track
     */
    public SuperProjectile(Point originPoint, Enemy targetEnemy){
        super(originPoint, SUPPROJ_DMG, targetEnemy, SUP_TANK_IMG);
    }
}
