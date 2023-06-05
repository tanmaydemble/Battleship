package cs3500.pa03.model;

import cs3500.pa03.view.BattleSalvoView;
import cs3500.pa03.view.TerminalView;
import java.util.List;

/**
 * Represents an implementation of AbstractPlayer where a human makes shot decisions.
 */
public class HumanPlayer extends AbstractPlayer {

  /**
   * Construct a human player
   *
   * @param name       This player's display name.
   * @param view       The view the human player uses to interact with the game
   * @param mainPlayer true if this player should see their board with ships and the
   *                   opponent with only hits/misses, false otherwise.
   */
  public HumanPlayer(String name, BattleSalvoView view, boolean mainPlayer) {
    super(name, view, mainPlayer);
  }

  /**
   * Asks the user to take Salvo shots for one round.
   *
   * @return a list of shots, the length corresponding to the number of unsunk ships.
   */
  @Override
  public List<Coord> takeShots() {
    super.takeShots();
    if (!shotsTaken) {
      shotsTaken = true;
      int numUnsunkShips = 0;
      for (Ship s : getPlayerBoard().getShips()) {
        if (!s.sunk()) {
          numUnsunkShips++;
        }
      }
      List<Coord> shotList;
      while (true) {
        // Ask the user for shots
        shotList = view.getSalvoShots(numUnsunkShips);
        boolean duplicates = false;
        // Check that there's no duplicates in the shot list AND
        // Check they didn't previously attack those coordinates
        for (int i = 0; i < shotList.size(); i++) {
          if (!opponentBoard.getTile(shotList.get(i)).getHitStatus()
              .equals(HitStatus.UNATTACKED)) {
            duplicates = true;
          }
          for (int j = 0; j < shotList.size(); j++) {
            if (i != j) {
              if (shotList.get(i).equals(shotList.get(j))) {
                duplicates = true;
              }
            }
          }
        }

        if (duplicates) {
          System.out.println("Shots cannot be duplicates. Please try again.");
        } else {
          break;
        }
      }
      for (Coord c : shotList) {
        mostRecentAttack.add(c.clone());
      }
      return shotList;
    } else {
      System.err.println("ERROR: Must report damage before taking the next set of shots.");
      return null;
    }
  }

  /**
   * Displays and end-of-game message to the player.
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
    BattleSalvoView view = new TerminalView();
    if (result.equals(GameResult.WIN)) {
      view.displayMessage("Congrats, you win!\nReason: " + reason);
    } else if (result.equals(GameResult.LOSE)) {
      view.displayMessage("Sorry, you lost.\nReason: " + reason);
    } else {
      view.displayMessage("The game ended in a tie.\nReason: " + reason);
    }
  }
}
