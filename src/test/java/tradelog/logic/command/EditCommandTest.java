package tradelog.logic.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tradelog.exception.TradeLogException;
import tradelog.model.Trade;
import tradelog.model.TradeList;

/**
 * Test suite for EditCommand ensuring atomicity and directional logic.
 */
public class EditCommandTest {
    private TradeList tradeList;

    @BeforeEach
    public void setUp() {
        tradeList = new TradeList();
        // Initial state: AAPL, Long, Entry 150.0, Stop Loss 145.0
        tradeList.addTrade(new Trade("AAPL", "2026-03-18", "Long", 150.0, 160.0, 145.0, "Win", "Trend"));
    }

    /**
     * Tests that an invalid direction string is caught and original state is preserved.
     */
    @Test
    public void execute_invalidDirectionString_noChanges() throws TradeLogException {
        EditCommand edit = new EditCommand("1 dir/invalid");
        edit.execute(tradeList, null, null);
        assertEquals("Long", tradeList.getTrade(0).getDirection());
    }

    /**
     * Tests that for a Long trade, setting Stop Loss >= Entry results in an error and no update.
     */
    @Test
    public void execute_invalidLongRisk_noChanges() throws TradeLogException {
        // Attempt to move Stop Loss to 155 (above Entry 150)
        EditCommand edit = new EditCommand("1 s/155.0");
        edit.execute(tradeList, null, null);
        assertEquals(145.0, tradeList.getTrade(0).getStopLossPrice());
    }

    /**
     * Tests that for a Short trade, setting Entry >= Stop Loss results in an error.
     */
    @Test
    public void execute_invalidShortRisk_noChanges() throws TradeLogException {
        // Add a Short: Entry 200, Stop Loss 210
        tradeList.addTrade(new Trade("TSLA", "2026-03-18", "Short", 200.0, 180.0, 210.0, "Win", "Trend"));
        EditCommand edit = new EditCommand("2 e/220.0"); // Entry 220 > Stop 210 (Invalid for Short)
        edit.execute(tradeList, null, null);
        assertEquals(200.0, tradeList.getTrade(1).getEntryPrice());
    }

    /**
     * Confirms that if validation fails, even other correct fields (like Ticker) are NOT updated.
     */
    @Test
    public void execute_atomicUpdateFailure_tickerNotChanged() throws TradeLogException {
        // Change ticker to 'GOOG' but set invalid Stop Loss (155)
        EditCommand edit = new EditCommand("1 t/GOOG s/155.0");
        edit.execute(tradeList, null, null);
        assertEquals("AAPL", tradeList.getTrade(0).getTicker());
    }
}