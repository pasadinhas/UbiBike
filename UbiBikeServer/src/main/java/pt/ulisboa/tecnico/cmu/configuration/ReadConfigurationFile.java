package pt.ulisboa.tecnico.cmu.configuration;

import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.persistence.StationRepository;
import pt.ulisboa.tecnico.cmu.persistence.UserRepository;

@Component
public class ReadConfigurationFile {

	private static final String CONFIG_FILE = "/Config.xml";
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StationRepository stationRepo;
	
	
	public void startConfigurationFile(){
		try{
			System.out.println("###### Start reading config file #######");
			Resource resource = new ClassPathResource(CONFIG_FILE);
			InputStream resourceInputStream = resource.getInputStream();
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(resourceInputStream);
			
			Element root = doc.getRootElement();
			Element usersElem = root.getChildren("users").get(0);
			for(Element userElem : usersElem.getChildren()){
				String username = userElem.getAttributeValue("username");
				String password = userElem.getAttributeValue("password");
				long points = Long.parseLong(userElem.getAttributeValue("points"));
				User user = new User(username,password);
				user.setPoints(points);
				
				for(Element trajElem : userElem.getChildren()){
					long timestamp = Long.parseLong(trajElem.getAttributeValue("date"));
					Trajectory traj = new Trajectory(timestamp);
					for(Element coordElem: trajElem.getChildren()){
						double latitude = Double.parseDouble(coordElem.getAttributeValue("latitude"));
						double longitude = Double.parseDouble(coordElem.getAttributeValue("longitude"));
						Coordinates coord = new Coordinates(latitude,longitude);
						traj.addCoordinates(coord);
					}
					user.addTrajectory(traj);
				}
				userRepo.save(user);
			}
			
			Element stationsElem = root.getChildren("stations").get(0);
			for(Element stationElem : stationsElem.getChildren()){
				String stationName = stationElem.getAttributeValue("name");
				double latitude = Double.parseDouble(stationElem.getAttributeValue("latitude"));
				double longitude = Double.parseDouble(stationElem.getAttributeValue("longitude"));
				Station station = new Station(stationName, new Coordinates(latitude,longitude));
				
				for(Element bikeElem : stationElem.getChildren()){
					String bikeId = bikeElem.getAttributeValue("id");
					double la = Double.parseDouble(bikeElem.getAttributeValue("latitude"));
					double lo = Double.parseDouble(bikeElem.getAttributeValue("longitude"));
					Bike bike = new Bike(new Coordinates(la, lo), bikeId);
					station.addBike(bike);
					bike.setStation(station);
				}
				stationRepo.save(station);
			}
			System.out.println("##### Config file read with success ######");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}