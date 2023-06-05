package cs3500.pa03.model;

/**
 * Enumerates between the different types, with their corresponding size.
 */
public enum ShipType {
  CARRIER(6),
  BATTLESHIP(5),
  DESTROYER(4),
  SUBMARINE(3),
  NO_SHIP(0);

  public final int size;

  /**
   * @param size the ship's length
   */
  ShipType(int size) {
    this.size = size;
  }
}
