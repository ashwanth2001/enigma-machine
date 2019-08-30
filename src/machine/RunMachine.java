package machine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class RunMachine implements ActionListener {
	
	JFrame frame;
	MachinePanel panel;
	
	final int START_X = 0;
	final int START_Y = 0;
	final int START_WIDTH = 10000;
	final int START_HEIGHT = 10000;
	
	Timer t;
	int tick = 15;
	
	public RunMachine() {
		t = new Timer(tick, this);
		frame = new JFrame("Enigma");
		panel = new MachinePanel(START_X, START_Y, START_WIDTH, START_HEIGHT);
		frame.add(panel);
		frame.addKeyListener(panel);
		frame.addMouseListener(panel);
	}
	
	public static void main(String[] args) {
		RunMachine main = new RunMachine();
		main.setup();
	}
	
	private void setup() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(START_WIDTH, START_HEIGHT);
		frame.setLocation(0, 0);
		frame.setVisible(true);
		panel.start();
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		panel.updateInfo(frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight());
	}
}
