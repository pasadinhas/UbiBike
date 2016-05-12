package pt.ulisboa.tecnico.cmu.configuration;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.qos.logback.core.net.SyslogOutputStream;
import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.persistence.BikeRepository;
import pt.ulisboa.tecnico.cmu.persistence.StationRepository;
import pt.ulisboa.tecnico.cmu.persistence.UserRepository;

@Component
public class InitializeDatabase {

	private static final String CONFIG_FILE = "/Config.xml";
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StationRepository stationRepo;
	
	@Autowired
	private BikeRepository bikeRepo;
	
	public void populateDatabaseFromConfigFile() {
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Resource resource = new ClassPathResource(CONFIG_FILE);
			InputStream resourceInputStream = resource.getInputStream();
			Document doc = builder.parse(resourceInputStream);
			
			Element root = doc.getDocumentElement();
			NodeList rootChilds = root.getChildNodes();
			for(int i = 0; i < rootChilds.getLength(); i++){
				Node n = rootChilds.item(i);
				if(n.getNodeName().equals("users")){
					System.out.println("Users");
					NodeList users = n.getChildNodes();
					//Add all users
					System.out.println(users.getLength());
					for(int k = 0; k < users.getLength(); k++){
						System.out.println(users.item(k).getTextContent());
//						Element userElem = (Element) users.item(k);
//						String username = userElem.getAttribute("username");
//						String password = userElem.getAttribute("password");
//						long points = Long.parseLong(userElem.getAttribute("points"));
//						User u = new User(username,password);
//						u.setPoints(points);
//						userRepo.save(u);
					}
				}
				else if(n.getNodeName().equals("statios")){
					System.out.println("Stations");
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void populateDatabase(){
		//populateDatabaseFromConfigFile();
		populateProgramatically();
	}
	
	private void populateProgramatically(){
		System.out.println("###### Initializing BD ######");
		Calendar c = Calendar.getInstance();
		c.set(2015, 11, 6, 18, 11, 32);
		Date d = c.getTime();
		Trajectory t = new Trajectory(d.getTime());
		t.addCoordinates(new Coordinates(38.769836, -9.204510));
		t.addCoordinates(new Coordinates(38.770108, -9.204942));
		t.addCoordinates(new Coordinates(38.770362, -9.205211));
		t.addCoordinates(new Coordinates(38.770119, -9.205608));
		t.addCoordinates(new Coordinates(38.769316, -9.205436));
		t.addCoordinates(new Coordinates(38.769425, -9.205093));
		t.addCoordinates(new Coordinates(38.769642, -9.204707));
		c.set(2015,12, 2, 14, 50);
		Date d1 = c.getTime();
		Trajectory t1 = new Trajectory(d1.getTime());
		t1.addCoordinates(new Coordinates(38.754212, -9.144914));
		t1.addCoordinates(new Coordinates(38.754471, -9.144721));
		t1.addCoordinates(new Coordinates(38.755040, -9.142897));
		t1.addCoordinates(new Coordinates(38.753701, -9.142203));
		t1.addCoordinates(new Coordinates(38.753701, -9.142203));
		t1.addCoordinates(new Coordinates(38.753450, -9.142938));
		t1.addCoordinates(new Coordinates(38.754212, -9.144914));
      
        User u = new User("Admin","root");
        u.setPoints(1401400);
        User u1 = new User("Andre","123");
        User u2 = new User("António","123");
        User u3 = new User("Alexandre","123");
        User u4 = new User("Álvaro","123");
        User u5 = new User("Bruno","123");
        try{
        	u.addTrajectory(t);
            u.addTrajectory(t1);
        }catch(Exception e){
        	//DO Nothing
        }
        
        Bike b1 = new Bike(new Coordinates(38.769836, -9.204510),"1");
        Bike b2 = new Bike(new Coordinates(38.769832, -9.204973),"2");
        Bike b3 = new Bike(new Coordinates(38.754212, -9.144914),"3");
        Bike b4 = new Bike(new Coordinates(38.736976, -9.139048),"4");
        Bike b5 = new Bike(new Coordinates(38.754206, -9.145207),"5");
        Bike b6 = new Bike(new Coordinates(38.707225, -9.135622),"6");
        Bike b7 = new Bike(new Coordinates(38.707376, -9.135735),"7");
        Bike b8 = new Bike(new Coordinates(38.707179, -9.135665),"8");
        Bike b9 = new Bike(new Coordinates(38.692097, -9.214703),"9");
        Bike b10 = new Bike(new Coordinates(38.692068, -9.214896),"10");
        Bike b11 = new Bike(new Coordinates(38.767751, -9.127676),"11");
        Bike b12 = new Bike(new Coordinates(38.744396, -9.167933),"12");
        Bike b13 = new Bike(new Coordinates(38.744448, -9.168054),"13");
        
        Station s1 = new Station("Carnide",new Coordinates(38.770018, -9.204704));
        s1.addBike(b1);
        s1.addBike(b2);
        b1.setStation(s1);
        b2.setStation(s1);
        Station s2 = new Station("Alvalade",new Coordinates(38.754120, -9.144843));
        s2.addBike(b3);
        s2.addBike(b5);
        b3.setStation(s2);
        b5.setStation(s2);
        Station s3 = new Station("Instituto Superior Técnico",new Coordinates(38.737045, -9.139072));
        s3.addBike(b4);
        b4.setStation(s3);
        Station s4 = new Station("Terreiro do Paço",new Coordinates(38.707242, -9.135710));
        s4.addBike(b6);
        b6.setStation(s4);
        s4.addBike(b7);
        b7.setStation(s4);
        s4.addBike(b8);
        b8.setStation(s4);
        Station s5 = new Station("Belém",new Coordinates(38.692122, -9.214856));
        s5.addBike(b9);
        b9.setStation(s5);
        s5.addBike(b10);
        b10.setStation(s5);
        Station s6 = new Station("Aeroporto",new Coordinates(38.767843, -9.127762));
        s6.addBike(b11);
        b11.setStation(s6);
        Station s7 = new Station("Jardim Zoológico",new Coordinates (38.744466, -9.167980));
        s7.addBike(b12);
        b12.setStation(s7);
        s7.addBike(b13);
        b13.setStation(s7);
        
        stationRepo.save(s1);
        stationRepo.save(s2);
        stationRepo.save(s3);
        userRepo.save(u);
        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(u4);
        userRepo.save(u5);
        System.out.println("###### BD Initialized ######");
	}
	
	public void wipeoutDatabase(){
		stationRepo.deleteAll();
		userRepo.deleteAll();
		bikeRepo.deleteAll();
	}
	
	public void resetDatabase(){
		wipeoutDatabase();
		populateDatabase();
	}
	
}
