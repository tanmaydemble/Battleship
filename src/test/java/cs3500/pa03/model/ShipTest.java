package cs3500.pa03.model;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the Ship class.
 */
class ShipTest {


  Ship ship;

  /**
   * Set up a clean ship before each test
   */
  @BeforeEach
  void shipSetup() {
    ArrayList<Coord> shipCoords = generateCoordsList();
    ship = new Ship(ShipType.DESTROYER, shipCoords);
  }

  /**
   * Test giving the ship illegal coordinates.
   */
  @Test
  void testIllegalConstructorArgs() {
    // Create an incorrectly sized coords list
    ArrayList<Coord> shipCoords = new ArrayList<>();
    shipCoords.add(new Coord(5, 5));
    shipCoords.add(new Coord(5, 6));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> new Ship(ShipType.BATTLESHIP, shipCoords));
  }

  /**
   * Tests the sunk-checking functionality
   */
  @Test
  void testSunk() {
    // Add 3 hits (but not the 4th yet)
    ship.addHit(new Coord(5, 5));
    ship.addHit(new Coord(5, 6));
    ship.addHit(new Coord(5, 7));
    Assertions.assertFalse(ship.sunk());

    // Add the final blow
    ship.addHit(new Coord(5, 8));
    Assertions.assertTrue(ship.sunk());

  }

  /**
   * Tests getType
   */
  @Test
  void testType() {
    Assertions.assertEquals(ShipType.DESTROYER, ship.getType());
  }

  /**
   * Tests getting the ship's location.
   */
  @Test
  void testGetCoords() {
    ArrayList<Coord> expected = generateCoordsList();
    ArrayList<Coord> actual = ship.getLocation();
    boolean coordsMatch = true;
    for (int i = 0; i < expected.size(); i++) {
      if (!expected.get(i).equals(actual.get(i))) {
        coordsMatch = false;
      }
    }
    Assertions.assertTrue(coordsMatch);
  }

  /**
   * Tests ship comparisons.
   */
  @Test
  void testCompareTo() {
    ArrayList<Coord> location = new ArrayList<>();
    location.add(new Coord(0, 0));
    location.add(new Coord(0, 1));
    location.add(new Coord(0, 2));
    Ship s1 = new Ship(ShipType.SUBMARINE, location);
    location.add(new Coord(0, 3));
    location.add(new Coord(0, 4));
    Ship s2 = new Ship(ShipType.BATTLESHIP, location);
    Assertions.assertEquals(-1, s1.compareTo(s2));
    Assertions.assertEquals(1, s2.compareTo(s1));
  }

  /**
   * A helper for ship setup.
   *
   * @return a list of coordinates for the ship.
   */
  private ArrayList<Coord> generateCoordsList() {
    ArrayList<Coord> shipCoords = new ArrayList<>();
    shipCoords.add(new Coord(5, 5));
    shipCoords.add(new Coord(5, 6));
    shipCoords.add(new Coord(5, 7));
    shipCoords.add(new Coord(5, 8));
    return shipCoords;
  }
}