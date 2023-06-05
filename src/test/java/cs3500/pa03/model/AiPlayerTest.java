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
 * Tests the AI player.
 */
class AiPlayerTest {

  AbstractPlayer ai;

  /**
   * Set up a clean AI player before each test.
   */
  @BeforeEach
  void aiPlayerSetup() {
    Map<ShipType, Integer> ships = new HashMap<>();
    ships.put(ShipType.CARRIER, 1);
    ships.put(ShipType.BATTLESHIP, 3);
    ships.put(ShipType.DESTROYER, 2);
    List<Coord> salvoShots = new ArrayList<>();
    salvoShots.add(new Coord(0, 0));
    salvoShots.add(new Coord(0, 1));
    salvoShots.add(new Coord(0, 2));
    salvoShots.add(new Coord(0, 3));
    salvoShots.add(new Coord(0, 4));
    BattleSalvoView mockView = new MockView(new Coord(10, 10), ships, salvoShots, true);
    ai = new AiPlayer("AI Player", mockView, false);
    ai.setup(10, 10, ships);
  }

  /**
   * Tests getting the player's name.
   */
  @Test
  void testName() {
    assertEquals("AI Player", ai.name());
  }


  /**
   * Tests autonomous shot taking.
   */
  @Test
  void testTakeShots() {
    List<Coord> hitList = new ArrayList<>();
    hitList.add(new Coord(1, 1));
    ai.successfulHits(hitList);
    List<Coord> shotList2 = ai.takeShots();
    ai.successfulHits(new ArrayList<Coord>());
    List<Coord> shotList1 = ai.takeShots();
    assertEquals(6, shotList1.size());
    assertEquals(6, shotList2.size());
    ai.takeShots(); // For error handling.
    for (Coord c1 : shotList1) {
      for (Coord c2 : shotList2) {
        assertFalse((c1.row == c2.row) && (c1.col == c2.col));
      }
    }

  }

  /**
   * Tests the end-game message (or lack thereof).
   */
  @Test
  void testEndGame() {
    // Should do nothing and throw no errors.
    ai.endGame(GameResult.WIN, "Testing reason");
  }


}