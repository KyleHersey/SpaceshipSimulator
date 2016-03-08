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
		
		//establish the window for the game
		Frame frame = new Frame();
		Window win = new Window();
		frame.add(win);
		frame.setSize(600, 600);
		
		
		//make the model
		Model m = new Model();
		
		//bind the model to the Panel and display it
		win.setModel(m);
		frame.setVisible(true);
		
		//this ought to be in m.start(), which would return when the game ends
		while(m.shipList.size() > 1){
			m.tick();
			Thread.sleep(25);
			win.repaint();
		}	
	}
	
}
