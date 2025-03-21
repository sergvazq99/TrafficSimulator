package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.control.Controller;

public class Main {

	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static int _ticks = 10;
	private static boolean _viewMode=true;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksOption(line);
			parsemodeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10)").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("...").build());
		
		return cmdLineOptions;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		if(!_viewMode) {
			_outFile = line.getOptionValue("o");
		}
	}
	
	private static void parseTicksOption(CommandLine line) throws ParseException {
		if(line.hasOption("t")) {
			_ticks = 10;
			if (line.getOptionValue("t") != null)
				_ticks = Integer.parseInt(line.getOptionValue("t"));
		}
	}
	
	private static void parsemodeOption(CommandLine line) {
		if(line.hasOption("m")) {
			if(line.getOptionValue("t")=="console") {
				_viewMode=false;
			}
			else if(line.getOptionValue("t")=="gui") {
				_viewMode=true;
			}
		}
	}

	private static void initFactories() {
		// Factoria lssFactory
		List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add(new RoundRobinStrategyBuilder());
		lsbs.add(new MostCrowdedStrategyBuilder());
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		// Factoria dqStrategy
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveFirstStrategyBuilder() );
		dqbs.add( new MoveAllStrategyBuilder() );
		Factory<DequeuingStrategy> dqFactory = new BuilderBasedFactory<>(dqbs);
		
		// Construcción de factoria de eventos
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add(new NewJunctionEventBuilder(lssFactory, dqFactory));
		ebs.add(new NewCityRoadEventBuilder());
		ebs.add(new NewInterCityRoadEventBuilder());
		ebs.add(new NewVehicleEventBuilder());
		ebs.add(new SetContClassEventBuilder());
		ebs.add(new SetWeatherEventBuilder());
		_eventsFactory = new BuilderBasedFactory<>(ebs);
	}

	private static void startBatchMode() throws IOException {
		InputStream input = new FileInputStream(_inFile);
		OutputStream out;
		if(_outFile == null)
			out = System.out;
		else
			out = new FileOutputStream(_outFile);
		
		TrafficSimulator sim = new TrafficSimulator();
		Controller controller = new Controller(sim, _eventsFactory);
		controller.loadEvents(input);
		input.close();
		controller.run(_ticks, out);
		
	}
	
	private static void startGUIMode() {
		TrafficSimulator sim = new TrafficSimulator();
		Controller controller = new Controller(sim, _eventsFactory);
		if(_inFile!=null) {
			try {
				controller.loadEvents(new FileInputStream(_inFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new MainWindow(controller);
			}
		}); 
	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		if(_viewMode) {
			startGUIMode();
		}
		else {
			startBatchMode();
		}
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
