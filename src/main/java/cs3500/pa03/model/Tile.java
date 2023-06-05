package cs3500.pa03.model;

/**
 * Represents a single tile on a BattleSalvo board.
 */
public class Tile implements Cloneable {
  private ShipType ship;
  private HitStatus hitStatus;

  /**
   * Create an empty tile
   */
  public Tile() {
    ship = ShipType.NO_SHIP;
    hitStatus = HitStatus.UNATTACKED;
  }

  /**
   * Create a tile occupied with a ship.
   *
   * @param ship the type of ship on this tile.
   */
  public Tile(ShipType ship) {
    this.ship = ship;
    hitStatus = HitStatus.UNATTACKED;
  }

  /**
   * @return the type of ship on this tile.
   */
  public ShipType getShipType() {
    return ship;
  }

  /**
   * Adds a ship to this tile, if empty.
   *
   * @param ship the type of ship to occupy this tile
   * @throws IllegalStateException if there's already a ship on this tile.
   */
  public void setShipType(ShipType ship) throws IllegalStateException {
    if (this.ship == ShipType.NO_SHIP) {
      this.ship = ship;
    } else {
      throw new IllegalStateException("A tile's ship type cannot be changed unless empty.");
    }
  }

  /**
   * @return the HitStatus of this tile.
   */
  public HitStatus getHitStatus() {
    return hitStatus;
  }

  /**
   * Mark this tile as a hit or miss.
   *
   * @param h the Hit/Miss status to add to this tile.
   * @throws IllegalStateException if attempting to overwrite the status of this tile
   *                               (if it has already been hit or missed before)
   */
  public void setHitStatus(HitStatus h) throws IllegalStateException {
    if (hitStatus.equals(HitStatus.UNATTACKED)) {
      hitStatus = h;
    } else {
      throw new IllegalStateException("ERROR: A previously hit or missed"
          + " tile cannot be attacked again.");
    }
  }

  /**
   * An implementation of the cloneable interface.
   *
   * @return a deep-copy clone of this tile object.
   */
  @Override
  public Tile clone() {
    Tile cloneTile = new Tile(this.ship);
    cloneTile.setHitStatus(this.hitStatus);
    return cloneTile;
  }

  /**
   * Checks if another tile is the same as this one.
   *
   * @param o the Tile to compare with
   * @return true if they are equal, false otherwise.
   */
  public boolean equals(Tile o) {
    return (o.getShipType() == this.ship) && (o.getHitStatus() == this.hitStatus);
  }


}
