package tradelog.logic.command;

import tradelog.model.Trade;
import tradelog.model.TradeList;
import tradelog.storage.Storage;
import tradelog.ui.Ui;

/**
 * Represents a command to add a new trade to the TradeLog.
 * Handles parsing, strict validation of user arguments, and executing the addition.
 */
public class AddCommand extends Command{
    public static final String[] REQUIRED_PREFIXES = {"t/", "d/", "dir/", "e/", "x/", "s/", "o/", "strat/"};

    private final Trade addTrade;

    /**
     * Constructs an AddCommand with a pre-validated Trade object.
     *
     * @param trade The completely validated Trade to be added.
     */
    public AddCommand(Trade trade) {
        addTrade = trade;
    }

    /**
     * Executes the add command by creating a new Trade object, adding it to the TradeList,
     * and displaying the trade summary to the user.
     *
     * @param tradeList The current list of trades.
     * @param ui        The UI handler for output.
     * @param storage   The storage handler for persistence.
     */
    @Override
    public void execute(TradeList tradeList, Ui ui, Storage storage) {
        tradeList.addTrade(addTrade);

        ui.showLine();
        System.out.println(addTrade.toSummaryString());
        System.out.println("Trade successfully added.");
        ui.showLine();
    }
}
