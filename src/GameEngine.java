import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public abstract class GameEngine extends JPanel implements KeyListener, MouseListener
{
	boolean running = true;
	boolean paused = false;
	long lastTime;
	Container frame;
	protected int viewWidth = 1024;
	protected int viewHeight = 768;
	protected float aspectRatio = 1024.0f/768.0f;
	public GameEngine(Container parentFrame)
	{
		frame = parentFrame;
		frame.setSize(viewWidth,viewHeight);
		frame.setLocation((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2 - viewWidth/2), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2 - viewHeight/2));
		frame.addKeyListener(this);
		frame.addMouseListener(this);
		lastTime = System.currentTimeMillis();
	}
	public abstract void loadContent();
	public abstract void initialize();
	public abstract void updateGame(long elapsedTime);
	
	public void start()
	{
		Thread runThread = new Thread(){
			public void run()
			{
				while(running)
				{
					repaint();
				}
			}
		};
		runThread.start();
	}
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
}
