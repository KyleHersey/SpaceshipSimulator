package main.java.Model;

import main.java.Model.Ships.*;
import main.java.Rendering.Panel;

import java.util.ArrayList;

/**
 * This models the spaceship game.
 * <p>
 * Everything that is simulation logic happens in here
 * <p>
 * <p>
 * Notes: 	Keeping track of ship destruction should me more heavily regulated.
 * This can be accomplished by making more things private to the model.
 * (As it should be)
 * <p>
 * Maybe, each ship should move, then check for deaths, then allow shooting, then check for deaths.
 */
public class Model {

    public boolean gameOver;

    int refreshSleep;

    public void setRefreshSleep(int ms){
        refreshSleep = ms;
    }

    public int width;
    public int height;

    public void setWidth(int width_in){
        width = width_in;
    }

    public void setHeight(int height_in){
        height = height_in;
    }

    int laserKills;                //deaths by laser
    public int collisionKills;            //deaths by collision

    public int highestKillstreak;        //most kills by a single ship
    final int startShips = 5;    //this many ships on each team at start

    public ArrayList<Ship> shipList = new ArrayList<Ship>();    //list of ships

    //should be moved to shot class
    public ArrayList<Shot> shotList = new ArrayList<Shot>();    //list of shots (for graphical purposes)

    /**
     * Initializes the model with the starting ships
     */
    public Model() {
        gameOver = false;
    }

    private void setup(){
        //addCarrier(1);
        //addCarrier(2);
        for (int i = 0; i < 5; i++) {
            addFrigate(1);
            addFrigate(2);
        }
    }

    public void start() {
        setup();

        while (!gameOver) {
            System.out.println("ticking");
            tick();
            try {
                Thread.sleep(refreshSleep);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }    //approximately 30fps
        }
        System.out.println("Game Over");
    }

    /**
     * Happens as a defined interval of time
     * Model.Ships.Model performs a list of procedures each time it 'ticks'
     */
    public void tick() {

        //clear the list of shots
        shotList = new ArrayList<Shot>();

        //tell each ship to move. If it survived that, make it shoot.
        for (int i = 0; i < shipList.size(); i++) {
            Ship tempShip = shipList.get(i);
            tempShip.move(this);

            //check for boundary violation
            if (!(0 < tempShip.coords.getCoordX() && tempShip.coords.getCoordX() < width &&
                    0 < tempShip.coords.getCoordY() && tempShip.coords.getCoordY() < height)) {
                destroyShip(tempShip, false);
            }

            if (shipList.contains(tempShip)) {
                tempShip.shoot(this);
            }
        }
    }

    /**
     * This method is called to keep the board populated with ships. The world is my fishtank.
     * <p>
     * This may need to be an addShip method, which makes use of a ship factory class
     *
     * @param team team to make new frigates with
     */
    private void addFrigate(int team) {
        shipList.add(new Frigate(team, this));
    }

    private void addDestroyer(int team) {
        shipList.add(new Destroyer(team, this));
    }

    private void addCarrier(int team) {
        shipList.add(new Carrier(team, this));
    }

    /**
     * This method should do more validation that the ship firing the shot is capable of firing the shot
     *
     * @param s the shot that the ship sent
     */
    public void handleShot(Shot s) {
        shotList.add(s);    //add to list to draw
        s.getTarget().health -= s.getSender().damage;    //apply damage (maybe use a method. abstract in ship class?)

        //if we destroyed the ship.
        if (s.getTarget().health <= 0 && s.getTarget().destroyed == false) {
            destroyShip(s.getTarget(), true);    //Note: can cause problems with death explosions enabled

            //System.out.println(s.getSender().team + " killed " + s.getTarget().team);

            laserKills++;    //may become obsolete as more weapons come into play

            //handout experience to the ship (not sure if this is smart)
            //s.getSender().damage *= 2;
            s.getSender().killstreak += 1;

            //check killstreak - why bother. introduction of different ships makes this less cool
            if (s.getSender().killstreak > highestKillstreak) {
                highestKillstreak = s.getSender().killstreak;
            }
        }
    }

    /**
     * Called when a ship needs to be destroyed.
     * Adds ships to keep the simulation populated.
     *
     * @param s        ship to destroy
     * @param makeMore whether or not to populate model
     */
    public void destroyShip(Ship s, boolean makeMore) {
        shipList.remove(s);
        s.onDeath(this);
        if (makeMore && !(s instanceof Fighter)) {
            for (int i = 0; i < 2; i++) {
                replaceShip(s);
            }
        }
    }

    public void replaceShip(Ship s) {
        if (s instanceof Frigate) {
            shipList.add(new Frigate(s.team, this));
        } else if (s instanceof Destroyer) {
            shipList.add(new Destroyer(s.team, this));
        }
    }

    /**
     * More of an admin tool I guess.
     * This method removes the ship from the shiplist
     *
     * @param s the ship to be removed
     */
    public void removeShip(Ship s) {
        shipList.remove(s);
        s.destroyed = true;
    }

    /**
     * This should probably be implemented in a different way to handle alllll the colors
     *
     * @return the number of red ships
     */
    public int getReds() {
        int count = 0;
        for (int i = 0; i < shipList.size(); i++) {
            if (shipList.get(i).team == 1) {
                count++;
            }
        }
        return count;
    }

    /**
     * See getReds
     *
     * @return the number of blue ships
     */
    public int getBlues() {
        return shipList.size() - getReds();
    }
}
