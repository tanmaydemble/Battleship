package cs3500.pa03.model;

/**
 * An object to track coordinates
 */
public class Coord implements Cloneable {

  // These are accessible as Coords are immutable.
  public final int row;
  public final int col;

  private static final int MAX_SIZE = 15;
  private static final int MIN_SIZE = 0;

  /**
   * Coordinates are stored as integers between 0 and 14. These map directly to
   * the array indices.
   * NOTE: When presenting to the user, convert the row so that it starts at 1 and
   * col so that it starts at A.
   *
   * @param row the row index
   * @param col the column index
   */
  public Coord(int row, int col) throws IllegalArgumentException {
    if ((row <= MAX_SIZE) && (col <= MAX_SIZE) && (row >= MIN_SIZE) && (col >= MIN_SIZE)) {
      this.row = row;
      this.col = col;
    } else {
      throw new IllegalArgumentException("ERROR: Coord indices are out of bounds.");
    }
  }

  /**
   * Compares this coord with another.
   *
   * @param coord the Coord to compare this one against
   * @return true if they are equal, false otherwise.
   */
  public boolean equals(Coord coord) {
    return ((this.row == coord.row) && (this.col == coord.col));
  }

  /**
   * An implementation of the cloneable interface.
   *
   * @return a copy of this Coord.
   */
  @Override
  public Coord clone() {
    return new Coord(this.row, this.col);
  }
}
