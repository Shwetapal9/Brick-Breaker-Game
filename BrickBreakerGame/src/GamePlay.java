import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play=false;
    private int score =0;
    private int totalBricks=21;
    private Timer timer;
    private int delay=8;
    private int playerX=310;
    private int ballPosX=120;
    private int ballPosY=350;
    private int ballXDir=-1;
    private int ballYDir=-2;
    private MapGenerator map;
    public GamePlay()
    {
        map= new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer= new Timer(delay,this);
        timer.start();
    }
    public void paint(Graphics g)
    {
        // background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        // drawing map
        map.draw((Graphics2D) g);

        // borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        // the scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString("" + score,590,30);

        // the paddle
        g.setColor(Color.yellow);
        g.fillRect(playerX,550,100,8);

        //the ball
        g.setColor(Color.green);
        g.fillOval(ballPosX,ballPosY,20,20);

        // when you lose the game
        if(ballPosY>570) {
            play = false;
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString(" GAME-OVER ", 230, 300);

            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("SCORE: " + score, 260, 340);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("PRESS ENTER TO START AGAIN", 120, 380);
        }

        // when you won the game
        if(totalBricks==0)
        {
            play=false;
            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString(" GAME-OVER ", 230, 300);

            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("SCORE: " + score, 260, 340);

            g.setColor(Color.WHITE);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("PRESS ENTER TO START AGAIN", 120, 380);
        }
        g.dispose();
    }
    public void actionPerformed(ActionEvent e)
    {
         timer.start();

         if(play) {
             if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                 ballYDir = -ballYDir;
             }

             //check map collision with the ball
             A:
             for (int i = 0; i < map.map.length; i++) {
                 for (int j = 0; j < map.map[0].length; j++) {
                     if (map.map[i][j] > 0) {

                         //scores
                         int brickX = j * map.bricksWidth + 80;
                         int brickY = i * map.bricksHeight + 50;
                         int bricksWidth = map.bricksWidth;
                         int bricksHeight = map.bricksHeight;

                         Rectangle rect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                         Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                         Rectangle brickRect = rect;

                         if (ballRect.intersects(brickRect)) {
                             map.setBricksValue(0, i, j);
                             totalBricks--;
                             score += 5;

                             //when ball hits the right or left of brick
                             if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + bricksWidth) {
                                 ballXDir = -ballXDir;
                             }
                             // when ball hits top or bottom of brick
                             else {
                                 ballYDir = -ballYDir;

                             }
                             break A;
                         }
                     }
                 }
             }
             ballPosX += ballXDir;
             ballPosY += ballYDir;
             if (ballPosX < 0) { //left
                 ballXDir = -ballXDir;
             }
             if (ballPosY < 0) { //top
                 ballYDir = -ballYDir;
             }
             if (ballPosX > 670) { //right
                 ballXDir = -ballXDir;
             }
             repaint();
         }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600)
                playerX = 600;
            else
                moveRight();   //new function to move to the right

        }
        if( e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX < 10)
                playerX = 10;
            else
                moveLeft();    //new function to move to left
        }
        if( e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXDir = -1;
                ballYDir = -2;
                score = 0;
                playerX = 310;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void moveRight()
    {
        play = true;
        playerX += 20;
    }
    public void moveLeft()
    {
        play = true;
        playerX -= 20;
    }

}
