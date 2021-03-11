import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.FileNotFoundException;
import java.util.List;

public class ShadowDefend extends AbstractGame{
    /* Constants (for project 2B) */
    private static final int LEVEL_AMT = 2;
    /* Initialise resources */
    private Level[] levels = new Level[LEVEL_AMT];
    private int currLevel = 0;
    private int timeScale = 1; // Timescale (1 by default)


    /**
     * Entry point for Bagel game
     *
     * Explore the capabilities of Bagel: https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Create new instance of ShadowDefend and run it
        new ShadowDefend().run();
    }

    /**
     * Setup ShadowDefend
     */
    public ShadowDefend() throws FileNotFoundException {
        // Map that will be used in level 1
        TiledMap map1 = new TiledMap("./res/levels/1.tmx");
        TiledMap map2 = new TiledMap("./res/levels/2.tmx");
        //Initialise levels
        levels[0] = new Level(map1);
        levels[1] = new Level(map2);
    }

    /**
     * Updates the game state approximately 60 times a second, potentially reading from input.
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {

        // Start the wave on the press of 'S'
        if (input.wasPressed(Keys.S)){
            levels[currLevel].startWave();
        }

        // Check if L and K are pressed, if they are and the timescale allows for it, increase/decrease by a
        // factor of one respectively and adjust timeScale accordingly
        if (input.wasPressed(Keys.L)){
            if (timeScale < 5) {
                timeScale = timeScale + 1;
            }
        }
        if (input.wasPressed(Keys.K)){
            if (timeScale > 1){
                timeScale = timeScale - 1;
            }
        }

        // If there are still levels to play
        if(currLevel < LEVEL_AMT){
            // Update the level
            levels[currLevel].update(timeScale, input);
            //Check if the level is over and attempt to move to the next level
            if(levels[currLevel].getLevelOver()){
                currLevel = currLevel + 1;
            }

        }
        // Otherwise all levels are finished, close the window.
        else {
            Window.close();
        }

    }
}
