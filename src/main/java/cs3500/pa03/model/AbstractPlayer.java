package cs3500.pa03.model;

import cs3500.pa03.view.BattleSalvoView;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * An abstract class that adds shared functionality for all players.
 */
public abstract class AbstractPlayer implements Player {
  protected Board playerBoard;
  protected Board opponentBoard;
  protected String name;
  protected ArrayList<Coord> mostRecentAttack = new ArrayList<>();
  protected boolean shotsTaken = false;
  protected BattleSalvoView view;
  protected boolean mainPlayer;

  /**
   * Constructs an abstract player.
   *
   * @param name       this player's display name
   * @param view       the view to use for communicating with this player
   * @param mainPlayer true if this player should have its board printed to the view as if it
   *                   is the current user (e.g. the human), and false if not (e.g. the AI)
   */
  public AbstractPlayer(String name, BattleSalvoView view, boolean mainPlayer) {
    this.name = name;
    this.view = view;
    this.mainPlayer = mainPlayer;

  }

  /**
   * @return the player's display name
   */
  @Override
  public String name() {
    return name;
  }

  /**
   * Given the specifications for a BattleSalvo board, return a list of ships with their locations
   * on the board.
   *
   * @param height         the height of the board, range: [6, 15] inclusive
   * @param width          the width of the board, range: [6, 15] inclusive
   * @param specifications a map of ship type to the number of occurrences each ship should
   *                       appear on the board
   * @return the placements of each ship on the board
   */
  @Override
  public List<Ship> setup(int height, int width, Map<ShipType, Integer> specifications) {
    playerBoard = new Board(height, width);
    opponentBoard = new Board(height, width);
    // Generate ship positions given board dimensions.
    List<Ship> shipList = generateShipList(specifications);
    // Place the ships on playerBoard
    playerBoard.placeShips((ArrayList<Ship>) shipList);
    // Return the list of ships
    return shipList;
  }

  /**
   * This is to be implemented in AiPlayer and HumanPlayer.
   * NOTE: Must update the mostRecentAttack list with the coordinates of this attack.
   * Returns this player's shots on the opponent's board. The number of shots returned should
   * equal the number of ships on this player's board that have not sunk.
   *
   * @return the locations of shots on the opponent's board
   */
  @Override
  public List<Coord> takeShots() {
    // Call super to display info...
    if (mainPlayer) {
      view.displayBoard("Your Board:", playerBoard);
      view.displayBoard("Your Opponent's Board:", opponentBoard);
    }
    return null;
  }

  /**
   * Given the list of shots the opponent has fired on this player's board, report which
   * shots hit a ship on this player's board. Must set shotsTaken to true after execution.
   *
   * @param opponentShotsOnBoard the opponent's shots on this player's board
   * @return a filtered list of the given shots that contain all locations of shots that hit a
   *     ship on this board
   */
  @Override
  public List<Coord> reportDamage(List<Coord> opponentShotsOnBoard) {
    List<Coord> hitList = new ArrayList<>();
    for (Coord c : opponentShotsOnBoard) {
      try {
        HitStatus status = playerBoard.addAttack(c);
        if (status.equals(HitStatus.HIT)) {
          hitList.add(c.clone());
        }
      } catch (IllegalAccessException e) {
        System.err.println("ERROR: The opponent shot at the same spot more than once.");
        System.exit(1);
      }
    }
    return hitList;
  }

  /**
   * Reports to this player what shots in their previous volley returned from takeShots()
   * successfully hit an opponent's ship.
   *
   * @param shotsThatHitOpponentShips the list of shots that successfully hit the opponent's ships
   */
  public void successfulHits(List<Coord> shotsThatHitOpponentShips) {
    if (shotsTaken) {
      shotsTaken = false;
      for (Coord c : shotsThatHitOpponentShips) {
        opponentBoard.markBoardPosition(c, HitStatus.HIT);
      }

      for (Coord c : mostRecentAttack) {
        Tile curr = opponentBoard.getTile(c);
        if (curr.getHitStatus().equals(HitStatus.UNATTACKED)) {
          opponentBoard.markBoardPosition(c, HitStatus.MISS);
        }
      }
      displayStatistics(shotsThatHitOpponentShips);
      mostRecentAttack = new ArrayList<>(); // Purge the old attack's list.
    }


  }

