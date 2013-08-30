package eu.sn7.unlocker;

import java.io.File;

public class Main {
	private static final int ARGUMENT_PATH_INDEX = 0;

	public static void main(String[] args) {
		if (pathIsOk(args)) {
			File path = new File(args[ARGUMENT_PATH_INDEX]);
			Unlocker unlocker = new Unlocker(path);
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
