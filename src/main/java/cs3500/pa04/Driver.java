package cs3500.pa04;

import cs3500.pa03.control.BattleSalvoControl;
import cs3500.pa03.model.AbstractPlayer;
import cs3500.pa03.model.AiPlayer;
import cs3500.pa03.model.HumanPlayer;
import cs3500.pa03.view.BattleSalvoView;
import cs3500.pa03.view.TerminalView;

/**
 * Manages the program entry point.
 */
public class Driver {
  /**
   * The program entry point
   *
   * @param args disregarded for this implementation
   */
  public static void main(String[] args) {
    try {
      BattleSalvoView view = new TerminalView();
      AbstractPlayer human = new HumanPlayer("Human", view, true);
      AbstractPlayer ai = new AiPlayer("AI", view, false);
      BattleSalvoControl control = new BattleSalvoControl(human, ai, new TerminalView());
      control.runGame();
    } catch (Exception e) {
      System.out.println("Some unexpected error occurred.");
    }
  }

}