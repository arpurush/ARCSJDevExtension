package oracle.apps.fnd.arcs.log;

public interface Logger {

	/**
	 * Should implement the logic to log INFO level messages
	 * @param message
	 */
	public void logInfo(String message);
	
	/**
	 * Should implement the logic to log WARN level messages
	 * @param message
	 */
	public void logWarning(String message);
	
	/**
	 * Should implement the logic to log ERROR level messages
	 * @param message
	 */
	public void logErrors(String message);
}
