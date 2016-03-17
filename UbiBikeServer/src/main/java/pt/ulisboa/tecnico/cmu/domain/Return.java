package pt.ulisboa.tecnico.cmu.domain;

public class Return {

	private final String content;
	
	private final int id;
	
	public Return(String content,int id){
		this.content = content;
		this.id = id;
	}
	
	public String getContent(){
		return content;
	}
	
	public int getId(){
		return id;
	}
	
}
