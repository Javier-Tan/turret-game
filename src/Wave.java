import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Wave {
    // Constants
    private static final int TICKPERSEC = 60;

    // Variables
    private final int DEF_NEXT_INDEX = 1;
    private Enemy[] enemies; // Stores the enemies to spawn in order
    private int enemyAmt; // Stores the amount of enemies to spawn/are in the enemies array
    private int enemiesSpawned = 0; // Tracks enemies spawned (starting from 0)
    private boolean waveOver = false; //Check if the wave is over
    private ArrayList<String> waveSpecs;
    private boolean spawnEvent = false;
    private boolean delayEvent = false;
    private int eventNum = 0;
    private double spawnDelayTimer;
    private double delayTimer;
    private List<Point> polyline;
    private Point defaultStartPoint;
    private int currSpawnDelay;
    private ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private ArrayList<Tank> activeTanks = new ArrayList<>();
    private ArrayList<Airplane> activeAirplanes = new ArrayList<>();
    private int penalty = 0;
    private int reward = 0;

    /**
     * Constructor for the wave
     * @param waveSpecs contains all the events the wave needs to execute
     * @param polyline contains the polyline of the current level
     */
    public Wave(ArrayList<String> waveSpecs, List<Point> polyline){
        this.waveSpecs = waveSpecs;
        this.polyline = polyline;
        this.defaultStartPoint = polyline.get(0);
    }

    /**
     * @return if the wave is over
     */
    public boolean getWaveOver(){
        return waveOver;
    }

    /**
     * Handles initialisation of the upcoming spawn event
     * @param enemyAmt amount of enemies to spawn
     * @param enemyType type of enemy to spawn
     * @param spawnDelay delay between spawning
     */
    private void startSpawn(int enemyAmt, String enemyType, int spawnDelay){
        this.spawnEvent = true;
        this.enemyAmt = enemyAmt;
        this.enemiesSpawned = 0;
        enemies = new Enemy[enemyAmt];
        for(int i = 0; i < enemyAmt; i++){
            if(enemyType.equals("slicer")){
                enemies[i] = new RegSlicer(polyline, defaultStartPoint, DEF_NEXT_INDEX);
            }
            else if(enemyType.equals("superslicer")){
                enemies[i] = new SuperSlicer(polyline, defaultStartPoint, DEF_NEXT_INDEX, activeEnemies);
            }
            else if(enemyType.equals("megaslicer")){
                enemies[i] = new MegaSlicer(polyline, defaultStartPoint, DEF_NEXT_INDEX, activeEnemies);
            }
            else if(enemyType.equals("apexslicer")){
                enemies[i] = new ApexSlicer(polyline, defaultStartPoint, DEF_NEXT_INDEX, activeEnemies);
            }
        }
        spawnDelayTimer = currSpawnDelay*TICKPERSEC;
    }

    /**
     * Receives the list of active tanks every tick to ensure that any new placed tanks are included
     * @param activeTanks list of active tanks
     */
    public void recActiveTanks(ArrayList<Tank> activeTanks){
        this.activeTanks = activeTanks;
    }

    /**
     * handles initialisation of delayEvent
     * @param delayTime time of delayEvent
     */
    private void startDelay(int delayTime){
        this.delayEvent = true;
        this.delayTimer = delayTime*TICKPERSEC;
    }

    /**
     * @return the penalty to be applied for slicers leaving the map
     */
    public int getPenalty(){
        int tempPenalty = penalty;
        penalty = 0;
        return tempPenalty;
    }

    /**
     * @return the reward for any slicers that died before leaving the map
     */
    public int getReward(){
        int tempReward = reward;
        reward = 0;
        return tempReward;
    }

    /**
     * to add a plane to activeAirplanes so it will be used (updated)
     * @param plane the plane to add to activeAirplanes
     */
    public void addPlane(Airplane plane){
        activeAirplanes.add(plane);
    }

    /**
     * Updates which happens every tick
     * @param timeScale ensures that update is compliant with timeScale of the game
     */
    public void update(int timeScale){
        // Continue a spawn event
        if(spawnEvent){
            if (enemiesSpawned < enemyAmt) {
                // Spawn logic, slicersSpawned being incremented allows the next slicer in array to start moving
                if (spawnDelayTimer >= (currSpawnDelay * TICKPERSEC)) {
                    activeEnemies.add(enemies[enemiesSpawned]);
                    spawnDelayTimer = 0;
                    enemiesSpawned++;
                }
                spawnDelayTimer = spawnDelayTimer + timeScale;
            }
            else{
                spawnEvent = false;
            }
        }
        // Continue a delay event
        else if(delayEvent){
            if(delayTimer <= 0){
                this.delayEvent = false;
            }
            delayTimer = delayTimer - timeScale;
        }
        else{
            // Reads through the waveEvent and finds if it's a delay or spawn event, and initialises it
            if(eventNum < waveSpecs.size()){
                int commaIndex = waveSpecs.get(eventNum).indexOf(",");
                int commaIndex2 = waveSpecs.get(eventNum).indexOf(",", commaIndex+1);
                String eventType = waveSpecs.get(eventNum).substring(commaIndex+1, commaIndex2);
                if(eventType.equals("delay")){
                    int delayTime = Integer.parseInt(waveSpecs.get(eventNum).substring(commaIndex2+1))/1000;
                    this.startDelay(delayTime);
                }
                else{
                    commaIndex = waveSpecs.get(eventNum).indexOf(",", commaIndex2+1);
                    enemyAmt = Integer.parseInt(waveSpecs.get(eventNum).substring(commaIndex2+1, commaIndex));
                    commaIndex2 = waveSpecs.get(eventNum).indexOf(",", commaIndex+1);
                    String enemyType = waveSpecs.get(eventNum).substring(commaIndex+1, commaIndex2);
                    currSpawnDelay = Integer.parseInt(waveSpecs.get(eventNum).substring(commaIndex2+1))/1000;
                    this.startSpawn(enemyAmt, enemyType, currSpawnDelay);
                }
                eventNum += 1;
            }
            else{
                //Check if all activeEnemies are dead after all events have been completed.
                if(activeEnemies.size() == 0){
                    waveOver = true;
                }
            }
        }

        // Updates the position for all activeSlicers and checks if any have died.
        for(int i = 0; i < activeEnemies.size(); i++){
            if (!activeEnemies.get(i).isAlive()){
                if(activeEnemies.get(i).reachedEnd()) {
                    penalty = penalty + activeEnemies.get(i).getEntityPenalty();
                }
                else {
                    reward = reward + activeEnemies.get(i).getEntityReward();
                }
                activeEnemies.remove(i);
                i = i - 1;
            }
            else {
                activeEnemies.get(i).updatePosition(timeScale);
            }
        }


        // For updating towers
        for(Tank tank:activeTanks){
            tank.update(timeScale, activeEnemies);
        }

        // Updates the position for all activeAirplanes and checks if any are no longer active.
        for(int i = 0; i < activeAirplanes.size(); i++){
            if (!activeAirplanes.get(i).getIsActive()){
                activeAirplanes.remove(i);
                i = i - 1;
            }
            else {
                activeAirplanes.get(i).update(timeScale, activeEnemies);
            }
        }

    }
}
