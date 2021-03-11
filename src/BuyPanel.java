import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Colour;
import bagel.util.Point;

import java.util.ArrayList;

public class BuyPanel {
    private static final int WINDOW_X = 1024;
    private static final int WINDOW_Y = 768;
    private final static Image BP_IMG = new Image("./res/images/buypanel.png");
    private final static double MIDDLE_Y = BP_IMG.getHeight()/2;
    private final static double MIDDLE_X = BP_IMG.getWidth()/2;
    private final static int BUY_ICON_OFFSET_Y = (int)(MIDDLE_Y - 10);
    private final static Image TANK_IMG = new Image("./res/images/tank.png");
    private final static int TANK_OFFSETX = 64;
    private final static int T_PRICE = 250;
    private final static String T_PRICE_S = "$250";
    private final static Image STANK_IMG = new Image("./res/images/supertank.png");
    private final static int STANK_OFFSETX = TANK_OFFSETX + 120;
    private final static int ST_PRICE = 600;
    private final static String ST_PRICE_S = "$600";
    private final static Image AP_IMG = new Image("./res/images/airsupport.png");
    private final static int AP_OFFSETX = STANK_OFFSETX + 120;
    private final static int AP_PRICE = 500;
    private final static String AP_PRICE_S = "$500";
    private final static String KEY_BIND_TEXT = "Key binds: \n\nS - Start Wave \n" +
            "L - Increase timescale by 1\nK - Decrease timescale by 1";
    // Fonts and stuffs
    private final static int PRICE_FONT_SIZE = 18;
    private final static Font PRICE_FONT  = new Font("./res/fonts/DejaVuSans-Bold.ttf", PRICE_FONT_SIZE);
    private final static int PRICE_OFFSET_Y = (int) MIDDLE_Y + 38;
    private final static int PRICE_MIDOFFSET_X = -25;
    private DrawOptions bColour = new DrawOptions();

    private final static int KEYB_FONT_SIZE = 15;
    private final static Font KEYBIND_FONT = new Font("./res/fonts/DejaVuSans-Bold.ttf", KEYB_FONT_SIZE);
    private final static int KEYB_MIDOFFSET_X = -30;
    private final static int KEYB_OFFSET_Y = 20;

    private final static int MONEY_FONT_SIZE = 50;
    private final static Font MONEY_FONT = new Font("./res/fonts/DejaVuSans-Bold.ttf", MONEY_FONT_SIZE);
    private final static int MONEY_OFFSET_X = -200;
    private final static int MONEY_OFFSET_Y = 65;
    private boolean isPlacing = false;
    private Image placingImage;
    private int placingPrice;
    private int money = 500;
    private Tank addTank = null;
    private Airplane addAirplane = null;

    /**
     * Constructor for the buy panel
     */
    public BuyPanel(){
    }

    /**
     * @return the tank in addTank cache (null if empty)
     */
    public Tank addTank(){
        return addTank;
    }

    /**
     * @return the airplane in addAirplane cache (null if empty)
     */
    public Airplane addAirplane(){
        return addAirplane;
    }

    /**
     * @return if placing is currently happening
     */
    public boolean isPlacing(){
        return isPlacing;
    }

    /**
     * Sets the colour of the font depending on if money is higher than price
     */
    private void setBColour(int price){
        if(money >= price){
            bColour.setBlendColour(Colour.GREEN);
        }
        else{
            bColour.setBlendColour(Colour.RED);
        }
    }

    /**
     * Adds money if a reward is given
     * @param reward money to be added
     */
    public void addMoney(int reward){
        this.money = this.money + reward;
    }

