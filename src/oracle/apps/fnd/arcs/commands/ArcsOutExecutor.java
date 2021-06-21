package oracle.apps.fnd.arcs.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.apps.fnd.arcs.log.Logger;
import oracle.apps.fnd.arcs.process.SSHManager;
import oracle.apps.fnd.arcs.utils.ProdTopInfo;

public class ArcsOutExecutor {
	
	private Logger logger;
	
	private ArcsJavaBridge arcsJavaBridge;
	
	private ProdTopInfo prodTopInfo;

	private String generateARCSOutROCommand(String filePath, String label){
        
        StringBuffer arcsOutROCommand = new StringBuffer();
        arcsOutROCommand.append(ArcsCommands.ARCS_EXECUTABLE);
        arcsOutROCommand.append(ArcsCommands.ARCS_OUT);
        arcsOutROCommand.append(filePath);
        arcsOutROCommand.append(ArcsCommands.ARCS_RO);
        if(null != label && "".equals(label)){
            arcsOutROCommand.append(ArcsCommands.ARCS_LABEL);
            arcsOutROCommand.append(label);
        }
        logger.logInfo("Executing command: " + arcsOutROCommand);
		return arcsOutROCommand.toString();
		
	}
	
	/**
	 * Constructor for ArcsOutExecutor class
	 * @param arcsJavaBridge: class which implements the interface ArcsJavaBridge
	 * @param prodTop: class which implements the ProdTopInfo interface
	 */
	public ArcsOutExecutor(ArcsJavaBridge arcsJavaBridge, ProdTopInfo prodTop) {
		
		this.arcsJavaBridge = arcsJavaBridge;
		this.prodTopInfo = prodTop;
		logger = arcsJavaBridge.getLogger();

	}
	
	/**
	 * This method will arcs out the file from the prodTop and drop it in the 
	 * location mentioned. It will arcs out the version of the file as mentioned in the
	 * label. If the label is null or empty, the latest file will be arcs'ed out
	 * @param prodTopRelativeFilePath: The location of the file relative to the prod top
	 * Example: If a file is located at $FND_TOP/java/oracle/apps/fnd/util/URLEncoder.java, then the
	 * value of prodTopRelativeFilePath should be set to java/oracle/apps/fnd/util/URLEncoder.java
	 * @param label: The version of the file to be arcs'ed out. If the latest version of the file needs
	 * to be arcs'ed out, then this variable can be set to null
	 * @param destination: The location where the arcs'ed out file needs to be store
	 * @return
	 */
	public List<CommandExecResults> arcsOutFile(String prodTopRelativeFilePath,
			String label, String destination){
		
		String hostname = arcsJavaBridge.getHostName();
		String userName = arcsJavaBridge.getUserName();
		String password = arcsJavaBridge.getPassword();
		
		SSHManager sshManager = new SSHManager(hostname, userName, password);
		List<CommandObj> commands = new ArrayList<CommandObj>();
		String chenvCommand = ArcsCommands.CHENV_EXECUTABLE + " " + prodTopInfo.getProdTopForChenvCommand();
		CommandObj command = new CommandObj(chenvCommand, null);
		commands.add(command);
		Map<String, String> promptqa = new HashMap<String, String>();
        promptqa.put(ArcsOutReturnMessages.FILE_OVERWRITE_QUESTION, "y");
        command = new CommandObj (generateARCSOutROCommand(prodTopRelativeFilePath, label), promptqa);
		commands.add(command);
		List<CommandExecResults> commandExecResults = sshManager
				.execInShell(commands);
		return commandExecResults;
	}
	
	class ArcsOutResultCodes{
		
		public static final int SUCCESS = 0;
		
		public static final int FILE_LOCKED_BY_DIFFERENT_USER = 1;
		
		public static final int FILE_UNAVAILABLE = 2;
		
		public static final int PERMISSION_DENIED = 3;
		
		public static final int TIME_OUT = 4;
	}
	
	class ArcsOutReturnMessages {
		
		public static final String FILE_OVERWRITE_QUESTION = "Do you want to overwrite it (y/n) [n] ?";
		
		
	}

}
