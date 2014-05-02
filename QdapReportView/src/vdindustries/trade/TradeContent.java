package vdindustries.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TradeContent {
	
    public static List<Trade> trades = new ArrayList<Trade>();

    
    public static Map<String, Trade> TRADE_MAP = new HashMap<String, Trade>();

    static {
        // Rough-ins
        addItem(new Trade("1", "Framing"));
        addItem(new Trade("2", "Mechanical"));
        addItem(new Trade("3", "Electrical"));
        addItem(new Trade("4", "Security"));
        addItem(new Trade("5", "HVAC"));
        addItem(new Trade("6", "Insulation"));
        //Finishes
        addItem(new Trade("7", "Drywall"));
        addItem(new Trade("8", "Paint"));
        addItem(new Trade("9", "Mechanical"));
        addItem(new Trade("10", "Electrical"));
        addItem(new Trade("11", "Electrical"));
        addItem(new Trade("12", "Flooring"));
        addItem(new Trade("13", "Cabinets"));
        addItem(new Trade("14", "Countertops"));
        addItem(new Trade("15", "Finish Carpentry"));
    }

    private static void addItem(Trade item) {
    	trades.add(item);
    	TRADE_MAP.put(item.id, item);
    }

    
    public static class Trade {
        public String id;
        public String content;

        public Trade(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}