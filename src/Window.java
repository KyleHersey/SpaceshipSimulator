import java.awt.Graphics;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Window extends JPanel{

	Model m;
	
	public void setModel(Model in){
		m = in;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		DrawingHandler dh = new DrawingHandler();
		dh.drawIt(g, m);
		
	}
}
