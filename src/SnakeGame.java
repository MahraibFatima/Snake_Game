import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private static class Tile{
        int x,y;
        Tile(int x, int y){
            this.x= x;
            this.y= y;
        }
    }

    int boardWidth,boardHeight;
    int tileSize= 25;

    //for snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    //food -> target
    Tile snakeFood;
    Random random;

    //game tracker
    boolean gameOver= false;
    Timer gameLoop;

    //positions
    int velocityX;
    int velocityY;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth= boardWidth;
        this.boardHeight= boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead= new Tile(5,5);
        snakeBody= new ArrayList<Tile>();
        snakeFood= new Tile(10, 10);
        random= new Random();
        placeFood();
        velocityX= 1;
        velocityY= 0;

        gameLoop= new Timer(100, this);
        gameLoop.start();

    }

    public void placeFood(){
        snakeFood.x= random.nextInt(boardWidth/tileSize);
        snakeFood.y= random.nextInt(boardWidth/tileSize);
    }

    public void move(){
        if(collision(snakeHead, snakeFood)){
            snakeBody.add(new Tile(snakeFood.x, snakeFood.y));
            placeFood();
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        for(int i= 0; i<boardWidth/tileSize; ++i){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
            g.setColor(Color.red);
            g.fill3DRect(snakeFood.x*tileSize,snakeFood.y*tileSize,tileSize, tileSize, true);

            g.setColor(Color.green);
            g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);


        }
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}