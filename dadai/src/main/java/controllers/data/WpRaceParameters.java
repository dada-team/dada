package main.java.controllers.data;

public class WpRaceParameters {

	protected static final String URL_RACE_PATTERN_REGEXP = ".*/rapports-[^-]+-([^-]+-[^-]+-[^-]+)-(.+)\\.html\\?idcourse=([0-9]+)";
	protected static final String HORSE_URL_SELECT = "table.tableauLine tbody tr td:eq(2) a[href]";
	protected static final String HORSE_JOCKEY_SELECT = "table.tableauLine tbody tr td:eq(3) a[href]";
	protected static final String HORSE_TRAINER_SELECT = "table.tableauLine tbody tr td:eq(4) a[href]";
	protected static final String HORSE_COTES_SELECT = "table.tableauLine tbody tr td:eq(6)";
	protected static final String RACE_DESCRIPTION_SELECT = "div.detailCourseCaract p";
	public static final int TIMED_OUT_MILLI = 8000;
	public static final String DETAIL_URL_SELECT = "div#onglets ul li:eq(0) a[href]";
}
