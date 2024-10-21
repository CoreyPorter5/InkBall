package inkball;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.*;


import processing.core.PApplet;
import processing.core.PImage;




public class Board {

    public static final int BALLRADIUS = 12;

    private String layoutPath;
    private char[][] layout;
    private ArrayList<Hole> holes;
    private ArrayList<Ball> balls;
    private ArrayList<Ball> ballsToRemove;
    private ArrayList<Spawner> spawners;
    private ArrayList<Wall> walls;


    private HashMap<String, PImage> sprites;
    private App app;

    private CollisionHandler collisionHandler;

    public Board(String layoutPath, HashMap<String, PImage> sprites, App app) {
        //this.layoutPath = layoutPath;
        this.layoutPath = "level2.txt";
        this.layout = new char[18][18];
        this.holes = new ArrayList<>();
        this.balls = new ArrayList<>();
        this.ballsToRemove = new ArrayList<>();
        this.spawners = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.sprites = sprites;
        this.app = app;
        this.collisionHandler = new CollisionHandler(app, this);
        loadLayout(this.layoutPath);
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


        // Load the layout from the file and populate the layout array


    }

    public void display(PApplet app) {
        // Display the board based on the layout array
        walls.clear();
        spawners.clear();

        //Displays rest
        for (int row = 0; row < layout.length; row++) {
            for (int col = 0; col < layout[row].length; col++) {
                char tile = layout[row][col];
                PImage img = null;

                if (tile == 'h') {
                    continue;
                }

                if (tile == 'X') {
                    walls.add(new Wall(col, row, sprites.get("wall0"), this));
                } else if (tile == '1') {
                    walls.add(new Wall(col, row, sprites.get("wall1"), this));
                } else if (tile == '2') {
                    walls.add(new Wall(col, row, sprites.get("wall2"), this));
                } else if (tile == '3') {
                    walls.add(new Wall(col, row, sprites.get("wall3"), this));
                } else if (tile == '4') {
                    walls.add(new Wall(col, row, sprites.get("wall4"), this));
                } else if (tile == ' ' || tile == 'b') {
                    img = sprites.get("tile");
                    app.image(img, col * App.CELLSIZE, row * App.CELLSIZE + App.TOPBAR, App.CELLSIZE, App.CELLSIZE);

                } else if (tile == 'S') {
                    spawners.add(new Spawner(col, row, sprites.get("entrypoint"), this));
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


    }

    public ArrayList<Hole> getHoles() {
        return holes;
    }


    public void removeBall(Ball ball) {
        balls.remove(ball);
    }

    public void removeBalls() {
        balls.removeAll(ballsToRemove);
        ballsToRemove.clear();
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

    public void testForCollisions(Ball ball) {
        collisionHandler.testForCollisions(ball);
    }








}