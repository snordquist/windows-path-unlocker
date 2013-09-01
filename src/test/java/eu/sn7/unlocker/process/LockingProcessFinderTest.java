package eu.sn7.unlocker.process;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.sn7.unlocker.process.ExecCommand;
import eu.sn7.unlocker.process.HandleCommandPidParser;
import eu.sn7.unlocker.process.LockingProcessFinder;
import eu.sn7.unlocker.process.ProcessIdParser;
import eu.sn7.unlocker.util.ListUtil;

@RunWith(MockitoJUnitRunner.class)
public class LockingProcessFinderTest {

	@Mock
	ExecCommand execCommand;

	@Test
	public void testUniqueListOfProcesses() throws Exception {
		when(execCommand.getOutputLines())
				.thenReturn(
						ListUtil.create(
								"cmd.exe            pid: 248    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir",
								"java.exe            pid: 2493    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir",
								"java.exe            pid: 2493    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir\\file.jar"));

		ProcessIdParser processIdParser = new HandleCommandPidParser();
		LockingProcessFinder processFinder = new LockingProcessFinder(execCommand, processIdParser);

		assertThat(processFinder.findProcessIds(), hasItems("248", "2493"));
	}
}
