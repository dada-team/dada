package main.java.controllers.launchers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import main.java.model.interfaces.WebPage;

public class CommandParser {
	/**
	 * Main method : launch at first
	 * 
	 * @param args
	 *            command line arguments
	 */
	static Logger logger = Logger.getLogger("main.java.controllers.launchers.CommandParser");
	static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public static Options getOptions() {
		Options options = new Options();
		options.addOption("help", "h", false, "help");
		options.addOption("dtstart", "ds", true, "");
		options.addOption("dtend", "ds", true, "");
		options.addOption("o", "o", true, "json output dir");

		return options;
	}

	
	private static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		String programName = new java.io.File(
				CommandParser.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
		formatter.printHelp(programName, getOptions());
	}
	/**
	 * 
	 * @param cmd
	 * @throws Exception 
	 */
	private static void process(CommandLine cmd) throws Exception {
		if (cmd.hasOption("help") || (!cmd.hasOption("dtstart") || (!cmd.hasOption("dtend")))
				|| (!cmd.hasOption("o"))) {
			 printHelp();
		} else {
			
			try {
				DateTime dtStart = parseDateTime(cmd.getOptionValue("dtstart"));
				DateTime dtEnd = parseDateTime(cmd.getOptionValue("dtend"));
				File output = new File(cmd.getOptionValue("o"));
				
				if(!output.isDirectory())
				{
					throw new ParseException("output directory does not exist (or is a file)");
				}
				
				processLaunch(output, dtStart, dtEnd);
			} catch (ParseException e) {
				logger.error(e);
				printHelp();
			} catch (Exception e) {
				logger.error(e);
				throw e;
			}
		}
	}

	private static DateTime parseDateTime(String optionValue) throws Exception {
		// TODO Auto-generated method stub
		DateTime dt = null;
		
		try {
			dt = formatter.parseDateTime(optionValue);
		} catch (Exception e) {
			logger.error(e);
			throw new ParseException("erreur format date : yyyy-MM-dd demande");
		}
		
		return dt;
	}

	/**
	 * 
	 * @param dtStart
	 * @param dtEnd
	 * @throws Exception 
	 */
	private static void processLaunch(File output, DateTime dtStart, DateTime dtEnd) throws Exception {
		TurfoRaceLauncher trl = new TurfoRaceLauncher(output, dtStart, dtEnd);
		trl.loadAllData();
		trl.writeAllData();
	}

	/**
	 * 
	 */
	private static void processLaunchMultiThreaded(File output, DateTime dtStart, DateTime dtEnd)
			throws InterruptedException, ExecutionException {

		int availableProcessors = Runtime.getRuntime().availableProcessors();

		if ((Days.daysBetween(dtStart, dtEnd).getDays() > 600) && (availableProcessors > 4)) {
			processLaunchMultiThreaded(output, dtStart, dtEnd, 4);
		} else if ((Days.daysBetween(dtStart, dtEnd).getDays() > 400) && (availableProcessors > 3)) {
			processLaunchMultiThreaded(output, dtStart, dtEnd, 3);
		} else if ((Days.daysBetween(dtStart, dtEnd).getDays() > 200) && (availableProcessors > 2)) {
			processLaunchMultiThreaded(output, dtStart, dtEnd, 2);
		} else {
			processLaunchMultiThreaded(output, dtStart, dtEnd, 1);
		}

		return;
	}

	/**
	 * 
	 * @param dtStart
	 * @param dtEnd
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private static void processLaunchMultiThreaded(File output, DateTime dtStart, DateTime dtEnd, int nbOfThreads)
			throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newFixedThreadPool(nbOfThreads);
		List<Future<List<WebPage>>> futures = new ArrayList<Future<List<WebPage>>>();

		/**
		 * 1 get data
		 */
		for (int i = 0; i < nbOfThreads; i++) {
			futures.add(service.submit(new TurfoRaceLauncher(output, dtStart, dtEnd)));
		}

		List<WebPage> webPages = new ArrayList<WebPage>();

		for (Future<List<WebPage>> future : futures) {
			webPages.addAll(extract(future));
		}

		/**
		 * 2 write data
		 */
		//write all webpages
		logger.debug("TODO");

		return;
	}

	private static List<WebPage> extract(Future<List<WebPage>> future) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		List<WebPage> webPages = null;
		try {
			webPages = future.get();
		} catch (InterruptedException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			throw e;
		} catch (ExecutionException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			throw e;
		}

		return webPages;
	}

	/**
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Options options = getOptions();
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			process(cmd);
		} catch (UnrecognizedOptionException e) {
			logger.error(e);
			HelpFormatter formatter = new HelpFormatter();
			String programName = new java.io.File(
							CommandParser.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();

			formatter.printHelp(programName, getOptions());
		}
	}

}