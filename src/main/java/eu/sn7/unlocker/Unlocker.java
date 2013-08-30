package eu.sn7.unlocker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unlocker {
	private static final String DEFAULT_HANDLE_COMMAND_PATH = "handle";
	private static final String HANDLE_CMD_PATH_PROPERTY = "unlocker.handlepath";
	private static final String HANDLE_CMD_PATH = System.getProperty(HANDLE_CMD_PATH_PROPERTY);
	private static final Pattern PROCESS_ID_PATTERN = Pattern.compile(".*pid[:][\\s]+([0-9]+)[\\s]+.*");
	private static final int FIRST_PATTERN_RESULT = 1;
	private File path;

	public Unlocker(File path) {
		this.path = path;
	}

	public void unlock() {
		List<String> processIds = findProcessIds();
		killProcesses(processIds);
		retryUnlockIfNecessary(processIds);
	}

	private void retryUnlockIfNecessary(List<String> processIds) {
		if (processIds.size() > 0) {
			unlock();
		}
	}

	private void killProcesses(List<String> processIds) {
		for (String processId : processIds) {
			killProcess(processId);
		}
	}

	private void killProcess(String processId) {
		String[] command = new String[] { "taskkill", "/F", "/PID", processId };
		try {
			log("kill " + processId);
			execCommand(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int execCommand(String[] command) throws InterruptedException, IOException {
		Process process = startCommand(command);
		return process.waitFor();
	}

	private List<String> findProcessIds() {
		List<String> lines;
		try {
			lines = readLinesFromHandleCommand();
			return parseProcessIdsFromHandleOutput(lines);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ListUtil.emptyList();
	}

	private List<String> readLinesFromHandleCommand() throws IOException, InterruptedException {
		String[] command = getHandleCommand();
		Process process = startCommand(command);
		List<String> lines = readLinesFromProcess(process);
		process.waitFor();
		return lines;
	}

	private List<String> parseProcessIdsFromHandleOutput(List<String> outputLines) {
		List<String> processIds = ListUtil.emptyList();
		for (String line : outputLines) {
			String processId = parseProcessId(line);
			if (shouldAddProcessId(processId, processIds)) {
				processIds.add(processId);
			}
		}
		return processIds;
	}

	private boolean shouldAddProcessId(String processId, List<String> processIds) {
		return processId != null && !processIds.contains(processId);
	}

	private String parseProcessId(String line) {
		Matcher matcher = PROCESS_ID_PATTERN.matcher(line);
		if (matcher.find()) {
			return matcher.group(FIRST_PATTERN_RESULT);
		}
		return null;
	}

	private List<String> readLinesFromProcess(Process process) throws IOException {
		List<String> lines = ListUtil.emptyList();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getBufferedReaderFromProcess(process);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			tryToCloseReader(bufferedReader);
		}
		return lines;
	}

	private void tryToCloseReader(BufferedReader bufferedReader) throws IOException {
		if (bufferedReader != null) {
			bufferedReader.close();
		}
	}

	private BufferedReader getBufferedReaderFromProcess(Process process) {
		return new BufferedReader(new InputStreamReader(process.getInputStream()));
	}

	private Process startCommand(String[] command) throws IOException {
		return Runtime.getRuntime().exec(command);
	}

	private String[] getHandleCommand() {
		String handleCommandPath = getHandleCommandPath();
		return new String[] { handleCommandPath, path.getAbsolutePath() };
	}

	private String getHandleCommandPath() {
		String pathFromProperty = HANDLE_CMD_PATH;
		if (pathFromProperty == null) {
			return DEFAULT_HANDLE_COMMAND_PATH;
		}
		return pathFromProperty;
	}

	private static void log(String line) {
		System.out.println(line);
	}

}
