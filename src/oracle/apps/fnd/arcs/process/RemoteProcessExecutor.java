package oracle.apps.fnd.arcs.process;



public class RemoteProcessExecutor {
    
    
    private RemoteProcessExecutor() {
        super();
    }
    
    private RemoteProcessExecutor remoteProcessExecutor = null;
    
    public RemoteProcessExecutor getRemoteProcessExecutor(){
        
       remoteProcessExecutor = null == remoteProcessExecutor ? new RemoteProcessExecutor() : remoteProcessExecutor; 
       return remoteProcessExecutor;
    
    }
    
    public boolean execCommand (String command, String [] args){
        
       
        
        return false;
    }
    
}
