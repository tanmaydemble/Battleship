package cs3500.pa03.view;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ShipType;
import java.util.List;
import java.util.Map;

/**
 * An interface for views in BattleSalvo.
 */
public interface BattleSalvoView {

  /**
   * Displays a message to the user
   *
   * @param message the string to display.
   */
  void displayMessage(String message);

  /**
   * Displays a battle salvo board to the user.
   *
   * @param description A text description for the board
   * @param board       the board object to display
   */
  void displayBoard(String description, Board board);

  /**
   * @return the desired dimensions of the board.
   *     int[0] is the number of rows and int[1] is the number of cols.
   */
  int[] getBoardSize();

  /**
   * Asks the user for the fleet specifications.
   *
   * @param size The maximum fleet size
   * @return a map specifying how many of which boat to use
   */
  Map<ShipType, Integer> getFleet(int size);

  /**
   * Asks the user to select their shots.
   *
   * @param number number of shots to take
   * @return a list of shots.
   */
  List<Coord> getSalvoShots(int number);

  /**
   * @param col the column index
   * @return the letter corresponding to that column, for terminal display.
   */
  char getColumnIdentifier(int col);

  /**
   * Prints a dashed line or other separator, for terminal display.
   */
  void printSeperator();
}
