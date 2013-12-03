package robomap.control;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import robomap.model.home.Door;
import robomap.model.home.Home;
import robomap.model.home.Room;
import robomap.model.home.Wall;
import robomap.model.object.Object;
import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

public class XMLController {
	
	private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		
	public static Home parsePlanimetry(String path) throws ParserConfigurationException, SAXException, IOException {
		File file = new File(path);
		Home home = null;
		
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();		
		Document doc = documentBuilder.parse(file);
		
		Element planimetry = doc.getDocumentElement();
		NodeList nodesHome = planimetry.getElementsByTagName("HOME");
		for (int h = 0; h < nodesHome.getLength(); h ++) {
			Element elementHome = (Element) nodesHome.item(h);
			String homeName = elementHome.getAttribute("name");
			int homeWidth = Integer.valueOf(elementHome.getAttribute("width"));
			int homeHeight = Integer.valueOf(elementHome.getAttribute("height"));
			home = new Home(homeName, new Dimension(homeWidth, homeHeight));
			
			NodeList nodeStartLocation = elementHome.getElementsByTagName("START");
			NodeList nodesRoom = elementHome.getElementsByTagName("ROOM");
			NodeList nodesWall = elementHome.getElementsByTagName("WALL");
			NodeList nodesDoor = elementHome.getElementsByTagName("DOOR");
			
			Element elementStartLocation = (Element) nodeStartLocation.item(0);
			int startLocationX = Integer.valueOf(elementStartLocation.getAttribute("x"));
			int startLocationY = Integer.valueOf(elementStartLocation.getAttribute("y"));
			Location startLocation = new Location(startLocationX, startLocationY);
			home.setStart(startLocation);
			
			for (int r = 0; r < nodesRoom.getLength(); r ++) {
				Element elementRoom = (Element) nodesRoom.item(r);
				String roomName = elementRoom.getAttribute("name");
				int roomWidth = Integer.valueOf(elementRoom.getAttribute("width"));
				int roomHeight = Integer.valueOf(elementRoom.getAttribute("height"));
				int roomLocationX = Integer.valueOf(elementRoom.getAttribute("x"));
				int roomLocationY = Integer.valueOf(elementRoom.getAttribute("y"));
				Room room = new Room(roomName, new Dimension(roomWidth, roomHeight), new Location(roomLocationX, roomLocationY));
				
				NodeList nodesObject = elementRoom.getElementsByTagName("OBJECT");
				
				for (int o = 0; o < nodesObject.getLength(); o ++) {
					Element elementObject = (Element) nodesObject.item(o);
					String objectName = elementObject.getAttribute("name");
					int objectWidth = Integer.valueOf(elementObject.getAttribute("width"));
					int objectHeight = Integer.valueOf(elementObject.getAttribute("height"));
					int objectLocationX = Integer.valueOf(elementObject.getAttribute("x"));
					int objectLocationY = Integer.valueOf(elementObject.getAttribute("y"));
					Direction orientation = Direction.valueOf(elementObject.getAttribute("orientation"));
					Object object = new Object(objectName, new Dimension(objectWidth, objectHeight), new Location(objectLocationX, objectLocationY), orientation);
					room.addObject(object);					
				}
				
				home.addRoom(room);
			}
			
			for (int w = 0; w < nodesWall.getLength(); w ++) {
				Element elementWall = (Element) nodesWall.item(w);
				int wallWidth = Integer.valueOf(elementWall.getAttribute("width"));
				int wallHeight = Integer.valueOf(elementWall.getAttribute("height"));
				int wallLocationX = Integer.valueOf(elementWall.getAttribute("x"));
				int wallLocationY = Integer.valueOf(elementWall.getAttribute("y"));
				Wall wall = new Wall(new Dimension(wallWidth, wallHeight), new Location(wallLocationX, wallLocationY));
				home.addWall(wall);				
			}
			
			for (int d = 0; d < nodesDoor.getLength(); d ++) {
				Element elementDoor = (Element) nodesDoor.item(d);
				int doorWidth = Integer.valueOf(elementDoor.getAttribute("width"));
				int doorHeight = Integer.valueOf(elementDoor.getAttribute("height"));
				int doorLocationX = Integer.valueOf(elementDoor.getAttribute("x"));
				int doorLocationY = Integer.valueOf(elementDoor.getAttribute("y"));
				Door door = new Door(new Dimension(doorWidth, doorHeight), new Location(doorLocationX, doorLocationY));
				home.addDoor(door);				
			}
		}
		
		return home;
		
	}

}
