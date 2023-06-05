package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import cs3500.pa03.view.BattleSalvoView;
import cs3500.pa03.view.MockView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests human player's functionality.
 */
class HumanPlayerTest {

  AbstractPlayer human;
  Map<ShipType, Integer> ships;
  List<Coord> salvoShots;

  /**
   * Set up a clean player before each test.
   */
  @BeforeEach
  void humanPlayerSetup() {
    ships = new HashMap<>();
    ships.put(ShipType.CARRIER, 1);
    ships.put(ShipType.BATTLESHIP, 3);
    ships.put(ShipType.DESTROYER, 1);
    ships.put(ShipType.SUBMARINE, 1);
    salvoShots = new ArrayList<>();
    salvoShots.add(new Coord(0, 0));
    salvoShots.add(new Coord(0, 1));
    salvoShots.add(new Coord(0, 2));
    salvoShots.add(new Coord(0, 3));
    salvoShots.add(new Coord(0, 4));
    salvoShots.add(new Coord(0, 5));
    BattleSalvoView mockView = new MockView(new Coord(10, 10), ships, salvoShots,
        true);
    human = new HumanPlayer("Human Player", mockView, false);
    human.setup(10, 10, ships);
  }


  /**
   * Tests getting the name
   */
  @Test
  void testName() {
    assertEquals("Human Player", human.name());
  }


  /**
   * Tests taking shots
   */
  @Test
  void testTakeShots() {
    human.successfulHits(new ArrayList<>());
    List<Coord> shotList1 = human.takeShots();
    human.successfulHits(new ArrayList<Coord>());
    List<Coord> shotList2 = human.takeShots();
    human.successfulHits(new ArrayList<Coord>());
    assertEquals(6, shotList1.size());
    assertEquals(6, shotList2.size());

    for (Coord c1 : shotList1) {
      for (Coord c2 : shotList2) {
        assertFalse((c1.row == c2.row) && (c1.col == c2.col));
      }
    }
  }

  /**
   * Tests taking the same shots more than once (error handling)
   */
  @Test
  void testTakeDuplicateShots() {
    salvoShots.clear();
    salvoShots.add(new Coord(0, 1));
    salvoShots.add(new Coord(0, 2));
    salvoShots.add(new Coord(0, 3));
    salvoShots.add(new Coord(0, 4));
    salvoShots.add(new Coord(0, 5));
    salvoShots.add(new Coord(0, 6));
    BattleSalvoView mockView = new MockView(new Coord(10, 10), ships, salvoShots, false);
    human = new HumanPlayer("Human Player", mockView, false);
    human.setup(10, 10, ships);
    human.takeShots();

  }

  /**
   * Tests the end-of-game display message.
   */
  @Test
  void testEndGame() {
    // Should do nothing and throw no errors.
    human.endGame(GameResult.WIN, "Testing reason");
    human.endGame(GameResult.LOSE, "Testing reason");
    human.endGame(GameResult.TIE, "Testing reason");
  }

}