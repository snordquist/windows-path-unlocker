package eu.sn7.unlocker;

import java.io.File;

import eu.sn7.unlocker.process.HandleCommand;
import eu.sn7.unlocker.process.HandleCommandPidParser;
import eu.sn7.unlocker.process.LockingProcessFinder;
import eu.sn7.unlocker.process.ProcessKiller;
import eu.sn7.unlocker.process.TaskKill;

public class Main {
	private static final int ARGUMENT_PATH_INDEX = 0;
	private static final int NUMBER_OF_TRIES = 10;

	public static void main(String[] args) {
		if (pathIsOk(args)) {
			File path = new File(args[ARGUMENT_PATH_INDEX]);
			LockingProcessFinder lockingProcessFinder = new LockingProcessFinder(new HandleCommand(path),
					new HandleCommandPidParser());
			ProcessKiller processKiller = new TaskKill();
			Unlocker unlocker = new Unlocker(lockingProcessFinder, processKiller, NUMBER_OF_TRIES);
			unlocker.unlock();
		} else {
			printHelp();
		}
	}

	private static boolean pathIsOk(String[] args) {
		return args.length > 0 && pathExists(args[ARGUMENT_PATH_INDEX]);
	}

	private static boolean pathExists(String string) {
		return (new File(string)).exists();
	}

	private static void printHelp() {
		log("java -jar <jarname>.jar <path to unlock>");
	}

	private static void log(String line) {
		System.out.println(line);
	}

}
