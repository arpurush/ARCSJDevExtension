package oracle.apps.fnd.arcs.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandObj {
    
    /**
     * The command which needs to be executed
     */
    private String command;
    
    /**
     * The map contains a key which is a prompt question and the value is the answer to the prompt question
     */
    private Map<String, String> promptqa = new HashMap<String, String>();
    
    public CommandObj(String command, Map<String, String> promptqa) {
        super();
        this.command = command;
        this.promptqa = promptqa;
    }
    
    public String getCommand(){
        return command;
    }
    
    public Map<String, String> getPromptqa(){
        return promptqa;
    }
    
    
}
