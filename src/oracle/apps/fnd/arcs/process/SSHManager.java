package oracle.apps.fnd.arcs.process;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import oracle.apps.fnd.arcs.commands.CommandExecResults;
import oracle.apps.fnd.arcs.commands.CommandObj;


public class SSHManager {

    private String username;

    private String server;

    private String password;
    
    private String homePrompt;
    
    public static final String STATUS_COMMAND = "echo $?";

    public SSHManager(String server, String username, String password) {
        super();
        this.username = username;
        this.server = server;
        this.password = password;
        homePrompt = username + "-" ;
    }

    private static Session session = null;

    private static Channel channel = null;

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


    public List<CommandExecResults> execInShell(List<CommandObj> commandObjs) {

        List<CommandExecResults> commandsOutput = new ArrayList<CommandExecResults>();
        PrintStream oldOutStream = System.out;
        ByteArrayOutputStream baos = null;
        try {
            createSession();
            ChannelShell channel = (ChannelShell) session.openChannel("shell");
            baos = new ByteArrayOutputStream(); 

            PrintStream ps = new PrintStream(baos);
            channel.setOutputStream(ps); 
            PrintStream shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience 
            channel.connect(); 
            String output = baos.toString();
            String lastLine = "";
            String prompt = homePrompt;
            baos.reset();
            for (CommandObj commandObj : commandObjs) {
            
                Map<String, String> promptqa = commandObj.getPromptqa();
                String command = commandObj.getCommand();
                if(command.contains("cd")){
                    prompt = getNewprompt(command);
                }
                shellStream.println(command); 
                shellStream.flush();
               
                while(!lastLine.equals(prompt) ){
                    try {
                            Thread.sleep(100);
                            output = baos.toString();
                            
                            List<String> outputLines = getOutputLines(output);
                            int lineSize = outputLines.size();
                            if(lineSize != 0){
                                lastLine = outputLines.get(lineSize - 1);
                            }
                            
                        if(null != promptqa){
                            for(String promptq: promptqa.keySet()){
                                if(lastLine.equals(promptq)){
                                    String promptans = promptqa.get(promptq);
                                    shellStream.println(promptans); 
                                    shellStream.flush();
                                }
                            }
                        }
                        
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } 
                }
                
                if(!isCommandExecSuccessful(shellStream, baos, prompt)){
                    CommandExecResults commandExecResults = new CommandExecResults(command, output, 1);
                    commandsOutput.add(commandExecResults);
                    return commandsOutput;
                }

                CommandExecResults commandExecResults = new CommandExecResults(command, output, 0);
                commandsOutput.add(commandExecResults);
                baos.reset();
                lastLine = "";
                output = "";
            }
            
           

        }catch (JSchException jse) {
            jse.printStackTrace();
        }catch (IOException ioe) {

          ioe.printStackTrace();

       } finally {
            if(null != channel){
                channel.disconnect();
            }
            if(null != session){
                session.disconnect();
            }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.setOut(oldOutStream);
        }

        return commandsOutput;
    }
    
    private boolean isCommandExecSuccessful(PrintStream shellStream, ByteArrayOutputStream baos, String prompt) {
        
        baos.reset();
        shellStream.println(STATUS_COMMAND); 
        shellStream.flush();
        String output = baos.toString();
        List<String> outputLines = getOutputLines(output);
        int lineSize = outputLines.size();
        String lastLine = "";
        if(lineSize != 0){
            lastLine = outputLines.get(lineSize - 2);
        }
        while(!output.contains(prompt)){
            try {
                    Thread.sleep(100);
                    output = baos.toString();
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } 
        }
        output = baos.toString();
        outputLines = getOutputLines(output);
        lineSize = outputLines.size();
        lastLine = "";
        if(lineSize != 0){
             lastLine = outputLines.get(lineSize - 2).trim();
        }
        if(lastLine.equals("0")){
            return true;
        }
        return false;
    }
    
    private List<String> getOutputLines(String output) {
        
        List<String> outputLines = new ArrayList<String>();
        output = output.trim();
        StringTokenizer st = new StringTokenizer(output, "\n");
        while(st.hasMoreTokens()){
            outputLines.add(st.nextToken());
        }
        return outputLines;
    }
    
    private String getNewprompt(String command) {
        
       String prompt = "";
       StringTokenizer commandSplitter = new StringTokenizer(command, " ");
       commandSplitter.nextToken();
       String directory = commandSplitter.nextToken();
       prompt = directory;
       if(directory.contains("/")){
           StringTokenizer directorySplitter = new StringTokenizer(directory, "/");
           while(directorySplitter.hasMoreTokens()){
               prompt = directorySplitter.nextToken();
           }
       }
       prompt = prompt + "-";
       return prompt;
        
    }

}

