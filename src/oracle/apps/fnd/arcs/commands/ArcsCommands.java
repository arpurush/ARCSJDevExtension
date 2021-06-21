package oracle.apps.fnd.arcs.commands;

public class ArcsCommands {
    
	public static final String CHENV_EXECUTABLE = "/appldev/bin/chenv";
	
    public static final String ARCS_EXECUTABLE = "arcs ";
    
    public static final String ARCS_IN = " in ";
    
    public static final String ARCS_OUT = " out ";
    
    public static final String ARCS_LABEL = " label= ";
    
    public static final String ARCS_RO = " ro ";
    
    public static String generateARCSInCommand(String fileName, String comments){
        
        StringBuffer arcsInCommand = new StringBuffer();
        arcsInCommand.append(ARCS_EXECUTABLE);
        arcsInCommand.append(ARCS_IN);
        arcsInCommand.append(fileName);
        arcsInCommand.append("\" ");
        arcsInCommand.append(comments);
        arcsInCommand.append("\" ");
        
        return arcsInCommand.toString();
    }
    
    public static String generateARCSOutROCommand(String fileName, String label){
        
        StringBuffer arcsOutROCommand = new StringBuffer();
        arcsOutROCommand.append(ARCS_EXECUTABLE);
        arcsOutROCommand.append(ARCS_OUT);
        arcsOutROCommand.append(fileName);
        arcsOutROCommand.append(ARCS_RO);
        if(null != label && "".equals(label)){
            arcsOutROCommand.append(ARCS_LABEL);
            arcsOutROCommand.append(label);
        }
        
        return arcsOutROCommand.toString();
    }
}
