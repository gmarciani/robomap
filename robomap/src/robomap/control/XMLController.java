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
import robomap.model.object.Interaction;
import robomap.model.object.Object;
import robomap.model.vector.Dimension;
import robomap.model.vector.Direction;
import robomap.model.vector.Location;

/**
 * @project robomap
 *
 * @package robomap.control
 *
 * @class XMLController
 *
 * @author Giacomo Marciani
 *
 * @description
 *
 */
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
			int homeStartX = Integer.valueOf(elementHome.getAttribute("startx"));
			int homeStartY = Integer.valueOf(elementHome.getAttribute("starty"));
			home = new Home(homeName, new Dimension(homeWidth, homeHeight), new Location(homeStartX, homeStartY));
			
			NodeList nodesRoom = elementHome.getElementsByTagName("ROOM");
			NodeList nodesWall = elementHome.getElementsByTagName("WALL");
			NodeList nodesDoor = elementHome.getElementsByTagName("DOOR");
			
			for (int r = 0; r < nodesRoom.getLength(); r ++) {
				Element elementRoom = (Element) nodesRoom.item(r);
				String roomName = elementRoom.getAttribute("name");				
				int roomLocationX = Integer.valueOf(elementRoom.getAttribute("x"));
				int roomLocationY = Integer.valueOf(elementRoom.getAttribute("y"));
				int roomWidth = Integer.valueOf(elementRoom.getAttribute("width"));
				int roomHeight = Integer.valueOf(elementRoom.getAttribute("height"));
				Room room = new Room(roomName, new Location(roomLocationX, roomLocationY), new Dimension(roomWidth, roomHeight));
				
				NodeList nodesObject = elementRoom.getElementsByTagName("OBJECT");
				
				for (int o = 0; o < nodesObject.getLength(); o ++) {
					Element elementObject = (Element) nodesObject.item(o);
					String objectName = elementObject.getAttribute("name");	
					int objectLocationX = Integer.valueOf(elementObject.getAttribute("x"));
					int objectLocationY = Integer.valueOf(elementObject.getAttribute("y"));
					int objectWidth = Integer.valueOf(elementObject.getAttribute("width"));
					int objectHeight = Integer.valueOf(elementObject.getAttribute("height"));
					Direction objectOrientation = Direction.valueOf(elementObject.getAttribute("orientation"));
					String objectStatus = (elementObject.getAttribute("status"));
					Object object = new Object(objectName, new Dimension(objectWidth, objectHeight), new Location(objectLocationX, objectLocationY), objectOrientation, objectStatus);
					
					NodeList nodesInteraction= elementObject.getElementsByTagName("INTERACTION");
					
					for (int i = 0; i < nodesInteraction.getLength(); i ++) {
						Element elementInteraction = (Element) nodesInteraction.item(i);
						Interaction interaction = Interaction.valueOf(elementInteraction.getAttribute("name"));						
						object.addInteraction(interaction);
					}
					home.addObject(object);
					
				}				
				home.addRoom(room);
			}
			
			for (int w = 0; w < nodesWall.getLength(); w ++) {
				Element elementWall = (Element) nodesWall.item(w);
				int wallLenght = Integer.valueOf(elementWall.getAttribute("lenght"));
				Direction wallOrientation = Direction.valueOf(elementWall.getAttribute("orientation"));
				int wallLocationX = Integer.valueOf(elementWall.getAttribute("x"));
				int wallLocationY = Integer.valueOf(elementWall.getAttribute("y"));
				Wall wall = new Wall(new Location(wallLocationX, wallLocationY), wallOrientation, wallLenght);
				home.addWall(wall);				
			}
			
			for (int d = 0; d < nodesDoor.getLength(); d ++) {
				Element elementDoor = (Element) nodesDoor.item(d);
				int doorLenght = Integer.valueOf(elementDoor.getAttribute("lenght"));
				Direction doorOrientation = Direction.valueOf(elementDoor.getAttribute("orientation"));
				int doorLocationX = Integer.valueOf(elementDoor.getAttribute("x"));
				int doorLocationY = Integer.valueOf(elementDoor.getAttribute("y"));
				Door door = new Door(new Location(doorLocationX, doorLocationY), doorOrientation, doorLenght);
				home.addDoor(door);				
			}
		}
		
		return home;
		
	}

}
