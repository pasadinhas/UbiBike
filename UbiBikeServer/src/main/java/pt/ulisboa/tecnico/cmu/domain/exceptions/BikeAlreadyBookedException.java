package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class BikeAlreadyBookedException extends UbiBikeServerException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6630171047580501580L;

	@Override
	public String getMessage() {
		return "Bike already reserved by other User";
	}
	
}
