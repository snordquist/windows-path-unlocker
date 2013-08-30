package eu.sn7.unlocker;

import java.io.File;

public class Unlocker {
	private static final String HANDLE_CMD_PATH_PROPERTY = "unlocker.handlepath";
	private static final String DEFAULT_HANDLE_COMMAND_PATH = "handle";
	public static final String HANDLE_CMD_PATH = getHandleCommandPath();

	private File path;

	public Unlocker(File path) {
		this.path = path;
	}

	public void unlock() {
		UnlockProcess unlockProcess = new UnlockProcess(path);
		unlockProcess.unlock();

		retryUnlockIfNecessary(unlockProcess);
	}

	private void retryUnlockIfNecessary(UnlockProcess unlockProcess) {
		if (unlockProcess.hasKilledProcesses()) {
			unlock();
		}
	}

	private static String getHandleCommandPath() {
		String pathFromProperty = System.getProperty(HANDLE_CMD_PATH_PROPERTY);
		if (pathFromProperty == null) {
			return DEFAULT_HANDLE_COMMAND_PATH;
		}
		return pathFromProperty;
	}

	public static void log(String string) {
		System.out.println(string);
	}

}
