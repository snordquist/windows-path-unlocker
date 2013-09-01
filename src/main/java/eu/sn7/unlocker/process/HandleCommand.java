package eu.sn7.unlocker.process;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.sn7.unlocker.util.CommandUtil;

public class HandleCommand implements ExecCommand {
	private static final String HANDLE_CMD_PATH_PROPERTY = "unlocker.handlepath";
	private static final String DEFAULT_HANDLE_COMMAND_PATH = "handle";
	private static final String HANDLE_CMD_PATH = getHandleCommandPath();
	private File path;

	public HandleCommand(File path) {
		this.path = path;
	}

	public List<String> getOutputLines() throws IOException, InterruptedException {
		String[] command = getHandleCommand();
		Process process = CommandUtil.startCommand(command);
		List<String> outputLines = CommandUtil.readLinesFromProcess(process);
		process.waitFor();
		return outputLines;
	}

	private String[] getHandleCommand() {
		return new String[] { HANDLE_CMD_PATH, path.getAbsolutePath() };
	}

	private static String getHandleCommandPath() {
		String pathFromProperty = System.getProperty(HANDLE_CMD_PATH_PROPERTY);
		if (pathFromProperty == null) {
			return DEFAULT_HANDLE_COMMAND_PATH;
		}
		return pathFromProperty;
	}
}
