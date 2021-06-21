package oracle.apps.fnd.arcs.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oracle.apps.fnd.arcs.commands.CommandExecResults;
import oracle.apps.fnd.arcs.commands.CommandObj;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSSHManager {

	private String password = "";

	@Before
	public void setup() {

		System.out.println("Enter your session server password : ");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		try {
			password = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testChenv() {

		SSHManager sshManager = new SSHManager("rws3220122.us.oracle.com",
				"arpurush", password);
		List<CommandObj> commands = new ArrayList<CommandObj>();
		CommandObj command = new CommandObj(
				"/appldev/bin/chenv mob 12.0 mobdev", null);
		commands.add(command);
		command = new CommandObj("echo $mob", null);
		commands.add(command);
		List<CommandExecResults> commandExecResults = sshManager
				.execInShell(commands);
		CommandExecResults chenvResult = commandExecResults.get(0);
		Assert.assertEquals(0, chenvResult.getResultCode());
		CommandExecResults echoResult = commandExecResults.get(1);
		Assert.assertEquals(0, echoResult.getResultCode());
		Assert.assertTrue(echoResult.getCommandOutput().contains("/mobdev/mob/12.0"));
	}
	

}
