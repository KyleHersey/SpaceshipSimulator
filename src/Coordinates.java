
public class Coordinates {
	
	private int coordX;
	private int coordY;
	
	private double doubleCoordX;
	private double doubleCoordY;
	
	public Coordinates(){}
	public Coordinates(int x, int y){
		coordX = x;
		coordY = y;
		doubleCoordX = x;
		doubleCoordY = y;
	}
	public Coordinates(double x, double y){
		coordX = (int) x;
		coordY = (int) y;
		doubleCoordX = x;
		doubleCoordY = y;
	}
	
	public void setCoordX(int in){
		coordX = in;
		doubleCoordX = in;
	}
	
	public void setCoordY(int in){
		coordY = in;
		doubleCoordY = in;
	}
	
	public int getCoordX(){
		return coordX;
	}
	
	public int getCoordY(){
		return coordY;
	}
	
	public double getDoubleCoordX(){
		return doubleCoordX;
	}
	
	public double getDoubleCoordY(){
		return doubleCoordY;
	}
	
	public void addToX(double in){
		doubleCoordX += in;
		coordX = (int)doubleCoordX;
	}
	
	public void addToX(int in){
		doubleCoordX += in;
		coordX += in;
	}
	
	public void addToY(double in){
		doubleCoordY += in;
		coordY = (int)doubleCoordY;
	}
	
	public void addToY(int in){
		doubleCoordY += in;
		coordY += in;
	}

}
