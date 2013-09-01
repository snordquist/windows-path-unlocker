package eu.sn7.unlocker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CommandUtil {
	public static int execCommand(String[] command) throws InterruptedException, IOException {
		Process process = startCommand(command);
		return process.waitFor();
	}

	public static Process startCommand(String[] command) throws IOException {
		return Runtime.getRuntime().exec(command);
	}

	public static List<String> readLinesFromProcess(Process process) throws IOException {
		BufferedReader bufferedReader = null;
		List<String> lines = ListUtil.emptyList();
		try {
			bufferedReader = getBufferedReaderFromProcess(process);
			addHandleOutputLines(bufferedReader, lines);
		} finally {
			tryToCloseReader(bufferedReader);
		}
		return lines;
	}

	private static BufferedReader getBufferedReaderFromProcess(Process process) {
		return new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	private static List<String> addHandleOutputLines(BufferedReader bufferedReader, List<String> lines) throws IOException {
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}

	private static void tryToCloseReader(BufferedReader bufferedReader) throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}
}
