package cs3500.pa03.control;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cs3500.pa03.model.AbstractPlayer;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.BattleSalvoView;
import cs3500.pa03.view.MockView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the controller
 */
class BattleSalvoControlTest {

  BattleSalvoView mockView;
  private BattleSalvoControl control;
  private Map<ShipType, Integer> ships;
  private List<Coord> salvoShots;

  /**
   * Set up a clean controller for each test.
   */
  @BeforeEach
  void setUp() {
    ships = new HashMap<>();
    ships.put(ShipType.CARRIER, 1);
    ships.put(ShipType.BATTLESHIP, 3);
    ships.put(ShipType.DESTROYER, 2);
    salvoShots = new ArrayList<>();
    salvoShots.add(new Coord(0, 0));
    salvoShots.add(new Coord(0, 1));
    salvoShots.add(new Coord(0, 2));
    salvoShots.add(new Coord(0, 3));
    salvoShots.add(new Coord(0, 4));
    mockView = new MockView(new Coord(10, 10), ships, salvoShots, true);
    AbstractPlayer p1 = new AiPlayer("AI1", mockView, false);
    AbstractPlayer p2 = new AiPlayer("AI2", mockView, true);
    control = new BattleSalvoControl(p1, p2, mockView);
  }

  /**
   * Tests normal operation.
   */
  @Test
  void runGame() {

    // Run it many times to capture all cases.
    for (int i = 0; i < 10; i++) {
      assertDoesNotThrow(() -> control.runGame());
    }
  }

  /**
   * Tests providing illegal dimensions.
   */
  @Test
  void testIllegalDimensions() {
    BattleSalvoView mv = new MockView(new Coord(1, 1), ships, salvoShots, true);
    AbstractPlayer p1 = new AiPlayer("AI1", mockView, false);
    AbstractPlayer p2 = new AiPlayer("AI2", mockView, true);
    control = new BattleSalvoControl(p1, p2, mv);
    control.runGame();

  }
}