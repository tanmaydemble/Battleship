package cs3500.pa03.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa03.model.AbstractPlayer;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.HitStatus;
import cs3500.pa03.model.ShipType;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the terminal view.
 */
class TerminalViewTest {
  BattleSalvoView view;

  /**
   * Make a new terminal view before each test.
   */
  @BeforeEach
  void setup() {
    view = new TerminalView();
  }

  /**
   * Tests displaying a message
   */
  @Test
  void displayMessage() {
    view.displayMessage("Test message");
  }

  /**
   * Tests displaying a board.
   */
  @Test
  void displayBoard() {
    Map<ShipType, Integer> ships = new HashMap<>();
    ships.put(ShipType.CARRIER, 1);
    ships.put(ShipType.BATTLESHIP, 1);
    ships.put(ShipType.DESTROYER, 1);
    ships.put(ShipType.SUBMARINE, 1);
    List<Coord> salvoShots = new ArrayList<>();
    salvoShots.add(new Coord(0, 0));
    salvoShots.add(new Coord(0, 1));
    salvoShots.add(new Coord(0, 2));
    salvoShots.add(new Coord(0, 3));
    salvoShots.add(new Coord(0, 4));
    BattleSalvoView mockView = new MockView(new Coord(10, 10), ships, salvoShots,
        true);
    AbstractPlayer p = new AiPlayer("AI", mockView, false);
    p.setup(10, 10, ships);
    p.reportDamage(salvoShots);
    Board testBoard = p.getPlayerBoard();
    testBoard.markBoardPosition(new Coord(4, 4), HitStatus.HIT);
    view.displayBoard("Test Board 1", testBoard);
    view.displayBoard("Test board 2", p.getPlayerBoard());
  }

  /**
   * Tests getting board size.
   */
  @Test
  void getBoardSize() {
    String testIn = "10 10\n";
    System.setIn(new ByteArrayInputStream(testIn.getBytes()));
    view = new TerminalView();
    int[] expected = {10, 10};
    int[] actual = view.getBoardSize();
    assertEquals(expected[0], actual[0]);
    assertEquals(expected[1], actual[1]);
  }

  /**
   * Tests getting the fleet
   */
  @Test
  void getFleet() {
    String testIn = "1\n1\n1\n1\n";
    System.setIn(new ByteArrayInputStream(testIn.getBytes()));
    view = new TerminalView();
    Map<ShipType, Integer> expected = new HashMap<>();
    expected.put(ShipType.SUBMARINE, 1);
    expected.put(ShipType.DESTROYER, 1);
    expected.put(ShipType.BATTLESHIP, 1);
    expected.put(ShipType.CARRIER, 1);
    Map<ShipType, Integer> actual = view.getFleet(4);
    for (ShipType s : actual.keySet()) {
      assertEquals(expected.get(s), actual.get(s));
    }

  }

  /**
   * Tests getting salvo shots
   */
  @Test
  void getSalvoShots() {
    String testIn = "1 A\n2 B\n3 C\n4 D\n";
    System.setIn(new ByteArrayInputStream(testIn.getBytes()));
    view = new TerminalView();
    List<Coord> actual = view.getSalvoShots(4);
    List<Coord> expected = new ArrayList<>();
    expected.add(new Coord(0, 0));
    expected.add(new Coord(1, 1));
    expected.add(new Coord(2, 2));
    expected.add(new Coord(3, 3));

    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i).row, actual.get(i).row);
      assertEquals(expected.get(i).col, actual.get(i).col);
    }
  }

  /**
   * Tests getting column identifier
   */
  @Test
  void getColumnIdentifier() {
    char id = view.getColumnIdentifier(3);
    assertEquals('D', id);
  }

  /**
   * Tests printing a seperator
   */
  @Test
  void printSeperator() {
    view.printSeperator();
  }
}