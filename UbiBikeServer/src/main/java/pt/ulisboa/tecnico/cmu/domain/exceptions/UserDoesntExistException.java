package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class UserDoesntExistException extends UbiBikeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7889262716110064760L;

	@Override
	public String getLocalizedMessage() {
		return "User doesn't exist";
	}

	
	
}
