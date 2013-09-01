package eu.sn7.unlocker.process;

import java.util.logging.Logger;

import eu.sn7.unlocker.util.CommandUtil;

public class TaskKill implements ProcessKiller {
	Logger logger = Logger.getLogger(getClass().getName());

	public void killProcess(String processId) {
		String[] command = new String[] { "taskkill", "/F", "/PID", processId };
		try {
			logger.info("kill " + processId);
			CommandUtil.execCommand(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
