package main.java.Model.Ships;


import main.java.Model.Model;
import main.java.Model.Shot;

import java.awt.*;

/**
 * Generic ship.
 * Fastish speed, short range, low damage, low health
 * <p>
 * need to add size, and draw circle/manage collisions based upon it
 */
public class Frigate extends Ship {

    public final double startSpeed = 5;
    public final int startHealth = 200;
    public final int startDamage = 5;
    public final int startRange = 20;
    public final int size = 6;

    Ship target;    //current target ship

    /**
     * @param teamIN The team for the ship. Currently, Red = 1, Blue = 2.
     *               <p>
     *               Future direction involves factory pattern for ship creation, inputting team, Shiptype.
     */
    public Frigate(int teamIN, Model m) {
        super(m);

        team = teamIN;
        setStartPoint(this, m);

        //Base ship stats
        speed = startSpeed;
        health = startHealth;
        damage = startDamage;
        range = startRange;
        radius = size / 2;
        avoidRadius = 3;

        //not yet sure best way to ID ships. this works for now
        shipID = count;
    }

    /* (non-Javadoc)
     * @see Model.Ships.Model.Ships.Ship#move(Model.Ships.Model)
     *
     * Current behavior is to detect the closest enemy ship. Pursue it until it is destroyed.
     *
     */
    @Override
    void moveLogic(Model m) {

        //attempt at collision avoidance with teammates
        //an example of ships differing in behavior
        boolean avoidance = false;
        if (this.team == 1) {        //set for only team 1
            for (int i = 0; i < m.shipList.size(); i++) {
                if (!m.shipList.get(i).equals(this)) {
                    //if(m.shipList.get(i).team == this.team){
                    if (getDistanceSquared(m.shipList.get(i)) < ((radius + avoidRadius) * 2 * (radius + avoidRadius) * 2)) {
                        moveAwayFrom(m.shipList.get(i));
                        avoidance = true;
                    }
                    //}
                }
            }
        }


        //if not in danger of collisions
        if (avoidance == false) {

            Ship closest = null;
            double distance = Integer.MAX_VALUE;

            if (target == null || target.destroyed) {
                target = getClosestEnemy(m);
            }

            //BUG WORKAROUND HERE
            if (this.target == null) {
                m.gameOver = true;
                return;
            }

            //if target is out of range, move towards it
            if (this.getDistanceEdges(target) > range + speed - 10) {
                this.moveToward(target);
            }
        }
    }

    /* (non-Javadoc)
     * @see Model.Ships.Model.Ships.Ship#shoot(Model.Ships.Model)
     *
     * currently wrong but works. Model.Ships.Model should handle whether shots hit or not,
     * evaluating range, etc. This allows more cool things later, such as shields
     *
     */
    @Override
    public void shoot(Model m) {
        if ((target != null) && (getDistance(target) < range)) {
            m.handleShot(new Shot(target, this, "Laser"));
        }
    }

    /* (non-Javadoc)
     * @see Model.Ships.Model.Ships.Ship#drawOnGraphics(java.awt.Graphics)
     *
     * draws the ship onto the graphics. Frigate is an circle for now
     *
     * can be expanded to draw a ship once I feel the need to make it look nice
     */
    @Override
    public void drawOnGraphics(Graphics g) {
        /*to change to look like a ship
		g.setColor(getColor());
		g.drawOval(coords.getCoordX() - radius, coords.getCoordY() - radius, size, size);
		*/

        g.setColor(getColor());
        //orient tip toward target
        if (target != null) {
            int distX = target.coords.getCoordX() - this.coords.getCoordX();
            int distY = target.coords.getCoordY() - this.coords.getCoordY();

            double angle = Math.atan2(distX, distY);
            int forwardPointX = (int) (this.coords.getCoordX() + (Math.sin(angle) * radius));
            int forwardPointY = (int) (this.coords.getCoordY() + (Math.cos(angle) * radius));

            int backLeftPointX = (int) (this.coords.getCoordX() + (Math.sin(angle + Math.PI - Math.PI / 3) * radius));
            int backLeftPointY = (int) (this.coords.getCoordY() + (Math.cos(angle + Math.PI - Math.PI / 3) * radius));

            int backRightPointX = (int) (this.coords.getCoordX() + (Math.sin(angle + Math.PI + Math.PI / 3) * radius));
            int backRightPointY = (int) (this.coords.getCoordY() + (Math.cos(angle + Math.PI + Math.PI / 3) * radius));

            g.drawLine(forwardPointX, forwardPointY, backLeftPointX, backLeftPointY);
            g.drawLine(forwardPointX, forwardPointY, backRightPointX, backRightPointY);
        }
    }

    /* (non-Javadoc)
     * @see Model.Ships.Model.Ships.Ship#onDeath(Model.Ships.Model)
     *
     * this triggers on death.
     *
     * Frigate should not do anything special on death.
     * commented code is remnants of death explosion testing
     */
    @Override
    public void onDeath(Model m) {
        m.shipList.remove(this);
        destroyed = true;
		/* explosion on death
		for(int i = 0 ; i < m.shipList.size(); i++){
			if(this.getRange(m.shipList.get(i)) < 50){
				m.handleShot(new Shot(m.shipList.get(i), 20, this, "Explosion"));
			}
		}
		*/
    }

}
