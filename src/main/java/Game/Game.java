package main.java.Game;

import main.java.Model.Model;
import main.java.Rendering.Frame;
import main.java.Rendering.Panel;

import java.awt.*;

/**
 * Created by KHersey on 2/3/2017.
 */
public class Game {

    public void start() {

        int modelRefreshSleep = 15;
        int panelRefreshSleep = 15;

        int windowSizeX = 1600;
        int windowSizeY = 800;

        Panel panel = new Panel();
        Frame frame = new Frame();

        frame.setSize(windowSizeX, windowSizeY);
        panel.setSize(windowSizeX, windowSizeY);

        //TODO: uncouple these
        //make the model
        Model m = new Model();
        panel.setModel(m);

        /*add reset and pause button
        Button reset = new Button("Reset");
        reset.setVisible(true);
        panel.add(reset);
        */

        frame.add(panel);
        panel.setVisible(true);
        frame.setVisible(true);

        panel.setSleeptime(panelRefreshSleep);

        new Thread(panel).start();

        m.setRefreshSleep(modelRefreshSleep);
        m.start();

    }
}
