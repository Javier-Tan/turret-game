import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

import java.util.ArrayList;

public class StatusPanel {
    private final static Image SP_Img = new Image("./res/images/statuspanel.png");
    private static final int WINDOW_X = 1024;
    private static final int WINDOW_Y = 768;
    private static final int STATUS_MSG_AMT = 4;
    private static String[] statusMsg = new String[STATUS_MSG_AMT];
    private final static int FONT_SIZE = 18;
    private final static Font FONT  = new Font("./res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);
    private final static String WAVE = "Wave: ";
    private final static int WAVE_OFFSET_X = 5;
    private final static int OFFSET_Y = WINDOW_Y - 5;
    private final static String TIMESCALE_S = "Time Scale: ";
    private final static int TS_OFFSET_X = WINDOW_X/6;
    private final static DrawOptions dColour = new DrawOptions();
    private final static String STATUS_S = "Status: ";
    private final static int STATUS_OFFSET_X = 2*WINDOW_X/5;
    private final static String LIVES_S = "Lives: ";
    private final static int LIVES_OFFSET_X = 5*WINDOW_X/6;

    /**
     * Constructor for the status panel
     */
    public StatusPanel(){
        statusMsg[0] = "Awaiting Start";
        statusMsg[1] = "Wave In Progress";
        statusMsg[2] = "Placing";
        statusMsg[3] = "Winner!";
    }

    /**
     * update of the status panel which happens every tick
     * @param lives amount of lives to display
     * @param statusNo determines which status to display
     * @param timeScale the timeScale to display
     * @param currWave the wave number to display
     */
    public void update(int lives, int statusNo, int timeScale, int currWave){
        // By default, set timescale font colour to white
        dColour.setBlendColour(Colour.WHITE);
        // Draw all the necessary details
        SP_Img.drawFromTopLeft(0, WINDOW_Y - SP_Img.getHeight());
        String currStatus = statusMsg[statusNo];
        FONT.drawString(WAVE + (currWave+1), WAVE_OFFSET_X, OFFSET_Y);
        // If timescale > 1 (!= 1 is equivalent), make the timescale font green
        if(timeScale != 1){
            dColour.setBlendColour(Colour.GREEN);
        }
        FONT.drawString(TIMESCALE_S + timeScale+".0", TS_OFFSET_X, OFFSET_Y, dColour);
        FONT.drawString(STATUS_S + currStatus, STATUS_OFFSET_X, OFFSET_Y);
        FONT.drawString(LIVES_S + lives, LIVES_OFFSET_X, OFFSET_Y);
    }
}
