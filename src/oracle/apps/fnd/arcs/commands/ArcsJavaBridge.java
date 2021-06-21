package oracle.apps.fnd.arcs.commands;

import oracle.apps.fnd.arcs.log.Logger;

public interface ArcsJavaBridge {

	/**
	 * Should return the hostname of the session server. Example: rws3220122.us.oracle.com
	 * @return
	 */
	public String getHostName();
	
	/**
	 * Should return the GUID of the user. Example: arpurush
	 * @return
	 */	
	public String getUserName();
	
	/**
	 * Should return the session server password of the user
	 * @return
	 */
	public String getPassword();
	
	/**
	 * Should return an instance of Logger which will be used to log information
	 * @return
	 */
	public Logger getLogger();
	
}
