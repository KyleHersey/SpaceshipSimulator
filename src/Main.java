/**
 * This class contains the entry point
 *
 */
public class Main {
	
	/**
	 * This is the entry point for the game
	 * 
	 * @param args						Program arguments
	 * @throws InterruptedException		Required due to crash workaround. To Fix
	 */
	public static void main(String[] args) throws InterruptedException{
		int windowSizeX = 1600;
		int windowSizeY = 800;
		
		//establish the window for the game
		Frame frame = new Frame();
		Window win = new Window();
		win.setSize(windowSizeX, windowSizeY);

		frame.setSize(windowSizeX, windowSizeY);
		frame.add(win);
		
		//make the model
		Model m = new Model(win);
		
		//bind the model to the Panel and display it
		win.setModel(m);
		frame.setVisible(true);
		
		//this ought to be in m.start(), which would return when the game ends
		while(!m.gameOver){
			m.tick();
			Thread.sleep(5);	//approximately 30fps
			win.repaint();
		}	
		System.out.println("Game Over");
	}
	
}
