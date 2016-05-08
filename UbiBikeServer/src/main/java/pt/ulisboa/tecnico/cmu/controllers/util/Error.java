package pt.ulisboa.tecnico.cmu.controllers.util;

public class Error {

	private int errorCode;
	
	private String errorMessage;
	
	public Error(int errorCode,String errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public int getErrorCode(){
		return errorCode;
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
	
}
