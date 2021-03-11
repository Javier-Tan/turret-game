import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Level {

    // Variables
    private static final int WINDOW_X = 1024;
    private static final int WINDOW_Y = 768;
    private TiledMap map; // The map for this level
    private boolean waveOn = false; // Checks if the wave is on
    private int currWave = 0; // Stores which wave the player is currently on
    private boolean levelOver = false; // Check if the level is over
    private int waveAmt;
    private Wave[] waves;
    private final File waveSpecs = new File("./res/levels/waves.txt");
    private ArrayList<ArrayList<String>> waveEvents = new ArrayList<>();
    private List<Point> polyline;
    private ArrayList<Tank> activeTanks = new ArrayList<>();
    private BuyPanel buyPanel = new BuyPanel();
    private StatusPanel statusPanel = new StatusPanel();
    private int lives = 25; // TEST
    private int statusTracker = 0;


    /**
     * Helper function to extract wave information from wave.txt
     * @throws FileNotFoundException
     */
    private void insertWaveInfo() throws FileNotFoundException {
        Scanner waveReader = new Scanner(waveSpecs);
        while (waveReader.hasNextLine()){
            String data = waveReader.nextLine();
            int waveDataNum = Integer.parseInt(data.substring(0,1));
            if(waveDataNum > waveEvents.size()){
                ArrayList<String> AList = new ArrayList<>();
                waveEvents.add(waveDataNum - 1, AList);
                waveAmt = waveDataNum;
            }
            waveEvents.get(waveDataNum-1).add(data);
        }
        waves = new Wave[waveAmt];
        for(int i = 0; i < waveAmt; i++){
            waves[i] = new Wave(waveEvents.get(i), polyline);
        }
    }

    /**
     * Constructor for a level
     * @param map the map of the level
     * @throws FileNotFoundException
     */
    public Level(TiledMap map) throws FileNotFoundException {
        this.map = map;
        this.polyline = map.getAllPolylines().get(0);
        this.insertWaveInfo();
    }

    /**
     * Updates the map every tick depending on the gameState (waveOn)
     * @param timeScale ensures that the level is being updated according to the timeScale
     * @param input allows the level to interact with user input
     */
    public void update(int timeScale, Input input) {
        statusTracker = 0;
        map.draw(0, 0, 0, 0, WINDOW_X, WINDOW_Y);
        if (currWave < waveAmt) {
            // If the wave is active, update the wave
            if (waveOn) {
                statusTracker = 1;
                // Update the wave
                waves[currWave].recActiveTanks(activeTanks);
                waves[currWave].update(timeScale);
                lives = lives - waves[currWave].getPenalty();
                buyPanel.addMoney(waves[currWave].getReward());
                // Check if the wave is over
                if (waves[currWave].getWaveOver()) {
                    buyPanel.addMoney(150+(currWave+1)*100);
                    currWave = currWave + 1;
                    waveOn = false;
                }
            }
            else{
                // If the wave is not active, still draw the tanks
                for(Tank tank:activeTanks){
                    tank.draw();
                }
            }
        }
        // If there are no more waves, the level is over
        else {
            levelOver = true;
        }

        //Buy and status panel updating
        buyPanel.update(input, activeTanks, map);
        // If there is a tank in the addTank cache (not null), add it to the activeTanks
        if(buyPanel.addTank() != null){
            activeTanks.add(buyPanel.addTank());
        }
        // Similarly for addAirplane cache
        if(buyPanel.addAirplane() != null){
            waves[currWave].addPlane(buyPanel.addAirplane());
        }
        // Status check
        if(buyPanel.isPlacing()){
            statusTracker = 2;
        }
        statusPanel.update(lives, statusTracker, timeScale, currWave);

        // End the game if lives less than 0
        if(lives <= 0){
            Window.close();
        }
    }

    /**
     * Starts the wave is the wave is not already started
     */
    public void startWave(){
        if(!waveOn) {
            waveOn = true;
        }
    }

    /**
     * @return the levelOver tag
     */
    public boolean getLevelOver(){
        statusTracker = 3;
        return levelOver;
    }

}
