package main.java.Model.Ships;

import main.java.Model.Model;

import java.awt.Graphics;


public class ComparatorShip extends Ship {

    public ComparatorShip(Model m, String s) {
        super(m);
        // TODO Auto-generated constructor stub

        if (s == "small") {
            makeSmall();
        } else if (s == "large") {
            makeLarge();
        }
    }

    public void makeSmall() {
        radius = -1;
    }

    public void makeLarge() {
        radius = Integer.MAX_VALUE;
    }

    @Override
    void moveLogic(Model m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void shoot(Model m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void drawOnGraphics(Graphics g) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeath(Model m) {
        // TODO Auto-generated method stub

    }


}
