package inkball;

public class ScoreBoard {


    private int score;
    private App app;

    public ScoreBoard(int score, App app) {
        this.score = score;
        this.app = app;
    }

    public void update(int score){
        this.score = score;
    }

    public void display(){
        app.fill(205, 204, 205);
        app.noStroke();
        app.rect(455, 12, 150, 30);
        app.textSize(21);
        app.fill(0);
        app.text("Score:  " + score, 455, 30);
    }
}
