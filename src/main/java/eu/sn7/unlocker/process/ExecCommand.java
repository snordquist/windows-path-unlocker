package eu.sn7.unlocker.process;

import java.io.IOException;
import java.util.List;

public interface ExecCommand {
	public List<String> getOutputLines() throws IOException, InterruptedException;
}