package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class InvalidLoginException extends UbiBikeServerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -332950870228404946L;

	@Override
	public String getLocalizedMessage() {
		return "Invalid login";
	}

	
}
