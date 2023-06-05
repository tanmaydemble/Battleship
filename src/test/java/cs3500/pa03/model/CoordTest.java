package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


/**
 * Tests coord objects
 */
class CoordTest {

  /**
   * Tests all of Coord's functionality.
   */
  @Test
  void coordsTest() {

    // Check size parameters.
    assertThrows(IllegalArgumentException.class,
        () -> new Coord(16, -1));
    assertThrows(IllegalArgumentException.class,
        () -> new Coord(-1, 16));
    assertThrows(IllegalArgumentException.class,
        () -> new Coord(-1, -1));
    assertThrows(IllegalArgumentException.class,
        () -> new Coord(16, 16));

    // Check normal operation
    Coord coord = new Coord(5, 6);
    assertEquals(5, coord.row);
    assertEquals(6, coord.col);
  }

}