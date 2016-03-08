import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;

public class TheApplet extends JApplet{
	Model m;

	public TheApplet(){}
	
	public void init(){
		
		int windowSizeX = 1600;
		int windowSizeY = 800;
		
		//establish the window for the game
		Window win = new Window();
		win.setSize(windowSizeX, windowSizeY);
		this.setSize(windowSizeX, windowSizeY);


		getContentPane().add(win);
		
		//make the model
		m = new Model(win);
		//bind the model to the Panel and display it
		win.setModel(m);
		
		//add reset and pause button
		Button reset = new Button("Reset");
		reset.addActionListener(new resetAction(this));
		reset.setVisible(true);
		win.add(reset);
		
		this.validate();
		
		this.setVisible(true);
		
		//this ought to be in m.start(), which would return when the game ends
		while(!m.gameOver){
			System.out.println("ticking");
			m.tick();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//approximately 30fps
			win.repaint();
		}	
		System.out.println("Game Over");
	}
	
	public class resetAction implements ActionListener{
		TheApplet j;
		
		public resetAction(TheApplet j){
			this.j = j;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			j.m.gameOver = true;
			j.init();
		}
	}
	
	

}
