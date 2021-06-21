package oracle.apps.fnd.arcs.process;

import com.jcraft.jsch.SftpProgressMonitor;

public class SftpProgressMonitorImpl implements SftpProgressMonitor {
    
    public SftpProgressMonitorImpl() {
        super();
    }
    private long fileSize;
    private boolean transferSuccess = false;
    private long totalcount = 0;


    public boolean isTransferSuccess() {
        return transferSuccess;
    }
    
    
    public void init(int op, String src, String dest, long count) {
        this.fileSize = count;
    }

    public boolean count(long l) {
        totalcount += l;
        if (totalcount < fileSize) {
            return true;
        } else if (totalcount == fileSize) {
            transferSuccess = true;
            return false;
        }
        return false;
    }

    public void end() {
    }
}
