package vdindustries.reportsview;


public class ReportItem {
	public String text;
	public boolean checked;
	public String reportID;

	public ReportItem() {
		super();
	}

	public ReportItem(boolean checked, String text, String reportID) {
		super();
		this.checked = checked;
		this.text = text;
		this.reportID = reportID;
	}
}
