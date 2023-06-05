package cs3500.pa03.model;

import cs3500.pa03.view.BattleSalvoView;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An implementation of AbstractPlayer that autonomously makes moves.
 */
public class AiPlayer extends AbstractPlayer {

  static final int MAX_ALLOWED_ATTEMPTS = 500; // Cap the number of attempts if the board is full.
  // This is useful in cases where the number of spots left on the opponent's board is
  // fewer than the number of ships this player has.

  /**
   * Constructs an AI Player
   *
   * @param name       This player's display name.
   * @param view       The view used to display information about this player.
   * @param mainPlayer True if this player is the view's main one, false otherwise.
   *                   If true, the board will show ships for this player and only
   *                   hit/miss for the opponent.
   */
  public AiPlayer(String name, BattleSalvoView view, boolean mainPlayer) {
    super(name, view, mainPlayer);
  }


  /**
   * Automatically (randomly) determines the coordinates of shots for a Salvo round.
   * Takes the number of shots corresponding to unsunk ships remaining.
   *
   * @return a list of positions to shoot at in this round.
   */
  @Override
  public List<Coord> takeShots() {
    super.takeShots();
    if (!shotsTaken) {
      shotsTaken = true;

      // Get number of unsunk ships
      List<Coord> shotList = new ArrayList<>();
      int numUnsunkShips = 0;
      for (Ship s : getPlayerBoard().getShips()) {
        if (!s.sunk()) {
          numUnsunkShips++;
        }
      }

      // Generate numUnsunkShips shots...
      Random random = new Random(Instant.now().toEpochMilli());
      int maxRowIdx = this.opponentBoard.getSize().row;
      int maxColIdx = this.opponentBoard.getSize().col;
      for (int i = 0; i < numUnsunkShips; i++) {
        Coord curr;
        int attemptCount = 0;
        boolean validShot = true;
        while (true) {
          attemptCount++;
          int row = random.nextInt(maxRowIdx);
          int col = random.nextInt(maxColIdx);
          curr = new Coord(row, col);
          if (opponentBoard.getTile(curr).getHitStatus().equals(HitStatus.UNATTACKED)) {
            validShot = true;
            for (Coord shot : shotList) {
              if (curr.equals(shot)) {
                validShot = false;
              }

            }
            if (validShot) {
              break;
            }
          }
          if (attemptCount > MAX_ALLOWED_ATTEMPTS) {
            break;
          }
        }
        if (validShot) {
          shotList.add(curr);
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
   * Does nothing, as the AI does not need end-game stats.
   *
   * @param result if the player has won, lost, or forced a draw
   * @param reason the reason for the game ending
   */
  @Override
  public void endGame(GameResult result, String reason) {
  } //AI Doesn't need a message.
}
