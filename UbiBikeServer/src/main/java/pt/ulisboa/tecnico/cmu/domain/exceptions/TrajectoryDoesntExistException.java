package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class TrajectoryDoesntExistException extends UbiBikeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8699968383607518223L;

	@Override
	public String getMessage() {
		return "Trajectory doesn't exist";
	}
	
}
