package eu.sn7.unlocker.process;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.sn7.unlocker.process.HandleCommandPidParser;

public class HandleCommandPidParserTest {
	@Test
	public void testParseProcessId() {
		String line = "java.exe            pid: 2493    type: File            74: C:\\Dokumente und Einstellungen\\user\\Desktop\\somedir\\file.jar";
		HandleCommandPidParser parser = new HandleCommandPidParser();
		assertThat(parser.parseProcessId(line), is("2493"));
	}

	@Test
	public void testParseInvalidString() {
		String line = "some string";
		HandleCommandPidParser parser = new HandleCommandPidParser();
		assertThat(parser.parseProcessId(line), is(nullValue()));
	}
}
