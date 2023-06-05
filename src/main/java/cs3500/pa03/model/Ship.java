package cs3500.pa03.model;

import java.util.ArrayList;

/**
 * Represents a ship in BattleSalvo
 */
public class Ship implements Cloneable {
  private final ShipType type;
  private final ArrayList<Coord> location;
  private boolean[] hits;

  /**
   * Constructs a new ship
   *
   * @param type     the ShipType of this ship
   * @param location the board coordinates this ship occupies
   * @throws IllegalArgumentException if the coordinates' length doesn't match the ship's size.
   */
  public Ship(ShipType type, ArrayList<Coord> location) throws IllegalArgumentException {
    if (location.size() != type.size) {
      throw new IllegalArgumentException("ERROR: Ships must"
          + " occupy the correct number of tiles");
    }
    this.type = type;
    this.location = location;
    hits = new boolean[type.size];
  }

  /**
   * Adds a hit to the specified coordinate, if applicable.
   * Ignores coordinates that don't belong to this ship.
   *
   * @param hitLocation the Coord of the hit.
   */
  public void addHit(Coord hitLocation) {
    for (int i = 0; i < location.size(); i++) {
      Coord coord;
      coord = location.get(i);
      if (coord.equals(hitLocation)) {
        hits[i] = true;
      }
    }
  }

  /**
   * @return this ship's ShipType
   */
  public ShipType getType() {
    return type;
  }

  /**
   * @return a copy of this ship's occupied coordinates list.
   */
  public ArrayList<Coord> getLocation() {
    ArrayList<Coord> clonedList = new ArrayList<>();
    for (Coord c : location) {
      clonedList.add(c);
    }
    return clonedList;
  }

  /**
   * @return true if this ship has been sunk, false otherwise.
   */
  public boolean sunk() {
    for (boolean val : hits) {
      if (!val) {
        return false;
      }
    }
    return true;
  }

  /**
   * @return a clone of this Ship object.
   */
  @Override
  public Ship clone() {
    Ship clone = new Ship(this.type, this.location);
    clone.setHits(this.hits);
    return clone;
  }

  /**
   * This is a helper method for cloning. Not to be used by objects other than Ships.
   *
   * @param hitsList the list of hits to copy over
   */
  protected void setHits(boolean[] hitsList) {
    hits = hitsList;
  }

  /**
   * @param o the object to be compared.
   * @return 0 if the ships are the same,
   *     -1 if this ship is smaller, or 1 if this ship is larger.
   */
  public int compareTo(Ship o) {
    if (this.type.size < o.getType().size) {
      return -1;
    } else if (this.type.size == o.getType().size) {
      return 0;
    } else {
      return 1;
    }
  }
}
