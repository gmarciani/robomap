package robomap.control;

import java.util.List;

import robomap.model.home.Home;

public class DisplayController {
	
	private static DisplayController displayController;
		
	private DisplayController() {
		
	}
	
	public static DisplayController getInstance() {
		if(displayController == null) {
			displayController = new DisplayController();
		}
		return displayController;
	}

	public void showImportedHomes(List<Home> listHomes) {
		System.out.println("I've just imported the following homes:\n");
		for (Home home : listHomes) {
			System.out.println("#" + home.toString() + "\n");
		}		
	}	
	
	public int choose(List<String> choices) {		
		int choice = -1;
		
		while(choice < choices.size() || choice >= choices.size()) {
			System.out.println("Choose an option:\n");
			for (String option : choices) {
				System.out.println(choices.indexOf(option) + ") " + option);
			}
			choice = Integer.valueOf(System.console().readLine("Enter choice: "));
		}
		return choice;
	}

	public int selectHome(List<Home> allHomes) {
		int choice = -1;
		
		while(choice < allHomes.size() || choice >= allHomes.size()) {
			System.out.println("Choose a home:\n");
			for (Home home : allHomes) {
				System.out.println(allHomes.indexOf(home) + ") " + home.getName());
			}
			choice = Integer.valueOf(System.console().readLine("Enter choice: "));
		}
		return choice;
	}

}
