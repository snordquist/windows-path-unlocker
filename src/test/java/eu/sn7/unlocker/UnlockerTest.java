package eu.sn7.unlocker;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.sn7.unlocker.process.ExecCommand;
import eu.sn7.unlocker.process.HandleCommandPidParser;
import eu.sn7.unlocker.process.LockingProcessFinder;
import eu.sn7.unlocker.process.ProcessFinder;
import eu.sn7.unlocker.process.ProcessKiller;
import eu.sn7.unlocker.util.ListUtil;

@RunWith(MockitoJUnitRunner.class)
public class UnlockerTest {
	private final int NUMBER_OF_TRIES = 5;

	@Mock
	ExecCommand processFinderCommand;

	@Mock
	ProcessKiller processKiller;

	private Unlocker unlocker;

	@Before
	public void setUp() {
		HandleCommandPidParser processIdParser = new HandleCommandPidParser();
		ProcessFinder processListFinder = new LockingProcessFinder(processFinderCommand, processIdParser);
		unlocker = new Unlocker(processListFinder, processKiller, NUMBER_OF_TRIES);
	}

	@Test
	public void testUnlockerLoop() throws Exception {
		String line1 = "cmd.exe            pid: 248    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir";
		String line2 = "java.exe            pid: 2493    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir";
		String line3 = "java.exe            pid: 2493    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir\\file.jar";
		when(processFinderCommand.getOutputLines()).thenReturn(ListUtil.create(line1, line2, line3));

		unlocker.unlock();

		int numberOfDifferentPids = 2;
		verify(processKiller, times(NUMBER_OF_TRIES * numberOfDifferentPids)).killProcess(any(String.class));
	}

	@Test
	public void testEmptyProcessList() throws Exception {
		List<String> emptyList = ListUtil.emptyList();
		when(processFinderCommand.getOutputLines()).thenReturn(emptyList);

		unlocker.unlock();

		verify(processKiller, never()).killProcess(any(String.class));
	}

}
