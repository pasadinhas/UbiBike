package pt.ulisboa.tecnico.cmu.controllers.util;

public class JsonViews {

	public interface LowDetailed { }
	
	public interface MediumDetailed extends LowDetailed { }
	
	public interface HighlyDetailed extends MediumDetailed{ }
	
}
