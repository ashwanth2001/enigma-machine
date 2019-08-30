package machine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import components.Keyboard;
import components.Machine;

public class MachinePanel extends JPanel implements KeyListener, ActionListener, MouseListener{

	private int x, y, width, height;
	private int mX, mY;
	
	private Timer timer;
	private int tick = 15;
	
	private int[] ratio;
	private Keyboard k1, k2;
	private Machine machine;
	
	private int keyDown;
	private boolean mouseDown;
	private int state;
	
	Font font;
	
	public MachinePanel(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		mX = 0;
		mY = 0;
		
		timer = new Timer(tick, this);
		
		setRatio();
		k1 = new Keyboard(0, height/2, width, height/2, Color.GRAY, Color.DARK_GRAY, Color.BLACK, Color.WHITE, Color.WHITE);
		k2 = new Keyboard(0, 0, width, height/2, Color.WHITE, Color.YELLOW, Color.BLACK, Color.BLACK, Color.BLACK);
		machine = new Machine(x, y, width, height);
		
		keyDown = -1;
		mouseDown = false;
		state = 0;
		
		font = new Font("Courier New", 1, 32);
	}

	public void start() {
		timer.start();
	}
	
	public void updateInfo(int x, int y, int width, int height) {
		if(this.x==x&&this.y==y&&this.width==width&&this.height==height-23)
			return;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height-23;
		
		updateItems();
	}
	
	private void updateItems() {
		setRatio();
		k1.updateDim(ratio[0], ratio[1]+2*ratio[3]/3, ratio[2], ratio[3]/3);
		k2.updateDim(ratio[0], ratio[1]+ratio[3]/3, ratio[2], ratio[3]/3);
		machine.updateDim(ratio[0], ratio[1], ratio[2], ratio[3]);
		font = new Font("Courier New", 1, ratio[2]/18);
	}
	
	private void setRatio() {
		if(width>height) {
			ratio = new int[]{(width-height)/2, 0, height, height};
		}
		else {
			ratio = new int[] {0, (height-width)/2, width, width};
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if(state == 0) {
			k1.draw(g);
			k2.draw(g);	
		}
		machine.draw(g);
		drawButton(g);
	}
	
	private void drawButton(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(ratio[0], ratio[1], ratio[2]/3, ratio[3]/6);
		g.setColor(Color.WHITE);
		g.setFont(font);
		if(state == 0) {
			g.drawString("PLUGBOARD", ratio[0]+ratio[2]/50, ratio[1]+ratio[3]/10);
		}
		else {
			g.drawString("KEYBOARD", ratio[0]+ratio[2]/25, ratio[1]+ratio[3]/10);

		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
		
		machine.updateState(state);
		
		mX = MouseInfo.getPointerInfo().getLocation().x;
		mY = MouseInfo.getPointerInfo().getLocation().y;
		machine.updateMouse(mouseDown, mX-x, mY-y-23);
	}
	
	private void clickButton() {
		if(mX-x>=ratio[0]&&mX-x<=ratio[0]+ratio[2]/3 && mY-y-23>=ratio[1]&&mY-y-23<=ratio[1]+ratio[3]/6) {
			state = (state+1)%2;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!mouseDown) {
			clickButton();
		}
		mouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseDown = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(keyDown!=e.getKeyCode()-65&&e.getKeyCode()-65>=0&&e.getKeyCode()-65<=25) {
			if(keyDown>-1) {
				k1.pressed(keyDown, false);
				k2.pressed(-1, false);
			}
			k1.pressed(e.getKeyCode()-65, true);
			
			int n = machine.run(e.getKeyCode()-65);
			k2.pressed(n, true);
			keyDown = e.getKeyCode()-65;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			machine.space();
		}
		else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			machine.back();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			machine.enter();
		}
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()-65>=0&&e.getKeyCode()-65<=25) {
			k1.pressed(e.getKeyCode()-65, false);
			k2.pressed(-1,  false);
			if(keyDown==e.getKeyCode()-65) {
				keyDown = -1;
			}
		}
	}
}
