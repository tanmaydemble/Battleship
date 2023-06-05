package cs3500.pa03.view;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock view class used to test other components without
 * issues regarding reading user input during testing.
 */
public class MockView implements BattleSalvoView {

  private final HashMap<Integer, Character> columnIdentifiers = new HashMap<>();
  private final HashMap<String, Integer> reverseColumnIdentifiers = new HashMap<>();

  private Coord boardSize;
  private final Map<ShipType, Integer> fleet;
  private List<Coord> salvoShots;
  private boolean moveShots;

  /**
   * The parameters here will be returned if asked by the controller or model.
   *
   * @param boardSize  The board's dimensions
   * @param fleet      the map of shipType and number of ships.
   * @param salvoShots the shots to take on the first round where asked.
   * @param moveShots  true if the shots should be in new positions every time, false otherwise.
   */
  public MockView(Coord boardSize, Map<ShipType, Integer> fleet, List<Coord> salvoShots,
                  boolean moveShots) {
    this.moveShots = moveShots;
    this.boardSize = boardSize;
    this.fleet = fleet;
    this.salvoShots = salvoShots;
    generateColumnMap();
  }

  /**
   * Displays a message to the user
   *
   * @param message the string to display.
   */
  @Override
  public void displayMessage(String message) {
    // Do nothing.
  }

  /**
   * Displays a battle salvo board to the user.
   *
   * @param description A text description for the board
   * @param board       the board object to display
   */
  @Override
  public void displayBoard(String description, Board board) {
    // Do nothing.
  }

  /**
   * @return the desired dimensions of the board.
   *     int[0] is the number of rows and int[1] is the number of cols.
   */
  @Override
  public int[] getBoardSize() {
    int[] size = new int[2];
    size[0] = boardSize.row;
    size[1] = boardSize.col;
    boardSize = new Coord(10, 10); // Make sure any 2nd attempt is valid.
    return size;
  }

  /**
   * Asks the user for the fleet specifications.
   *
   * @param size The maximum fleet size
   * @return a map specifying how many of which boat to use
   */
  @Override
  public Map<ShipType, Integer> getFleet(int size) {
    return fleet;
  }

  /**
   * Asks the user to select their shots.
   *
   * @param number number of shots to take
   * @return a list of shots.
   */
  @Override
  public List<Coord> getSalvoShots(int number) {
    List<Coord> oldSalvoShots = salvoShots;
    if (moveShots) {
      salvoShots = new ArrayList<>();
      for (Coord s : oldSalvoShots) {
        salvoShots.add(new Coord(s.row + 1, s.col));
      }
    }
    moveShots = true; // Only need this feature for one iteration.
    return oldSalvoShots;
  }

  /**
   * @param col the column index
   * @return the character representing that column.
   */
  public char getColumnIdentifier(int col) {
    return columnIdentifiers.get(col);
  }

  /**
   * Does nothing.
   */
  @Override
  public void printSeperator() {
    // do nothing
  }

  /**
   * Makes HashMaps for column representation.
   */
  private void generateColumnMap() {
    columnIdentifiers.put(0, 'A');
    columnIdentifiers.put(1, 'B');
    columnIdentifiers.put(2, 'C');
    columnIdentifiers.put(3, 'D');
    columnIdentifiers.put(4, 'E');
    columnIdentifiers.put(5, 'F');
    columnIdentifiers.put(6, 'G');
    columnIdentifiers.put(7, 'H');
    columnIdentifiers.put(8, 'I');
    columnIdentifiers.put(9, 'J');
    columnIdentifiers.put(10, 'K');
    columnIdentifiers.put(11, 'L');
    columnIdentifiers.put(12, 'M');
    columnIdentifiers.put(13, 'N');
    columnIdentifiers.put(14, 'O');
    reverseColumnIdentifiers.put("A", 0);
    reverseColumnIdentifiers.put("B", 1);
    reverseColumnIdentifiers.put("C", 2);
    reverseColumnIdentifiers.put("D", 3);
    reverseColumnIdentifiers.put("E", 4);
    reverseColumnIdentifiers.put("F", 5);
    reverseColumnIdentifiers.put("G", 6);
    reverseColumnIdentifiers.put("H", 7);
    reverseColumnIdentifiers.put("I", 8);
    reverseColumnIdentifiers.put("J", 9);
    reverseColumnIdentifiers.put("K", 10);
    reverseColumnIdentifiers.put("L", 11);
    reverseColumnIdentifiers.put("M", 12);
    reverseColumnIdentifiers.put("N", 13);
    reverseColumnIdentifiers.put("O", 14);
  }
}
