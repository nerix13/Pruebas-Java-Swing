import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.util.Random;
import java.util.ArrayList;

public class ThreadAnimationMultipleSprites extends JFrame {
  
  public ThreadAnimationMultipleSprites() {
	initUI();
  }

  private void initUI() {
	add(new Board());

	setResizable(false);
	pack();

	setTitle("Block");
	setLocationRelativeTo(null);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String args[]) {
	EventQueue.invokeLater(() -> {
	  JFrame ex = new ThreadAnimationMultipleSprites();
	  ex.setVisible(true);
	});
  }

  public class Board extends JPanel implements Runnable {

    private final int B_WIDTH = 350;
    private final int B_HEIGHT = 350;
    private final int INITIAL_X = -40;
    private final int INITIAL_Y = -40;
    private final int DELAY = 10;
	private final int TOTAL_AMONGOS = 5; 

	private Random r = new Random();

    private ArrayList<Amongo> amongos;
	private Thread animator;

    public Board() {
        initBoard();
    }

    private void loadImage() {

		amongos = new ArrayList<Amongo>();

		for (int i=0; i < TOTAL_AMONGOS; i++) {
		  String filename = String.format("amongo%d.png", i);
		  ImageIcon ii = new ImageIcon(filename);
		  Image sprite = ii.getImage();
		  amongos.add(new Amongo(sprite, 30 + r.nextInt(100), 30 + r.nextInt(100), r.nextInt(5) + 1, r.nextInt(5) + 1));
		}
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImage();
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this, "Board Thread");
        animator.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawAmongo(g);
    }

    private void drawAmongo(Graphics g) {
		for (int i=0; i < amongos.size(); i++) {
		  Amongo amongo = amongos.get(i);
		  System.out.println(r.nextInt(100));
		  g.drawImage(amongo.sprite, amongo.x, amongo.y, this);
		}
    }

    private void cycle() {
      for (int i=0; i < amongos.size(); i++) {
		Amongo amongo = amongos.get(i);

		// direction listener...
		if (amongo.direction_x == "right")
		  amongo.x += amongo.speed_x;
		else if (amongo.direction_x == "left") // left
		  amongo.x -= amongo.speed_x;
		if (amongo.direction_y == "down")
		  amongo.y += amongo.speed_y;
		else if (amongo.direction_y == "up")// up
		  amongo.y -= amongo.speed_y;

		if (amongo.x > B_WIDTH - 32)
		  amongo.direction_x = "left";
		else if (amongo.x <= 0)
		  amongo.direction_x = "right";
		if (amongo.y > B_HEIGHT - 32)
		  amongo.direction_y = "up";
		else if (amongo.y <= 0)
		  amongo.direction_y = "down";
	  } 
    }

    @Override
    public void run() {
        long beforeTime, timeDiff, sleep;
        beforeTime = System.currentTimeMillis();

        while (true) {
            cycle();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) sleep = 2;

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                String msg = String.format("Thread interrupted: %s", e.getMessage());
                
                JOptionPane.showMessageDialog(this, msg, "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
	  }
	
	public class Amongo {
	  private Image sprite;
	  private int x, y, speed_x, speed_y;
	  private String direction_x, direction_y;

	  public Amongo(Image i, int x, int y, int speed_x, int speed_y) {
		this.sprite = i;
		this.x = x;
		this.y = y;
		this.speed_x = speed_x; // speed x
		this.speed_y = speed_y; // speed y

		direction_x = "right";
		direction_y = "down";
	  }
	}

  }	
}