  /**
   * Notifies the player that the game is over.
   * Win, lose, and draw should all be supported
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  public abstract void endGame(GameResult result, String reason);

  public Board getPlayerBoard() {
    return playerBoard.clone();
  }

  /**
   * Helper to generate a list of ships.
   * Modify ShipTypeList to specify which ships and CurrCords to change locations.
   *
   * @param specifications the number of ships of each type to place.
   * @return a list of validly-placed ships.
   */
  private ArrayList<Ship> generateShipList(Map<ShipType, Integer> specifications) {
    ArrayList<Ship> ships = new ArrayList<>();
    boolean overlap = true;
    while (overlap) {
      Random random = new Random(Instant.now().toEpochMilli()); // Seed the generator
      ArrayList<Coord> occupiedCoords = new ArrayList<>();
      for (ShipType s : specifications.keySet()) {
        int numShips = specifications.get(s);
        for (int i = 0; i < numShips; i++) {
          // Generate i ships for each type
          // Randomly generate coords until they don't overlap.
          overlap = true;
          while (overlap) {
            ArrayList<Coord> currCoords;
            overlap = false;
            boolean horizontal = random.nextBoolean();
            Coord size = playerBoard.getSize();
            if (horizontal) {
              int colIdx;
              if ((size.col - s.size) == 0) {
                colIdx = 0;
              } else {
                colIdx = random.nextInt(size.col - s.size);
              }
              currCoords = generateCoordsList(random.nextInt(size.row),
                  colIdx, true, s.size);
            } else {
              int rowIdx;
              if ((size.row - s.size) <= 0) {
                rowIdx = 0;
              } else {
                rowIdx = random.nextInt(size.row - s.size);
              }
              currCoords = generateCoordsList(rowIdx,
                  random.nextInt(size.col), false, s.size);
            }
            // Check to make sure coords aren't already occupied.
            for (Coord cc : currCoords) {
              for (Coord oc : occupiedCoords) {
                boolean rowsOverlap = cc.row == oc.row;
                boolean colsOverlap = cc.col == oc.col;
                if (rowsOverlap && colsOverlap) {
                  overlap = true;
                  break;
                }
              }
              if (overlap) {
                break;
              }
            }
            if (!overlap) {
              Ship currShip = new Ship(s, currCoords);
              ships.add(currShip);
              occupiedCoords.addAll(currCoords);
            }
          }

        }
      }
    }
    return ships;
  }

  /**
   * A tool to generate coordinates for a ship, given a starting row, starting col, orientation,
   * and size.
   *
   * @param startrow   starting index for the row
   * @param startcol   starting indices for the column
   * @param horizontal true if the ship is drawn horizontally (on one row), false otherwise
   * @param size       the length of the ship
   * @return a list of coordinates per the parameters.
   */
  private ArrayList<Coord> generateCoordsList(int startrow, int startcol, boolean horizontal,
                                              int size) {
    ArrayList<Coord> shipCoords = new ArrayList<>();
    if (horizontal) {
      for (int i = startcol; i < (size + startcol); i++) {
        shipCoords.add(new Coord(startrow, i));
      }
    } else {
      for (int i = startrow; i < (size + startrow); i++) {
        shipCoords.add(new Coord(i, startcol));
      }
    }
    return shipCoords;
  }

  /**
   * Prints the statistics for this player in the most recent round to the specified view.
   *
   * @param playerSuccessfulShots A list of this player's shots from the most recent round
   *                              that hit the opponent's ships.
   */
  private void displayStatistics(List<Coord> playerSuccessfulShots) {
    view.printSeperator();
    view.displayMessage("The following of " + name + "'s shots successfully hit targets: ");
    for (Coord c : playerSuccessfulShots) {
      if (playerSuccessfulShots.size() > 0) {
        int row = c.row;
        int col = c.col;
        int rowId = row + 1;
        char colId = view.getColumnIdentifier(col);
        view.displayMessage("[ " + rowId + " " + colId + " ]");
      } else {
        view.displayMessage("None of " + name + "'s shots hit targets. ");
      }
    }

    view.displayMessage("The following of " + name + "'s shots missed: ");
    boolean noMisses = true;
    for (Coord c : mostRecentAttack) {
      boolean success = false;
      for (Coord s : playerSuccessfulShots) {
        if (c.equals(s)) {
          success = true;
        }
      }
      if (!success) {
        int row = c.row;
        int col = c.col;
        int rowId = row + 1;
        char colId = view.getColumnIdentifier(col);
        view.displayMessage("[ " + rowId + " " + colId + " ]");
        noMisses = false;
      }
    }
    if (noMisses) {
      view.displayMessage("None of " + name + "'s shots missed!");
    }

    // Generate list of remaining ships
    List<Ship> ships = playerBoard.getShips();
    int carriers = 0;
    int battleships = 0;
    int destroyers = 0;
    int submarines = 0;

    for (Ship s : ships) {
      if (!s.sunk()) {
        if (s.getType().equals(ShipType.CARRIER)) {
          carriers++;
        } else if (s.getType().equals(ShipType.BATTLESHIP)) {
          battleships++;
        } else if (s.getType().equals(ShipType.DESTROYER)) {
          destroyers++;
        } else {
          submarines++;
        }
      }
    }

    view.printSeperator();
    view.displayMessage(name + " has the following ships remaining: ");
    view.displayMessage("Carriers (size 6): " + carriers);
    view.displayMessage("Battleships (size 5): " + battleships);
    view.displayMessage("Destroyers (size 4): " + destroyers);
    view.displayMessage("Submarines (size 3): " + submarines);

  }
}
