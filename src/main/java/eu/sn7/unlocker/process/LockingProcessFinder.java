package eu.sn7.unlocker.process;

import java.io.IOException;
import java.util.List;

import eu.sn7.unlocker.util.ListUtil;

public class LockingProcessFinder implements ProcessFinder {
	private ProcessIdParser processIdParser;
	private ExecCommand processFinderCommand;

	public LockingProcessFinder(ExecCommand processFinderCommand, ProcessIdParser processIdParser) {
		this.processFinderCommand = processFinderCommand;
		this.processIdParser = processIdParser;
	}

	public List<String> findProcessIds() {
		try {
			return tryToFindProcessIds();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ListUtil.emptyList();
	}

	private List<String> tryToFindProcessIds() throws IOException, InterruptedException {
		List<String> outputLines = readLinesFromHandleCommand();
		List<String> processIds = parseProcessIdsFromHandleOutput(outputLines);
		return processIds;
	}

	private List<String> readLinesFromHandleCommand() throws IOException, InterruptedException {
		return processFinderCommand.getOutputLines();
	}

	private List<String> parseProcessIdsFromHandleOutput(List<String> outputLines) {
		List<String> processIds = ListUtil.emptyList();
		for (String line : outputLines) {
			String processId = processIdParser.parseProcessId(line);
			if (shouldAddProcessId(processId, processIds)) {
				processIds.add(processId);
			}
		}
		return processIds;
	}

	private boolean shouldAddProcessId(String processId, List<String> processIds) {
		return processId != null && !processIds.contains(processId);
	}

}
