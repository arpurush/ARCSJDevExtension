package oracle.apps.fnd.arcs.process;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException;

public class SFTPManager {

    private String username;

    private String server;

    private String password;

    private static Session session = null;

    private static ChannelSftp channel = null;


    public SFTPManager(String server, String username, String password) {
        super();
        this.username = username;
        this.server = server;
        this.password = password;
    }

    private void createSession() {
        try {

            JSch jsch = new JSch();
            if (null == session || !session.isConnected()) {
                session = jsch.getSession(username, server, 22);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setPassword(password);
                session.connect();
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getRemoteFile(String sourceDirRelativeToHomeDir, String fileName, String localDestDir) {

        createSession();
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            String homeDir = channel.getHome();
            String sourceDir = homeDir + "/" + sourceDirRelativeToHomeDir;
            createSourceDirAndChangePermissions(sourceDir, "777");
			String sourceFileName = sourceDir + "/" + fileName;
            String destinationFile = localDestDir + "/" + fileName;
            checkAndCreateLocalFile(localDestDir, fileName);
            SftpProgressMonitorImpl progressMonitor = new SftpProgressMonitorImpl();                
            channel.get(sourceFileName, destinationFile, progressMonitor);
            if(progressMonitor.isTransferSuccess()){
                return true;
            }else{
                return false;
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException sftpe) {
            sftpe.printStackTrace();
        } finally {
            if (null != channel) {
                channel.disconnect();
            }
            if (null != session) {
                session.disconnect();
            }
        }

        return false;

    }
    
    public boolean sendFileToServer(String sourceDir, String fileName, String remoteDirRelativeToHome){
    	createSession();
        try {
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            String homeDir = channel.getHome();
            String destDir = homeDir + "/" + remoteDirRelativeToHome;
            createSourceDirAndChangePermissions(destDir, "777");
			String sourceFileName = sourceDir + "/" + fileName;
            String destinationFile = destDir + "/" + fileName;
            SftpProgressMonitorImpl progressMonitor = new SftpProgressMonitorImpl();                
            channel.put(sourceFileName, destinationFile, progressMonitor);
            if(progressMonitor.isTransferSuccess()){
                return true;
            }else{
                return false;
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException sftpe) {
            sftpe.printStackTrace();
        } finally {
            if (null != channel) {
                channel.disconnect();
            }
            if (null != session) {
                session.disconnect();
            }
        }
        
        return false;
    }

    public boolean createSourceDirAndChangePermissions(String sourceDir, String permissions) {
		
    	try{
    		channel.mkdir(sourceDir);
    		channel.chmod(Integer.parseInt(permissions, 8), sourceDir);
    	}catch(SftpException sftpException){
    		if(sftpException.id == ChannelSftp.SSH_FX_FAILURE){
    			System.out.println("Directory already exists");
    		}else{
    			sftpException.printStackTrace();
    			return false;
    		}
    	}
    	
    	return true;
	}

	private void checkAndCreateLocalFile(String localDestinationDir, String fileName) {

        File localDir = new File(localDestinationDir);
        File destinationFile = new File(localDestinationDir + "/" + fileName);
        try {
            if (!localDir.exists()) {
                localDir.mkdirs();
                destinationFile.createNewFile();
            }
            if (!destinationFile.exists()) {
                destinationFile.createNewFile();
            }
        } catch (IOException ioe) {
            // TODO: Add catch code
            ioe.printStackTrace();
        }

    }

    public static void main(String[] args) {

        SFTPManager sftpmanager = new SFTPManager("rws3220122.us.oracle.com", "arpurush", "");
        if (sftpmanager.getRemoteFile("tmp", "EBSMobile-1.0.css", "/Users/arpurush/.ARCSHelper")) {
            System.out.println("file sftp'ed successfully");
        } else {
            System.out.println("file sftp'ed with a failure");
        }
    }


    class CommandResult {

        String command;

        String commandResult;

        CommandResult(String command, String commandResult) {
            this.command = command;
            this.commandResult = commandResult;
        }
    }

}
