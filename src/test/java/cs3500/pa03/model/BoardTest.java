package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the board class
 */
class BoardTest {
  Board board;

  /**
   * Set up a clean board before each test.
   */
  @BeforeEach
  void boardSetup() {
    // Generate a 9x10 board and place ships
    board = new Board(9, 10);
    board.placeShips(generateShipList());
  }

  /**
   * Tests getting ships from the board.
   */
  @Test
  void testGetShips() {
    ArrayList<Ship> expected = generateShipList();
    ArrayList<Ship> actual = board.getShips();
    boolean listsMatch = true;
    for (int i = 0; i < expected.size(); i++) {
      if (expected.get(i).compareTo(actual.get(i)) != 0) {
        listsMatch = false;
      }
    }
    assertTrue(listsMatch);

  }

  /**
   * Tests getting a tile from the board.
   */
  @Test
  void testGetTile() {
    Tile expected = new Tile(ShipType.CARRIER);
    boolean coordsMatch = expected.equals(board.getTile(new Coord(0, 0)));
    assertTrue(coordsMatch);
  }

  /**
   * Tests setting the hit status
   *
   * @throws IllegalAccessException in some cases if the test fails.
   */
  @Test
  void testHitStatus() throws IllegalAccessException {

    // Check hit
    HitStatus returnVal = board.addAttack(new Coord(0, 0));
    assertEquals(HitStatus.HIT, returnVal);

    // Check miss
    returnVal = board.addAttack(new Coord(8, 0));
    assertEquals(HitStatus.MISS, returnVal);

    // Check re-attacking the same position
    assertThrows(IllegalAccessException.class,
        () -> board.addAttack(new Coord(0, 0)));
  }

  /**
   * Tests placing the ships in overlapping or other illegal ways.
   */
  @Test
  void testPlaceShipsExceptions() {

    // Generate a ship outside board dimensions and test it throws an error.
    ArrayList<Coord> shipLocation = new ArrayList<>();
    shipLocation.add(new Coord(12, 12));
    shipLocation.add(new Coord(12, 13));
    shipLocation.add(new Coord(12, 14));
    Ship s1 = new Ship(ShipType.SUBMARINE, shipLocation);
    ArrayList<Ship> ships = new ArrayList<>();
    ships.add(s1);
    assertThrows(IndexOutOfBoundsException.class,
        () -> board.placeShips(ships));

    // S1 and S2 in the same position.
    shipLocation = new ArrayList<>();
    shipLocation.add(new Coord(1, 2));
    shipLocation.add(new Coord(1, 3));
    shipLocation.add(new Coord(1, 4));
    Ship s2 = new Ship(ShipType.SUBMARINE, shipLocation);
    Ship s3 = new Ship(ShipType.SUBMARINE, shipLocation);
    ArrayList<Ship> ships2 = new ArrayList<>();
    ships2.add(s2);
    ships2.add(s3);
    assertThrows(IllegalArgumentException.class,
        () -> board.placeShips(ships2));

  }

  /**
   * Tests marking the same board position more than once.
   */
  @Test
  void testMarkBoardPositionException() {
    board.markBoardPosition(new Coord(1, 1), HitStatus.HIT);
    assertThrows(IllegalStateException.class,
        () -> board.markBoardPosition(new Coord(1, 1), HitStatus.HIT));
    // Should print an error message to screen, not throw anything.
  }

  /**
   * Helper to generate a list of ships.
   * Modify ShipTypeList to specify which ships and CurrCords to change locations.
   *
   * @return a list of validly-placed ships.
   */
  private ArrayList<Ship> generateShipList() {
    ArrayList<Ship> ships = new ArrayList<>();
    ShipType[] shipTypeList = {ShipType.CARRIER, ShipType.BATTLESHIP, ShipType.DESTROYER,
        ShipType.SUBMARINE};
    for (int i = 0; i < 4; i++) { // Generate one of each ship...
      ArrayList<Coord> currCoords = generateCoordsList(0, i, false,
          shipTypeList[i].size);
      ships.add(new Ship(shipTypeList[i], currCoords));
    }
    return ships;
  }

  /**
   * A tool to generate coordinates for a ship, given a starting row, starting col,
   * orientation, and size.
   * todo: Consider moving this to somewhere more useful (e.g. in the model)
   *
   * @param startRow   starting index for the row
   * @param startCol   starting indes for the column
   * @param horizontal true if the ship is drawn horizontally (on one row),
   *                   false if vertical (on one col)
   * @param size       the length of the ship
   * @return a list of coordinates per the parameters.
   */
  private ArrayList<Coord> generateCoordsList(int startRow, int startCol,
                                              boolean horizontal, int size) {
    ArrayList<Coord> shipCoords = new ArrayList<>();
    if (horizontal) {
      for (int i = startCol; i < size + startCol; i++) {
        shipCoords.add(new Coord(startRow, i));
      }
    } else {
      for (int i = startRow; i < size + startRow; i++) {
        shipCoords.add(new Coord(i, startCol));
      }
    }
    return shipCoords;
  }
}