    /**
     * Update which happens every tick
     * @param input reads the input to determine where to display the a placing item or if a buyitem is clicked
     * @param activeTanks needed to check if a tank's bounding box overlaps the cursor of a  placing item
     * @param map needed to determine if the tile is blocked on the map
     */
    public void update(Input input, ArrayList<Tank> activeTanks, TiledMap map){
        BP_IMG.drawFromTopLeft(0,0);

        // Reset addTank and addAirplane cache
        addTank = null;
        addAirplane = null;

        // Draw purchasable items and prices
        TANK_IMG.draw(TANK_OFFSETX, BUY_ICON_OFFSET_Y);
        this.setBColour(T_PRICE);
        PRICE_FONT.drawString(T_PRICE_S, TANK_OFFSETX + PRICE_MIDOFFSET_X, PRICE_OFFSET_Y, bColour);
        STANK_IMG.draw(STANK_OFFSETX, BUY_ICON_OFFSET_Y);
        this.setBColour(ST_PRICE);
        PRICE_FONT.drawString(ST_PRICE_S, STANK_OFFSETX + PRICE_MIDOFFSET_X, PRICE_OFFSET_Y, bColour);
        AP_IMG.draw(AP_OFFSETX, BUY_ICON_OFFSET_Y);
        this.setBColour(AP_PRICE);
        PRICE_FONT.drawString(AP_PRICE_S, AP_OFFSETX + PRICE_MIDOFFSET_X, PRICE_OFFSET_Y, bColour);
        // Draw keybinds
        KEYBIND_FONT.drawString(KEY_BIND_TEXT, MIDDLE_X + KEYB_MIDOFFSET_X, KEYB_OFFSET_Y);
        // Draw money
        String moneyString = "$"+money;
        MONEY_FONT.drawString(moneyString, WINDOW_X + MONEY_OFFSET_X, MONEY_OFFSET_Y);

        // Placing logic
        if(isPlacing){
            this.updatePlacing(input, map, activeTanks);
        }
        // Depending on the buy icon pressed, update the image and price to display/deduct when placed
        if(input.wasPressed(MouseButtons.LEFT)){
            Point tankP = new Point(TANK_OFFSETX, BUY_ICON_OFFSET_Y);
            Point sTankP = new Point(STANK_OFFSETX, BUY_ICON_OFFSET_Y);
            Point AP_P = new Point(AP_OFFSETX, BUY_ICON_OFFSET_Y);
            if(TANK_IMG.getBoundingBoxAt(tankP).intersects(input.getMousePosition()) && money >= T_PRICE){
                isPlacing = true;
                placingImage = TANK_IMG;
                placingPrice = T_PRICE;
            }
            else if(STANK_IMG.getBoundingBoxAt(sTankP).intersects(input.getMousePosition()) && money >= ST_PRICE){
                isPlacing = true;
                placingImage = STANK_IMG;
                placingPrice = ST_PRICE;
            }
            else if(AP_IMG.getBoundingBoxAt(AP_P).intersects(input.getMousePosition()) && money >= AP_PRICE){
                isPlacing = true;
                placingImage = AP_IMG;
                placingPrice = AP_PRICE;
            }
        }
    }

    /**
     * Update to be run when placing is happening
     * @param input allows the placingImg to be drawn on the cursor position
     * @param map used to check if the placing position is blocked by a blocked tile
     * @param activeTanks used to check if the placing position is blocked by a tank
     */
    public void updatePlacing(Input input, TiledMap map, ArrayList<Tank> activeTanks){
        // Use placingImage to determine which object to create
        // Allow airplanes to be placed/shown over blocked areas, but tanks/stanks not.
        boolean intersectingTank = false;
        for(Tank tank:activeTanks){
            intersectingTank = tank.getBoundingBox().intersects(input.getMousePosition());
        }
        if((!map.getPropertyBoolean((int)input.getMouseX(), (int)input.getMouseY(), "blocked",
                false) && !(intersectingTank)) || placingImage.equals(AP_IMG) ){
            placingImage.draw(input.getMouseX(), input.getMouseY());
            if(input.wasPressed(MouseButtons.LEFT)){
                if(placingImage.equals(TANK_IMG)){
                    addTank = new RegTank(input.getMousePosition());
                }
                else if(placingImage.equals(STANK_IMG)){
                    addTank = new SuperTank(input.getMousePosition());
                }
                else{
                    addAirplane = new Airplane(input.getMousePosition());
                }
                money = money - placingPrice;
                isPlacing = false;
            }
        }
        // Cancel the placing if right mouse button is clicked
        if(input.wasPressed(MouseButtons.RIGHT)){
            isPlacing = false;
        }
    }
}
