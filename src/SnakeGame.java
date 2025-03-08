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

    //positions
    int velocityX;
    int velocityY;
    //game tracker
    boolean gameOver= false;
    boolean paused= false;
    boolean hasMoved= false;
    Timer gameLoop;


    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth= boardWidth;
        this.boardHeight= boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.darkGray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead= new Tile(5,5);
        snakeBody= new ArrayList<>();
        snakeFood= new Tile(10, 10);
        random= new Random();
        placeFood();
        velocityX= 0;
        velocityY= 0;

        gameLoop= new Timer(250, this);//delay= 250 -> slow move
        gameLoop.start();

    }

    public void placeFood(){
        snakeFood.x= random.nextInt(boardWidth/tileSize);
        snakeFood.y= random.nextInt(boardWidth/tileSize);
    }

    public void move(){
        //eat food
        if(collision(snakeHead, snakeFood)){
            snakeBody.add(new Tile(snakeFood.x, snakeFood.y));
            placeFood();
        }

        //move snake body
        for(int i= snakeBody.size()-1; i>=0; --i){
            Tile currSnakePart = snakeBody.get(i);
            if(i==0){
                currSnakePart.x= snakeHead.x;
                currSnakePart.y= snakeHead.y;
            }else{
                Tile preSnakePart = snakeBody.get(i-1);
                currSnakePart.x= preSnakePart.x;
                currSnakePart.y= preSnakePart.y;
            }

        }
        //move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
        // reset after move
        hasMoved = false;

        // game over conditions
        for (Tile snakePart : snakeBody) {
            //collision with snake head
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }

        }// collision with wall
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth ||
                snakeHead.y< 0 || snakeHead.y >= boardHeight) {
            gameOver = true;
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.setColor(Color.gray);
        for(int i= 0; i<boardWidth/tileSize; ++i) {
            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
        }
        //food
        g.setColor(Color.orange);
        g.fill3DRect(snakeFood.x*tileSize,snakeFood.y*tileSize,tileSize, tileSize, true);
        //snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);
        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 16));

        for(Tile segment: snakeBody){
            g.fill3DRect(segment.x*tileSize, segment.y*tileSize, tileSize, tileSize, true);
        }

        if(gameOver){
            g.setColor(Color.red);
            g.drawString("Game over: "+snakeBody.size(), tileSize-16, tileSize);
        }else if (paused) {
            g.drawString("Game Paused: Press SPACE to Resume", tileSize, tileSize);
        }
        else{
            g.drawString("Score: "+ snakeBody.size(), tileSize-16, tileSize);
        }
    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            paused = !paused; // Toggle pause state
            if (!paused) {
                repaint(); // Resume game by updating the screen
            }
        }
        if(!paused && !hasMoved){
            //for arrow up key
            if(e.getKeyCode() == KeyEvent.VK_UP && velocityY == 0){
                velocityX = 0;
                velocityY = -1;
            }
            // for arrow down key
            else if(e.getKeyCode() == KeyEvent.VK_DOWN  && velocityY ==0){
                velocityX= 0;
                velocityY= 1;
            }
            // for left arrow key
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX == 0){
                velocityX= -1;
                velocityY= 0;
            }
            // for right arrow key
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX == 0) {
                velocityX= 1;
                velocityY= 0;
            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!gameOver && !paused)
        {
            move();
            repaint();
        }else{
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}