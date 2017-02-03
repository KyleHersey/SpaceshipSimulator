package main.java.Rendering;

import main.java.Model.Model;

import java.awt.Graphics;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Panel extends JPanel implements Runnable{

    Model m;
    private int sleeptime;
    private boolean running = false;

    public void stopRunning(){
        running = false;
    }

    public void setModel(Model in) {
        m = in;
        m.setWidth(getIntWidth());
        m.setHeight(getIntHeight());

    }

    public void setSleeptime(int ms){
        sleeptime = ms;
    }

    public void run(){
        running = true;

        while(running){
            repaint();
            try {
                Thread.sleep(sleeptime);
            } catch(Exception e){
                return;
            }
        }
    }

    public int getIntWidth(){
        return (int)getSize().getWidth();
    }

    public int getIntHeight(){
        return (int)getSize().getHeight();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        m.setWidth(getIntWidth());
        m.setHeight(getIntHeight());

        DrawingHandler dh = new DrawingHandler();
        dh.drawIt(g, m);

    }
}
