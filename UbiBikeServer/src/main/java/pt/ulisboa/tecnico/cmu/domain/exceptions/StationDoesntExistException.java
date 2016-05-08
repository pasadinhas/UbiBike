package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class StationDoesntExistException extends UbiBikeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4864840649358865028L;

	@Override
	public String getMessage() {
		return "Station doesn't exist";
	}
	
}
