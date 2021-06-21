package oracle.apps.fnd.arcs.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestSFTPManager {
	
	private String password = "";

	@Before
	public void setup(){
		System.out.println("Enter your session server password :");
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
	public void testPutAndGet() {
		SFTPManager sftpmanager = new SFTPManager("rws3220122.us.oracle.com", "arpurush", password);
		String fileName = "test.txt";
		String destinationDir = "/Users/arpurush/.ARCSHelper/";
		Assert.assertTrue(sftpmanager.sendFileToServer("/Users/arpurush/tmp", "test.txt", "tmp"));
		Assert.assertTrue(sftpmanager.getRemoteFile("tmp", fileName, destinationDir));
        Assert.assertTrue(new File(destinationDir+fileName).exists());
	}

}
