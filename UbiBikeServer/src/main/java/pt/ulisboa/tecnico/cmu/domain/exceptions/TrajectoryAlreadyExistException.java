package pt.ulisboa.tecnico.cmu.domain.exceptions;

public class TrajectoryAlreadyExistException extends UbiBikeServerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5792536522041141599L;

	@Override
	public String getLocalizedMessage(){
		return "Trajectory Already Exist";
	}
	
}
