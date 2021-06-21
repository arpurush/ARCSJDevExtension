package oracle.apps.fnd.arcs.commands;

public class CommandExecResults {
	String command;
    
    private String commandOutput;
    
    private int resultCode;
    
    public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCommandOutput() {
		return commandOutput;
	}

	public void setCommandOutput(String commandOutput) {
		this.commandOutput = commandOutput;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public CommandExecResults(String command, String commandOutput, int resultCode){
        
        this.command = command;
        this.commandOutput = commandOutput;
        this.resultCode = resultCode;
        
    }
    
    public String toString() {
        String toString = "\n **** COMMAND : " + command + " EXECUTED " + ((resultCode == 0) ? " SUCCESSFULLY " : " WITH A FAILURE ");
        toString = toString + " AND WITH THE MESSAGE ****\n" + commandOutput;
        return toString;
    }
}
