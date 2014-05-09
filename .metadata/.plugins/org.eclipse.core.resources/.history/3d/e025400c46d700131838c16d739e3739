package vdindustries.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class TradeContent {
	
	public static List<TradeItem>			ITEMS		= new ArrayList<TradeItem>();
	
	public static Map<String, TradeItem>	ITEM_MAP	= new HashMap<String, TradeItem>();
	
	static {
		
		addItem(new TradeItem("1", "Framing"));
		addItem(new TradeItem("2", "Mechanical"));
		addItem(new TradeItem("3", "Electrical"));
		addItem(new TradeItem("4", "Security"));
		addItem(new TradeItem("5", "HVAC"));
		addItem(new TradeItem("6", "Insulation"));
		
		addItem(new TradeItem("7", "Drywall"));
		addItem(new TradeItem("8", "Paint"));
		addItem(new TradeItem("9", "Mechanical"));
		addItem(new TradeItem("10", "Electrical"));
		addItem(new TradeItem("11", "Flooring"));
		addItem(new TradeItem("12", "Cabinets"));
		addItem(new TradeItem("13", "Countertops"));
		addItem(new TradeItem("14", "Finish Carp."));
	}
	
	
	private static void addItem(TradeItem item) {
	
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}
	
	
	
	/** A dummy item representing a piece of content. */
	public static class TradeItem {
		
		public String	id;
		public String	trade;
		/* All project deficiencies for this trade. */
		public List<Deficiency> deficiencies;
		
		
		public TradeItem(String id, String trade) {
		
			this.id = id;
			this.trade = trade;
			this.deficiencies = DeficiencyParser.getDefByTradeList(trade);
		}
		
		@Override public String toString() {
		
			return trade;
		}
	}
}
