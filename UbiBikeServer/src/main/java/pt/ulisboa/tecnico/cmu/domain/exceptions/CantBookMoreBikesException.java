package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class CantBookMoreBikesException extends UbiBikeServerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1285006170670339860L;

	@Override
	public String getLocalizedMessage() {
		return "Can't book more bikes";
	}
	
}
