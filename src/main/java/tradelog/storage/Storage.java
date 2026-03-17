package tradelog.storage;

import tradelog.model.Trade;
import tradelog.model.TradeList;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles reading and writing of trade data to and from a file.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a Storage instance with the specified file path.
     * 
     * @param filePath
    */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the given TradeList to the file specified by filePath.
     * 
     * @param tradeList
     */
    public void saveTrades(TradeList tradeList) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < tradeList.size(); i++) {
                Trade trade = tradeList.getTrade(i);
                writer.write(trade.toStorageString());
                writer.write("\n");
            }
            
            System.out.println("Trades saved successfully!");
        } catch (IOException e) {
            System.err.println("Failed to save trades: " + e.getMessage());
        }
    }
}
