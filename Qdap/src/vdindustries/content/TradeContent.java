package vdindustries.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeContent {

	public static List<TradeItem> ITEMS = new ArrayList<TradeItem>();

	public static Map<String, TradeItem> ITEM_MAP = new HashMap<String, TradeItem>();

	static {

		addItem(new TradeItem("1", "Framing"));
		addItem(new TradeItem("2", "Mechanical"));
		addItem(new TradeItem("3", "Electrical"));
		addItem(new TradeItem("4", "Security"));
		addItem(new TradeItem("5", "HVAC"));
		addItem(new TradeItem("6", "Insulation"));

		addItem(new TradeItem("7", "Drywall"));
		addItem(new TradeItem("8", "Paint"));
		addItem(new TradeItem("9", "Mechanical(f)"));
		addItem(new TradeItem("10", "Electrical(f)"));
		addItem(new TradeItem("11", "Flooring"));
		addItem(new TradeItem("12", "Cabinets"));
		addItem(new TradeItem("13", "Countertops"));
		addItem(new TradeItem("14", "Finish Carp."));
	}

	private static void addItem(TradeItem item) {

		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static void reloadDeficiencies() {

		for (TradeItem trade : ITEMS) {

			trade.reloadDeficiencies();
		}
	}

	/** A Trade list item that holds all deficiencies under this trade. */
	public static class TradeItem {

		public String id;
		public String trade;
		/* All project deficiencies for this trade. */
		public List<Deficiency> deficiencies;

		public TradeItem(String id, String trade) {

			this.id = id;
			this.trade = trade;
			this.deficiencies = DeficiencyParser.getDefsByTrade(trade);
		}

		@Override
		public String toString() {

			return trade;
		}

		private void reloadDeficiencies() {

			this.deficiencies = DeficiencyParser.getDefsByTrade(trade);
		}
	}
}
