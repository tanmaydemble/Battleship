package cs3500.pa03.control;

import cs3500.pa03.model.AbstractPlayer;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Ship;
import cs3500.pa03.model.ShipType;
import cs3500.pa03.view.BattleSalvoView;
import cs3500.pa03.view.TerminalView;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

/**
 * The controller for the BattleSalvo game.
 */
public class BattleSalvoControl {
  private final AbstractPlayer p1;
  private final AbstractPlayer p2;
  BattleSalvoView view;

  /**
   * Always assumes P1 is the human or board to display as the current player.
   *
   * @param p1 The first player in the game.
   * @param p2 The second player in the game.
   */
  public BattleSalvoControl(AbstractPlayer p1, AbstractPlayer p2, BattleSalvoView view) {
    this.p1 = p1;
    this.p2 = p2;
    this.view = view;
  }

  /**
   * Runs one Battle Salvo game with the players and view specified in the constructor.
   */
  public void runGame() {
    // Welcome user
    view.displayMessage("Welcome to BattleSalvo!");
    // Get board dimensions
    int[] dimensions;
    while (true) {
      try {
        dimensions = view.getBoardSize();
        // Validate board between min and max size.
        boolean widthValid = (dimensions[0] <= 15) && (dimensions[0] >= 6);
        boolean heightValid = (dimensions[1] <= 15) && (dimensions[1] >= 6);
        if (widthValid && heightValid) {
          break;
        }
        view.displayMessage("Error: the dimensions you entered are invalid. Please try again.");
        view.displayMessage("Remember, the height and width must be between 6 and 15, inclusive.");
      } catch (InputMismatchException e) {
        view = new TerminalView();
        System.out.println("That board size wasn't valid. Please try again...");
      }
    }

    // Get fleet
    int fleetSize = Math.max(dimensions[0], dimensions[1]);
    Map<ShipType, Integer> fleet;
    while (true) {
      try {
        fleet = view.getFleet(fleetSize);
        break;
      } catch (InputMismatchException e) {
        view = new TerminalView();
        System.out.println("That fleet wasn't valid. Please try again...");
      }
    }

    // Place ships
    p1.setup(dimensions[0], dimensions[1], fleet);
    p2.setup(dimensions[0], dimensions[1], fleet);

    // Game loop
    while (true) {
      // Start the loop
      List<Coord> p1Shots = p1.takeShots();
      List<Coord> p2Shots = p2.takeShots();
      List<Coord> p2SuccessfulShots = p1.reportDamage(p2Shots);
      List<Coord> p1SuccessfulShots = p2.reportDamage(p1Shots);

      // Send an update to the players
      p1.successfulHits(p1SuccessfulShots);
      p2.successfulHits(p2SuccessfulShots);

      // Check if game is over
      GameResult result = checkIfGameOver();
      if (result == GameResult.WIN) {
        p1.endGame(GameResult.WIN, "You sunk all of your opponent's ships!");
        p2.endGame(GameResult.LOSE, "Your opponent sunk all of your ships!");
        break;
      } else if (result == GameResult.LOSE) {
        p2.endGame(GameResult.WIN, "You sunk all of your opponent's ships!");
        p1.endGame(GameResult.LOSE, "Your opponent sunk all of your ships!");
        break;
      } else if (result == GameResult.TIE) {
        p1.endGame(GameResult.TIE, "You both sunk all of each other's ships.");
        p2.endGame(GameResult.TIE, "You both sunk all of each other's ships.");
        break;
      }

    }
  }

  /**
   * Checks if the game is over.
   * Note: All returns are in reference to P1. When displaying messages to P2, the result
   * needs to be inverted.
   *
   * @return WIN if P1 wins, LOSS if P1 loses, TIE if they tied, or UNFINISHED if the game
   *     is still in progress.
   */
  private GameResult checkIfGameOver() { //todo: Find a way to do this w/o getPlayerBoard...

    // Check if P1 Has any remaining ships
    boolean p1Lost = true;
    ArrayList<Ship> p1Ships = p1.getPlayerBoard().getShips();
    for (Ship s : p1Ships) {
      if (!s.sunk()) {
        p1Lost = false;
        break;
      }
    }
    // Check if P2 has any remaining ships
    boolean p2Lost = true;
    ArrayList<Ship> p2Ships = p2.getPlayerBoard().getShips();
    for (Ship s : p2Ships) {
      if (!s.sunk()) {
        p2Lost = false;
        break;
      }
    }

    if (p1Lost && p2Lost) {
      return GameResult.TIE;
    } else if (p1Lost) {
      return GameResult.LOSE;
    } else if (p2Lost) {
      return GameResult.WIN;
    } else {
      return GameResult.UNFINISHED;
    }
  }


}
