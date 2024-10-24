package inkball;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;


import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import static processing.core.PApplet.loadJSONObject;


public class Board {

    public static final int BALLRADIUS = 12;

    private String layoutPath;
    private char[][] layout;
    private ArrayList<Hole> holes;
    private ArrayList<Ball> balls;
    private ArrayList<Ball> ballsToRemove;
    private ArrayList<Spawner> spawners;
    private ArrayList<Wall> walls;
    private List<Brick> bricks;


    private HashMap<String, PImage> sprites;
    private App app;

    private CollisionHandler collisionHandler;

    public Board(String layoutPath, HashMap<String, PImage> sprites, App app) {
        if (layoutPath == null) {
            System.out.println("layoutPath is null");
        }
        if (sprites == null) {
            System.out.println("sprites is null");
        }
        if (app == null) {
            System.out.println("app is null");
        }
        this.layoutPath = layoutPath;
        this.layout = new char[18][18];
        this.holes = new ArrayList<>();
        this.balls = new ArrayList<>();
        this.ballsToRemove = new ArrayList<>();
        this.spawners = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.sprites = sprites;
        this.app = app;
        this.collisionHandler = new CollisionHandler(app, this);
        this.bricks = new ArrayList<>();
        loadLayout(this.layoutPath);
        loadBricks();
    }

    private void loadLayout(String layoutPath) {


        try {
            File layout_file = new File(layoutPath);
            Scanner scan = new Scanner(layout_file);
            int row = 0;

            while (scan.hasNext() && row < layout.length) {
                char[] line = scan.nextLine().toCharArray();
                for (int col = 0; col < line.length && col < layout[row].length; col++) {
                    if (layout[row][col] != 'h' && layout[row][col] != 'b') {
                        layout[row][col] = line[col];
                    }
                    if (line[col] == 'H' && col + 1 < line.length && row + 1 < layout.length) {
                        int colour = Character.getNumericValue(line[col + 1]);
                        holes.add(new Hole(col, row, colour, this));
                        layout[row][col] = 'h';
                        layout[row][col + 1] = 'h';
                        layout[row + 1][col] = 'h';
                        layout[row + 1][col + 1] = 'h';


                    } else if (line[col] == 'B' && col + 1 < line.length) {
                        int colour = Character.getNumericValue(line[col + 1]);
                        balls.add(new Ball(col, row, colour, this, app));
                        layout[row][col] = 'b';
                        layout[row][col + 1] = 'b';
                    }

                }
                row++;


            }
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                char tile = layout[row][col];

                if (tile == 'h' || tile == 'b') {
                    continue;
                }

                if (tile == 'X') {
                    walls.add(new Wall(col, row, sprites.get("wall0"), this, 0));
                } else if (tile == '1') {
                    walls.add(new Wall(col, row, sprites.get("wall1"), this, 1));
                } else if (tile == '2') {
                    walls.add(new Wall(col, row, sprites.get("wall2"), this, 2));
                } else if (tile == '3') {
                    walls.add(new Wall(col, row, sprites.get("wall3"), this, 3));
                } else if (tile == '4') {
                    walls.add(new Wall(col, row, sprites.get("wall4"), this, 4));
                } else if (tile == 'S') {
                    spawners.add(new Spawner(col, row, sprites.get("entrypoint"), this));
                }
            }
        }






        // Load the layout from the file and populate the layout array


    }

    public void display(PApplet app) {
        // Display the board based on the layout array


        //Displays rest
        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                char tile = layout[row][col];
                PImage img = null;

                if (tile == 'h') {
                    continue;
                }


                else if (tile == ' ' || tile == 'b') {
                    img = sprites.get("tile");
                    app.image(img, col * App.CELLSIZE, row * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);

                }


            }
        }
        //display holes
        for (Hole hole : holes) {
            hole.display(app);
        }

        //Display walls
        for (Wall wall : walls) {
            wall.display(app);
        }

        for (Spawner spawner : spawners) {
            spawner.display(app);
        }

        for (Brick brick : bricks) {
            brick.display(app);
        }


    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }



    public PImage getHoleImage(int colour) {
        return sprites.get("hole" + colour);
    }

    public PImage getBallImage(int colour) {
        return sprites.get("ball" + colour);
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public ArrayList<Spawner> getSpawners() {
        return spawners;
    }

    public void addBall(Ball ball) {
        balls.add(ball);
    }

    public void testForCollisions(Ball ball) {
        collisionHandler.testForCollisions(ball);
    }

    private void loadBricks() {
        JSONObject config = loadJSONObject(new File("config.json"));
        JSONArray levels = config.getJSONArray("levels");
        for (int i = 0; i < levels.size(); i++) {
            JSONObject level = levels.getJSONObject(i);
            if (level.hasKey("bricks")) {
                JSONArray bricksConfig = level.getJSONArray("bricks");
                for (int j = 0; j < bricksConfig.size(); j++) {
                    JSONObject brickConfig = bricksConfig.getJSONObject(j);
                    int x = brickConfig.getInt("x");
                    int y = brickConfig.getInt("y");
                    int colour = brickConfig.getInt("colour");
                    int maxHits = brickConfig.getInt("maxHits");
                    PImage normalBrick = sprites.get("wall" + colour);
                    PImage crackedBrick = sprites.get("wall" + (colour + 5));
                    bricks.add(new Brick(x, y, normalBrick, crackedBrick, this, colour, maxHits));
                }
            }
        }
    }

    public List<Brick> getBricks(){
        return bricks;
    }

    public void removeBrick(Brick brick){
        bricks.remove(brick);
    }










}