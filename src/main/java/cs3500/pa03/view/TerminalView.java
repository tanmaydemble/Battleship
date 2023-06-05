package cs3500.pa03.view;

import cs3500.pa03.model.Board;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.HitStatus;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.model.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * A terminal UI implementation of BattleSalvoView
 */
public class TerminalView implements BattleSalvoView {

  Scanner scanner;
  private final HashMap<Integer, Character> columnIdentifiers = new HashMap<>();
  private final HashMap<String, Integer> reverseColumnIdentifiers = new HashMap<>();

  /**
   * Create a new Terminal View object and initialize state.
   */
  public TerminalView() {
    scanner = new Scanner(System.in);
    generateColumnMap();
  }

  /**
   * @param message the string to display to the terminal.
   */
  @Override
  public void displayMessage(String message) {
    System.out.println(message);
  }

  /**
   * Displays a board to the terminal.
   *
   * @param description A text description for the board
   * @param board       the board object to display
   */
  @Override
  public void displayBoard(String description, Board board) {
    printSeperator();
    System.out.println("\n" + description);
    for (int i = 0; i < description.length(); i++) {
      System.out.print("-");
    }
    System.out.println("\n");


    final int numCols = board.getSize().col;
    final int numRows = board.getSize().row;
    System.out.print("  ");
    for (int i = 0; i < numCols; i++) {
      System.out.print("| " + columnIdentifiers.get(i) + " ");
    }
    System.out.println("|");

    // Print each row:
    for (int i = 0; i < numRows; i++) {
      // Print the row seperator
      System.out.println("-".repeat((numCols * 4) + 3));
      // Print the row identifier (index + 1... start at 1 not 0)
      if (i < 9) {
        System.out.print((i + 1) + " |");
      } else {
        System.out.print((i + 1) + "|"); // Get rid of shift at 10.
      }
      // Print out each cell
      for (int j = 0; j < numCols; j++) {
        char cellContents = getCellContents(board.getTile(new Coord(i, j)));
        System.out.print(" " + cellContents + " |");
      }
      System.out.println();

    }
    System.out.println("-".repeat((numCols * 4) + 3));
  }

  /**
   * Asks the user for the board's dimensions.
   *
   * @return the board's dimensions, where int[0] represents row and int[1] represents col.
   * @throws InputMismatchException if the user attempted to provide non-integer values.
   */
  @Override
  public int[] getBoardSize() throws InputMismatchException {
    printSeperator();
    System.out.println("Please enter a valid height and width below.");
    System.out.println("The format is height then width. For example '10 10'");
    System.out.print("HEIGHT AND WIDTH: ");
    boolean invalid = true;
    int row = -1;
    int col = -1;
    while (invalid) {
      row = scanner.nextInt();
      col = scanner.nextInt();
      invalid = false;
    }
    int[] returnValue = {row, col};
    return returnValue;
  }

  /**
   * Asks the user for the fleet composition.
   *
   * @param size The maximum fleet size
   * @return A map containing each ship type and the quantity desired.
   * @throws InputMismatchException if the user attempts to respond with an illegal value.
   */
  @Override
  public Map<ShipType, Integer> getFleet(int size) throws InputMismatchException {
    printSeperator();
    int carriers;
    int battleships;
    int destroyers;
    int submarines;
    while (true) {
      System.out.println(
          "Please enter your fleet. Your fleet size must not exceed " + size + ".");
      System.out.print("How many Carriers do you want: ");
      carriers = readIntegerInput();
      System.out.print("How many Battleships do you want: ");
      battleships = readIntegerInput();
      System.out.print("How many Destroyers do you want: ");
      destroyers = readIntegerInput();
      System.out.print("How many Submarines do you want: ");
      submarines = readIntegerInput();
      int sum = carriers + battleships + destroyers + submarines;
      if ((carriers > 0) && (battleships > 0) && (destroyers > 0) && (submarines > 0)) {
        if ((sum <= size)) {
          break;
        }
      }
      System.out.println("That fleet is not valid, please try again.");
    }
    Map<ShipType, Integer> shipList = new HashMap<>();
    shipList.put(ShipType.CARRIER, carriers);
    shipList.put(ShipType.BATTLESHIP, battleships);
    shipList.put(ShipType.DESTROYER, destroyers);
    shipList.put(ShipType.SUBMARINE, submarines);
    return shipList;
  }

  /**
   * Asks the user to select shots for a Salvo round.
   *
   * @param number number of shots to take.
   * @return a list of coordinates corresponding to the shots.
   */
  @Override
  public List<Coord> getSalvoShots(int number) {
    printSeperator();
    System.out.println("Please enter " + number + " shots:");
    System.out.println("Shot format example: '2 A'");
    System.out.println("SHOTS:");
    List<Coord> shots = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      boolean invalid = true;
      while (invalid) {
        try {
          int row = 16; //Invalidly-large
          String colStr = "";
          if (scanner.hasNext()) {
            row = scanner.nextInt();
            row--;
            colStr = scanner.nextLine();
            colStr = colStr.toUpperCase().trim();
          }

          if ((reverseColumnIdentifiers.containsKey(colStr)) && (row < 15)) {
            int col = reverseColumnIdentifiers.get(colStr);
            Coord currCoord = new Coord(row, col);
            shots.add(currCoord);
            invalid = false;
          }
        } catch (InputMismatchException e) {
          System.out.println("Invalid input, please try again.");
          scanner = new Scanner(System.in);
        }
      }
    }
    return shots;
  }

  /**
   * Gets the letter corresponding to the column in question. This view uses letters
   * instead of numbers for columns to make the user experience a little better.
   *
   * @param col the column index
   * @return the character corresponding to that column.
   */
  @Override
  public char getColumnIdentifier(int col) {
    return columnIdentifiers.get(col);
  }

  /**
   * Prints a dashed line seperator to the terminal.
   */
  @Override
  public void printSeperator() {
    System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
  }

  /**
   * Reads an integer from the user.
   *
   * @return the integer the user entered.
   * @throws InputMismatchException if the user inputs anything not classified as an integer.
   */
  private int readIntegerInput() throws InputMismatchException {
    boolean invalid = true;
    int result = -1;
    while (invalid) {
      if (scanner.hasNext()) {
        result = scanner.nextInt();
        invalid = false;
      }
    }
    return result;
  }

  /**
   * Generates hashmaps for column identification.
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

  /**
   * Gets the character representation of a tile's status.
   *
   * @param t the tile to display
   * @return 'H' if hit, a letter representing the ship type if not hit, 'M' if a miss,
   *     or ' ' if empty.
   */
  private char getCellContents(Tile t) {
    // Always display hits above all else.
    if (t.getHitStatus().equals(HitStatus.HIT)) {
      return 'H';
    } else {
      // Determine ship and return correct type
      ShipType type = t.getShipType();
      if (type.equals(ShipType.CARRIER)) {
        return 'C';
      } else if (type.equals(ShipType.BATTLESHIP)) {
        return 'B';
      } else if (type.equals(ShipType.DESTROYER)) {
        return 'D';
      } else if (type.equals(ShipType.SUBMARINE)) {
        return 'S';
        // If not a ship, and it's a miss, display that
      } else if (t.getHitStatus().equals(HitStatus.MISS)) {
        return 'M';
      } else {
        return ' ';
      }
    }
  }
}
