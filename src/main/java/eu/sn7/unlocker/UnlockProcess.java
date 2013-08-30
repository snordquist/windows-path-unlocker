package eu.sn7.unlocker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnlockProcess {
	private static final Pattern PROCESS_ID_PATTERN = Pattern.compile(".*pid[:][\\s]+([0-9]+)[\\s]+.*");
	private static final int PROCESSID_IN_PATTERN = 1;

	private File path;
	private List<String> processIds;
	private List<String> handleCommandOutputLines;

	public UnlockProcess(File path) {
		this.path = path;
		processIds = ListUtil.emptyList();
		handleCommandOutputLines = ListUtil.emptyList();
	}

	public void unlock() {
		findProcessIds();
		killProcesses();
	}

	private void killProcesses() {
		for (String processId : processIds) {
			killProcess(processId);
		}
	}

	private void killProcess(String processId) {
		String[] command = new String[] { "taskkill", "/F", "/PID", processId };
		try {
			Unlocker.log("kill " + processId);
			execCommand(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int execCommand(String[] command) throws InterruptedException, IOException {
		Process process = startCommand(command);
		return process.waitFor();
	}

	private void findProcessIds() {
		try {
			readLinesFromHandleCommand();
			parseProcessIdsFromHandleOutput(handleCommandOutputLines);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readLinesFromHandleCommand() throws IOException, InterruptedException {
		String[] command = getHandleCommand();
		Process process = startCommand(command);
		readLinesFromProcess(process);
		process.waitFor();
	}

	private void parseProcessIdsFromHandleOutput(List<String> outputLines) {
		for (String line : outputLines) {
			String processId = parseProcessId(line);
			addProcessIdIfNececcary(processId);
		}
	}

	private void addProcessIdIfNececcary(String processId) {
		if (shouldAddProcessId(processId, processIds)) {
			processIds.add(processId);
		}
	}

	private boolean shouldAddProcessId(String processId, List<String> processIds) {
		return processId != null && !processIds.contains(processId);
	}

	private String parseProcessId(String line) {
		Matcher matcher = PROCESS_ID_PATTERN.matcher(line);
		if (matcher.find()) {
			return matcher.group(PROCESSID_IN_PATTERN);
		}
		return null;
	}

	private void readLinesFromProcess(Process process) throws IOException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = getBufferedReaderFromProcess(process);
			addHandleOutputLines(bufferedReader);
		} finally {
			tryToCloseReader(bufferedReader);
		}
	}

	private void addHandleOutputLines(BufferedReader bufferedReader) throws IOException {
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			handleCommandOutputLines.add(line);
		}
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
		String handleCommandPath = Unlocker.HANDLE_CMD_PATH;
		return new String[] { handleCommandPath, path.getAbsolutePath() };
	}

	public boolean hasKilledProcesses() {
		return processIds.size() > 0;
	}
}
