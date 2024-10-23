package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import java.util.*;

public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;


    public static final int TOPBAR = 64;
    public static int WIDTH = 576; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;


    private boolean paused = false;
    protected boolean levelWin = false;



    public static final int FPS = 30;

    private String configPath;
    private JSONObject config;
    private int currentLevel;
    private JSONArray levels;
    private Board board;
    public int score;
    private int levelStartScore;
    protected Timer timer;
    private BallQueue ballQueue;
    private ScoreBoard scoreBoard;
    private int spawnInterval;
    private float countdown;

    private float scoreIncreaseModifier;
    private float scoreDecreaseModifier;

    private static final int[] SCORE_INCREASE = new int[5];
    private static final int[] SCORE_DECREASE = new int[5];

    public List<PlayerLine> playerLines;
    private List<YellowTile> yellowTiles;
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
        config = loadJSONObject(configPath);

        playerLines = new ArrayList<>();
        yellowTiles = new ArrayList<>();



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
                "wall4",
                "wall5",
                "wall6",
                "wall7",
                "wall8",
                "wall9"
        };

        for (String sprite : spriteNames) {
            PImage img = loadImage("inkball/" + sprite + ".png");
            if (img == null) {
                System.err.println("Error loading image: " + sprite);
            } else {
                sprites.put(sprite, img);
            }
        }

        scoreBoard = new ScoreBoard(this);
        loadLevelData();
        countdown = spawnInterval * App.FPS;

    }

    public PImage getBallImage(String ballColor){
        switch (ballColor) {
            case "grey":
                return sprites.get("ball0");
            case "orange":
                return sprites.get("ball1");
            case "blue":
                return sprites.get("ball2");
            case "green":
                return sprites.get("ball3");
            case "yellow":
                return sprites.get("ball4");
            default:
                return null;
        }
    }

    public JSONObject getConfig(){
        return config;
    }


    private void loadLevelData(){
        JSONObject config = loadJSONObject(configPath);
        levels = config.getJSONArray("levels");
        board = new Board(levels.getJSONObject(currentLevel).getString("layout"), this.sprites, this);
        spawnInterval = levels.getJSONObject(currentLevel).getInt("spawn_interval");


        timer = new Timer(levels.getJSONObject(currentLevel).getInt("time"), this);



        scoreIncreaseModifier = levels.getJSONObject(currentLevel).getInt("score_increase_from_hole_capture_modifier");
        scoreDecreaseModifier = levels.getJSONObject(currentLevel).getInt("score_decrease_from_wrong_hole_modifier");

        JSONObject scoreIncreaseConfig = config.getJSONObject("score_increase_from_hole_capture");
        SCORE_INCREASE[0] = scoreIncreaseConfig.getInt("grey");
        SCORE_INCREASE[1] = scoreIncreaseConfig.getInt("orange");
        SCORE_INCREASE[2] = scoreIncreaseConfig.getInt("blue");
        SCORE_INCREASE[3] = scoreIncreaseConfig.getInt("green");
        SCORE_INCREASE[4] = scoreIncreaseConfig.getInt("yellow");

        JSONObject scoreDecreaseConfig = config.getJSONObject("score_decrease_from_wrong_hole");
        SCORE_DECREASE[0] = scoreDecreaseConfig.getInt("grey");
        SCORE_DECREASE[1] = scoreDecreaseConfig.getInt("orange");
        SCORE_DECREASE[2] = scoreDecreaseConfig.getInt("blue");
        SCORE_DECREASE[3] = scoreDecreaseConfig.getInt("green");
        SCORE_DECREASE[4] = scoreDecreaseConfig.getInt("yellow");


        ArrayList<String> ballQueueList = new ArrayList<>();
        JSONArray balls = levels.getJSONObject(currentLevel).getJSONArray("balls");


        for (int i = 0; i < balls.size(); i++) {
            String ballColor = balls.getString(i);
            switch (ballColor) {
                case "grey":
                    ballQueueList.add("grey");
                    break;
                case "orange":
                    ballQueueList.add(("orange"));
                    break;
                case "blue":
                    ballQueueList.add("blue");
                    break;
                case "green":
                    ballQueueList.add("green");
                    break;
                case "yellow":
                    ballQueueList.add("yellow");
                    break;
            }
        }

        ballQueue = new BallQueue(ballQueueList, this);
        spawnBall();

        levelStartScore = score;


    }

    public void increaseScore(int holeColour){
        score += (int) (SCORE_INCREASE[holeColour] * scoreIncreaseModifier);

    }

    public void decreaseScore(int holeColour){
        score -= (int) (SCORE_DECREASE[holeColour] * scoreDecreaseModifier);

    }

    public void requeueBall(Ball ball){
        String ballColour = getBallColourString(ball.getColour());
        ballQueue.enqueue(ballColour);
    }

    private String getBallColourString(int color) {
        switch (color) {
            case 0:
                return "grey";
            case 1:
                return "orange";
            case 2:
                return "blue";
            case 3:
                return "green";
            case 4:
                return "yellow";
            default:
                return null;
        }
    }

    public void updateLevel(){
        if(currentLevel < levels.size() - 1) {
            levelWin = false;
            currentLevel++;
            loadLevelData();
            countdown = spawnInterval * App.FPS;
            playerLines.clear();
            yellowTiles.clear();
        }
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if(event.getKey() == ' '){
            paused = !paused;
            timer.setPaused(paused);
        }

        else if(event.getKey() == 'r'){
            if(currentLevel < levels.size() - 1) {
                restartLevel();
            }else{
                restartGame();
            }
        }
        
    }

    private void restartLevel(){
        levelWin = false;
        score = levelStartScore;
        loadLevelData();
        countdown = spawnInterval * App.FPS;
        playerLines.clear();
        yellowTiles.clear();
    }

    private void restartGame(){
        currentLevel = 0;
        score = 0;
        restartLevel();
    }



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


	@Override
    public void draw() {

        board.display(this);

        if(!paused && timer.getTime() > 0) {
            fill(205, 204, 205);
            noStroke();
            rect(250, 20, 170, 30);
            countdown--;
            if(countdown <= 0){
                spawnBall();
                countdown = spawnInterval * App.FPS;
            }
            displayCountdown();

            Iterator<Ball> ballIterator = board.getBalls().iterator();
            while (ballIterator.hasNext()) {
                Ball ball = ballIterator.next();
                ball.update();
                ball.display(this);
                if(ball.shouldBeRemoved()){
                    ballIterator.remove();
                }
            }


        }else if(timer.getTime() <= 0){
            displayTimesUp();
            for(Ball ball : board.getBalls()){
                ball.display(this);
            }
        }

        else {
            displayPausedMessage();
            for(Ball ball : board.getBalls()){
                ball.display(this);
            }
        }

        ballQueue.displayNextBalls();
        scoreBoard.display();
        timer.update();
        timer.display();

        if(board.getBalls().isEmpty() && ballQueue.isEmpty() && !levelWin){
            levelWin = true;
            scoreBoard.addTimeToScore(timer.getTime());
            initaliseYellowTiles();


        }else if(timer.getTime() <=0 && !levelWin){
            displayTimesUp();

        }

        if(levelWin){
            scoreBoard.updateScoreWithTime();
            updateAndDisplayYellowTiles();

        }

        if(currentLevel == levels.size() - 1 && levelWin && timer.getTime() <= 0){
            if(!yellowTiles.isEmpty()){
                yellowTiles.clear();
            }
            fill(205, 204, 205);
            noStroke();
            rect(250, 20, 177, 30);
            textSize(21);
            fill(0);
            text("===ENDED===", 255, 40);

        }




        if(timer.getTime() > 0 && !levelWin) {
            stroke(0);
            strokeWeight(10);
            for (PlayerLine line : playerLines) {
                List<PVector> points = line.getPoints();
                for (int i = 0; i < points.size() - 1; i++) {
                    PVector p1 = points.get(i);
                    PVector p2 = points.get(i + 1);
                    if (p1.y > TOPBAR + 4 && p2.y > TOPBAR + 4) {
                        line(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }


    }

    private void displayPausedMessage() {
        fill(205, 204, 205);
        noStroke();
        rect(250, 20, 177, 30);
        textSize(21);
        fill(0);
        text("*** PAUSED ***", 255, 40);
    }

    private void displayTimesUp(){
        fill(205, 204, 205);
        noStroke();
        rect(250, 20, 177, 30);
        textSize(21);
        fill(0);
        text("===TIME'S UP===", 255, 40);
    }

    protected void spawnBall(){
        int ballColourNum;
        if(!ballQueue.isEmpty()){
            String ballColour = ballQueue.dequeue();
            if(ballColour.equals("grey")){
                ballColourNum = 0;
            } else if(ballColour.equals("orange")){
                ballColourNum = 1;
            } else if(ballColour.equals("blue")){
                ballColourNum = 2;
            } else if(ballColour.equals("green")){
                ballColourNum = 3;
            } else {
                ballColourNum = 4;
            }


            if(!board.getSpawners().isEmpty()) {

                Random random = new Random();
                int randomSpawner = random.nextInt(board.getSpawners().size());


                board.addBall(new Ball((int) board.getSpawners().get(randomSpawner).getX()/App.CELLSIZE, ((int) board.getSpawners().get(randomSpawner).getY() - App.TOPBAR)/App.CELLSIZE, ballColourNum, board, this));
            }
        }
    }

    private void displayCountdown(){
        if(!ballQueue.isEmpty()){
            fill(205, 204, 205);
            noStroke();
            rect(180, 20, 60, 25);
            textSize(21);
            fill(0);
            float roundedCountdown = Math.round(countdown/FPS * 10) / 10.0f;
            String formattedCountdown = String.format("%.1f", roundedCountdown);
            text((formattedCountdown), 190, 42);

        }else{
            fill(205, 204, 205);
            noStroke();
            rect(180, 20, 60, 25);
            fill(0);
            countdown = spawnInterval * FPS;
        }


    }

    private void initaliseYellowTiles(){
        PImage yellowWall = sprites.get("wall4");
        yellowTiles.add(new YellowTile(0, App.TOPBAR, yellowWall, this));
        yellowTiles.add(new YellowTile(width - CELLSIZE, height - CELLSIZE, yellowWall, this));
    }

    private void updateAndDisplayYellowTiles(){
        for(YellowTile yellowTile : yellowTiles) {
            yellowTile.update();
            yellowTile.display();
        }
        if(timer.getTime() <= 0) {
            updateLevel();
        }
    }



    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

}


