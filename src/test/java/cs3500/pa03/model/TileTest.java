package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Tests the tile class.
 */
class TileTest {
  private Tile tile;

  /**
   * Create a new tile before each test.
   */
  @BeforeEach
  void tileSetup() {
    tile = new Tile();
  }

  /**
   * Tests hit status setting.
   */
  @Test
  void hitStatus() {

    // Test setting the hit status
    tile.setHitStatus(HitStatus.HIT);
    assertEquals(HitStatus.HIT, tile.getHitStatus());

    // Check error handling
    assertThrows(IllegalStateException.class,
        () -> tile.setHitStatus(HitStatus.MISS));

  }

  /**
   * Tests ship type setting
   */
  @Test
  void shipType() {
    // Test default constructor
    tile.setShipType(ShipType.SUBMARINE);
    assertEquals(ShipType.SUBMARINE, tile.getShipType());

    // Check error handling
    assertThrows(IllegalStateException.class,
        () -> tile.setShipType(ShipType.CARRIER));

    // Check other constructor type
    tile = new Tile(ShipType.DESTROYER);
    assertEquals(tile.getShipType(), ShipType.DESTROYER);

  }

  /**
   * Tests equality comparison.
   */
  @Test
  void testEquals() {
    Tile tile2 = new Tile();

    tile.setHitStatus(HitStatus.HIT);
    assertFalse(tile.equals(tile2));

    tile2.setShipType(ShipType.DESTROYER);
    assertFalse(tile.equals(tile2));
  }
}
