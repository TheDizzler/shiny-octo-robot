package vdindustries.reportsview;

import vdindustries.content.Deficiency;


public class ReportItem {
	
	public String		text;
	public boolean		checked;
	public String		reportID;
	public Deficiency	def;
	public Integer		position;
	
	
	public ReportItem() {
	
		super();
	}
	
	
	public ReportItem(boolean checked, String text, String reportID) {
	
		super();
		this.checked = checked;
		this.text = text;
		this.reportID = reportID;
		this.def = null;
		this.position = null;
		
	}
	
	public ReportItem(Deficiency defic) {
	
		super();
		this.checked = defic.completed;
		this.text = defic.toString();
		this.reportID = defic.reportID;
		this.def = defic;
		this.position = null;
		
		
	}
	
	public void setChecked(boolean check) {
	
		this.checked = check;
	}
	
	public boolean getChecked() {
	
		return this.checked;
	}
}
