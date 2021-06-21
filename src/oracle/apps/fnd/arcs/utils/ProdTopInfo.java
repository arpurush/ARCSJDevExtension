package oracle.apps.fnd.arcs.utils;

public class ProdTopInfo {
	
	/**
	 * This provides the prefix of the prod top in chenv command.
	 * For example: chenv fnd 12.0 fnddev, fnd is the prefix
	 */
	private String prefix;
	
	/**
	 * This provides the branch of the prod top to be used in chenv command.
	 * For example: chenv fnd 12.0 fnddev, 12.0 is the branch
	 */
	private String branch;
	
	/**
	 * This provides the suffix part of the prod top in chenv command.
	 * For example: chenv fnd 12.0 fnddev, fnddev is the suffix
	 */
	private String suffix;
	
	/**
	 * This is the entire prod top info passed for chenv command
	 * Example: fnd 12.0 fnddev
	 */
	
	private String prodTopForChenvCommand;
	
	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}
	
	public String getBranch() {
		return branch;
	}


	public ProdTopInfo(String prefix, String suffix){
		
		this.prefix = prefix;
		this.suffix = suffix;
		
	}
	
	public String getProdTopForChenvCommand (){

		prodTopForChenvCommand = prefix + " " + branch + " " + suffix;
		return prodTopForChenvCommand;
	}
	

}
