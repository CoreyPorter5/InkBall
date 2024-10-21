package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 1;

    public static final int FPS = 30;

    private String configPath;
    private int currentLevel;
    private JSONArray levels;
    private Board board;
    //private Score score;
    //private TimerClock timer;

    public List<PlayerLine> playerLines;
    private PlayerLine currentLine;

    private HashMap<String, PImage> sprites = new HashMap<>();

    public static Random random = new Random();
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
        this.currentLevel = 0;
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);

        playerLines = new ArrayList<>();


        String[] spriteNames = new String[]{
                "ball0",
                "ball1",
                "ball2",
                "ball3",
                "ball4",
                "entrypoint",
                "hole0",
                "hole1",
                "hole2",
                "hole3",
                "hole4",
                "tile",
                "wall0",
                "wall1",
                "wall2",
                "wall3",
                "wall4"
        };

        for (String sprite : spriteNames) {
            PImage img = loadImage("inkball/" + sprite + ".png");
            if (img == null) {
                System.err.println("Error loading image: " + sprite);
            } else {
                sprites.put(sprite, img);
            }
        }


        loadLevelData();











		//See PApplet javadoc:











		// the image is loaded from relative path: "src/main/resources/inkball/..."
		/*try {
            result = loadImage(URLDecoder.decode(this.getClass().getResource(filename+".png").getPath(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }*/
    }

    private void loadLevelData(){
        JSONObject config = loadJSONObject(configPath);
        levels = config.getJSONArray("levels");
        board = new Board(levels.getJSONObject(currentLevel).getString("layout"), this.sprites, this);


        //score = new Score();
        //timer = new TimerClock(levels.getJSONObject(currentLevel).getInt("time"));



        int score_increase_from_hole_capture_modifier = levels.getJSONObject(currentLevel).getInt("score_increase_from_hole_capture_modifier");
        int score_decrease_from_wrong_hole_modifier = levels.getJSONObject(currentLevel).getInt("score_decrease_from_wrong_hole_modifier");
        ArrayList<String> ballList = new ArrayList<>();

        int num_of_balls = levels.getJSONObject(currentLevel).getJSONArray("balls").size();

        for(int i = 0; i < num_of_balls; i++){
            ballList.add(levels.getJSONObject(currentLevel).getJSONArray("balls").getString(i));
        }

    }

    public void updateLevel(){
        if(currentLevel < levels.size() - 1) {
            currentLevel++;
            loadLevelData();
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if(event.getKey() == ' '){
            System.out.println("Space pressed");
        }

        else if(event.getKey() == 'r'){
            loadLevelData();
            System.out.println("R pressed");
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){

        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == LEFT){
            currentLine = new PlayerLine();
            currentLine.addPoint(e.getX(), e.getY());
            playerLines.add(currentLine);


        }


    }
	
	@Override
    public void mouseDragged(MouseEvent e) {
        if(e.getButton() == LEFT && currentLine != null){
            currentLine.addPoint(e.getX(), e.getY());
        }

        // add line segments to player-drawn line object if left mouse button is held
		
		// remove player-drawn line object if right mouse button is held 
		// and mouse position collides with the line
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == LEFT && e.isControlDown()) {
            for (int i = 0; i < playerLines.size(); i++) {
                PlayerLine line = playerLines.get(i);
                List<PVector> points = line.getPoints();
                for (int j = 0; j < points.size() - 1; j++) {
                    PVector p1 = points.get(j);
                    PVector p2 = points.get(j + 1);
                    if (line.mouseCollideWithLine(e.getX(), e.getY(), (int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y)) {
                        playerLines.remove(i);
                        break;
                    }
                }
            }
        } else if (e.getButton() == LEFT) {
            currentLine = null;
        } else if (e.getButton() == RIGHT) {
            for (int i = 0; i < playerLines.size(); i++) {
                PlayerLine line = playerLines.get(i);
                List<PVector> points = line.getPoints();
                for (int j = 0; j < points.size() - 1; j++) {
                    PVector p1 = points.get(j);
                    PVector p2 = points.get(j + 1);
                    if (line.mouseCollideWithLine(e.getX(), e.getY(), (int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y)) {
                        playerLines.remove(i);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        board.display(this);







        stroke(0);
        strokeWeight(10);
        for (PlayerLine line : playerLines) {
            List<PVector> points = line.getPoints();
            for (int i = 0; i < points.size() - 1; i++) {
                PVector p1 = points.get(i);
                PVector p2 = points.get(i + 1);
                if(p1.y > TOPBAR + 4 && p2.y > TOPBAR + 4){
                    line(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
        Iterator<Ball> ballIterator = board.getBalls().iterator();
        while (ballIterator.hasNext()) {
            Ball ball = ballIterator.next();
            ball.update();
            ball.display(this);
            if(ball.shouldBeRemoved()){
                ballIterator.remove();
            }
        }



        //score.display();
        //timer.display();
        //game_over.display();

        //updateable interface -> score, balls, ball tracker, time

        

        //----------------------------------
        //display Board for current level: //Includes GUI
        //----------------------------------
        //TODO

        //----------------------------------
        //display score
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------
		//display game end message

    }


    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}


