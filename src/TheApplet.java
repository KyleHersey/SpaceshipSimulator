import javax.swing.JApplet;

public class TheApplet extends JApplet{

	public TheApplet(){}
	
	public void init(){
		
		int windowSizeX = 1600;
		int windowSizeY = 800;
		
		//establish the window for the game
		Window win = new Window();
		win.setSize(windowSizeX, windowSizeY);
		this.setSize(windowSizeX, windowSizeY);
		this.setVisible(true);

		getContentPane().add(win);
		
		//make the model
		Model m = new Model(win);
		
		//bind the model to the Panel and display it
		win.setModel(m);
		
		//this ought to be in m.start(), which would return when the game ends
		while(!m.gameOver){
			m.tick();
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	//approximately 30fps
			win.repaint();
			this.repaint();
		}	
		System.out.println("Game Over");
	}
	
	

}
