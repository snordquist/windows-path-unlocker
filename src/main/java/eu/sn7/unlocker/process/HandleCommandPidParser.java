package eu.sn7.unlocker.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleCommandPidParser implements ProcessIdParser {
	private static final Pattern PROCESS_ID_PATTERN = Pattern.compile(".*pid[:][\\s]+([0-9]+)[\\s]+.*");
	private static final int PROCESSID_IN_PATTERN = 1;

	public String parseProcessId(String line) {
		Matcher matcher = PROCESS_ID_PATTERN.matcher(line);
		if (matcher.find()) {
			return matcher.group(PROCESSID_IN_PATTERN);
		}
		return null;
	}

}
