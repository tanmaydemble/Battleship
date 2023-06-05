package cs3500.pa03.model;

import java.util.ArrayList;

/**
 * Represents one game board in BattleSalvo (either the current player or opponent).
 */
public class Board {
  private ArrayList<Ship> ships = new ArrayList<>();
  private final Tile[][] board;

  /**
   * Constructs a board with the specified dimensions and initializes each tile.
   *
   * @param rows the board's height (number of rows)
   * @param cols the board's width (number of columns)
   */
  public Board(int rows, int cols) {
    board = new Tile[rows][cols];
    // Initialize each tile.
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        board[i][j] = new Tile();
      }
    }
  }

  /**
   * Places a list of ships on the board
   *
   * @param shipList the list of ships (which must contain their coordinates)
   * @throws IllegalArgumentException  if ships overlap on the board.
   * @throws IndexOutOfBoundsException if the ship's positions are beyond board dimensions.
   */
  public void placeShips(ArrayList<Ship> shipList)
      throws IllegalArgumentException, IndexOutOfBoundsException {
    this.ships = shipList;
    for (Ship s : shipList) {
      ArrayList<Coord> shipLocation = s.getLocation();
      for (Coord c : shipLocation) {
        if ((c.row >= board.length) || (c.col >= board[0].length)) {
          throw new IndexOutOfBoundsException("ERROR: Ship index out of bounds.");
        }
        if (board[c.row][c.col].getShipType().equals(ShipType.NO_SHIP)) {
          board[c.row][c.col].setShipType(s.getType());
        } else {
          throw new IllegalArgumentException("ERROR: Provided ship positions overlap.");
        }
      }
    }
  }

  /**
   * Process an attack upon a specified coordinate.
   *
   * @param location the coordinate to be attacked
   * @return the HitStatus of the attack, either HIT or MISS
   * @throws IllegalAccessException if attempting to attack a Coord that's already been attacked.
   */
  public HitStatus addAttack(Coord location) throws IllegalAccessException {
    HitStatus tileHitStatus;
    // Make sure tile is so far un-attacked.
    if (board[location.row][location.col].getHitStatus().equals(HitStatus.UNATTACKED)) {
      // Check if a ship is there.
      if (board[location.row][location.col].getShipType() != ShipType.NO_SHIP) {
        tileHitStatus = HitStatus.HIT;
        board[location.row][location.col].setHitStatus(HitStatus.HIT);
        // Find the ship that was hit and update its status also.
        for (Ship s : ships) {
          s.addHit(location); // this is ignored by ships unless they own that coordinate.
        }
      } else {
        tileHitStatus = HitStatus.MISS;
        board[location.row][location.col].setHitStatus(HitStatus.MISS);

      }
    } else {
      throw new IllegalAccessException("ERROR: Cannot re-attack tile on board.");
    }

    return tileHitStatus;
  }

  /**
   * Changes the HitStatus of a tile on the board, used to mark hits and misses.
   *
   * @param location  Coordinate to update
   * @param hitStatus new status to mark that position as.
   * @throws IllegalStateException if attempting to overwrite a tile that's already
   *                               been marked as hit or miss.
   */
  public void markBoardPosition(Coord location, HitStatus hitStatus)
      throws IllegalStateException {
    board[location.row][location.col].setHitStatus(hitStatus);
  }


  /**
   * @return a deep copy of the list of ships on this board.
   */
  public ArrayList<Ship> getShips() {
    ArrayList<Ship> cloneList = new ArrayList<>();
    for (Ship s : ships) {
      cloneList.add(s.clone());
    }
    return cloneList;
  }

  /**
   * Returns a copy of the tile at the specified position.
   *
   * @param location the coordinates of the desired tile
   * @return a deep copy of the tile at the specified location.
   */
  public Tile getTile(Coord location) {
    return board[location.row][location.col].clone();
  }

  /**
   * @return the dimensions of this board as a Coord object.
   */
  public Coord getSize() {
    return new Coord(board.length, board[0].length);
  }

  /**
   * @return a deep-copy clone of this board.
   */
  public Board clone() {
    Board clone = new Board(board.length, board[0].length);
    clone.placeShips(ships);
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        clone.setTile(new Coord(i, j), board[i][j]);
      }
    }
    return clone;
  }

  /**
   * Sets a particular tile, without regard for its previous state.
   * Only to be used for deep copy functionality.
   *
   * @param c the position of the tile
   * @param t the new tile to replace that position with
   */
  protected void setTile(Coord c, Tile t) {
    board[c.row][c.col] = t;
  }

}
