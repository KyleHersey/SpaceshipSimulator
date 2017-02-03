package main.java.Model.Ships;

import main.java.Model.Coordinates;
import main.java.Model.Model;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


public class Carrier extends Ship {

    final double startSpeed = 1;
    final int startHealth = 200000;
    final int startDamage = 0;
    final int startRange = 2000;
    final int size = 200;

    int maxSingleDeployment;
    int alreadyDeployed;

    Ship target;    //current target ship

    ArrayList<Fighter> fleet;
    final int fleetSize = 1000;

    public Carrier(int teamIN, Model m) {

        super(m);

        team = teamIN;
        setStartPoint(this, m);

        fleet = new ArrayList<Fighter>();
        for (int i = 0; i < fleetSize; i++) {
            fleet.add(new Fighter(team, m, this));
        }

        maxSingleDeployment = 3;
        alreadyDeployed = 0;

        //Base ship stats
        speed = startSpeed;
        health = startHealth;
        damage = startDamage;
        range = startRange;
        radius = size / 2;

        avoidRadius = 3;

        //not yet sure best way to ID ships. this works for now
        shipID = Ship.count;
    }

    @Override
    void moveLogic(Model m) {
        ArrayList<Ship> biggest = new ArrayList<Ship>();
        biggest.add(new ComparatorShip(m, "large"));

        for (int i = 0; i < m.shipList.size(); i++) {
            if (m.shipList.get(i) != this && m.shipList.get(i).team != team) {
                if (m.shipList.get(i) == this) {
                    //System.out.println("made it past somehow");
                }

                if (m.shipList.get(i).radius < biggest.get(0).radius) {
                    biggest = new ArrayList<Ship>();
                    biggest.add(m.shipList.get(i));
                } else if (m.shipList.get(i).radius == biggest.get(0).radius) {
                    biggest.add(m.shipList.get(i));
                }
            }
        }

        //biggest.remove(this);
        Ship closest = null;
        double distance = Double.MAX_VALUE;
        for (int i = 0; i < biggest.size(); i++) {
            if (getDistanceSquared(biggest.get(i)) < distance * distance) {
                closest = biggest.get(i);
                distance = getDistance(biggest.get(i));
            }
        }

        target = closest;
        keepAtRange(500, target);
    }

    @Override
    public void shoot(Model m) {
        for (int i = 0; i < fleet.size(); i++) {
            Fighter f = fleet.get(i);
            if (f.docked) {
                m.shipList.remove(f);
                f.returning = false;
                if (f.health >= f.startHealth) {
                    deployFighter(f, m);
                } else {
                    f.health += 5;
                }
            } else if (f.returning && getDistanceEdges(f) < 20) {
                f.docked = true;
            } else if (f.health < f.startHealth / 2) {
                f.returning = true;
            } else {
                f.target = target;
            }
        }
        alreadyDeployed = 0;
    }

    private void deployFighter(Fighter f, Model m) {
        Random rand = new Random();
        if (alreadyDeployed <= maxSingleDeployment && target != null) {

            f.docked = false;
            f.returning = false;

            m.shipList.add(f);
            f.coords = new Coordinates(this.coords.getCoordX(), this.coords.getCoordY());

            int distX = target.coords.getCoordX() - this.coords.getCoordX();
            int distY = target.coords.getCoordY() - this.coords.getCoordY();

            double angle = Math.atan2(distX, distY);
            angle += Math.PI / 2;
            angle -= (Math.PI * rand.nextDouble());


            f.coords.addToX((Math.sin(angle) * radius) * 1.2);
            f.coords.addToY((Math.cos(angle) * radius) * 1.2);


            f.docked = false;
            f.returning = false;
        }
        alreadyDeployed++;
    }

    private int dockedCount() {
        int count = 0;
        for (int i = 0; i < fleet.size(); i++) {
            if (fleet.get(i).docked) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void drawOnGraphics(Graphics g) {
        g.setColor(this.getColor());
        g.drawOval(coords.getCoordX() - radius, coords.getCoordY() - radius, size, size);

        String count = "" + dockedCount();
        g.drawChars(count.toCharArray(), 0, count.length(), coords.getCoordX(), coords.getCoordY());

    }

    @Override
    public void onDeath(Model m) {

    }

}
