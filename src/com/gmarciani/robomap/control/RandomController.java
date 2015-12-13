package robomap.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import robomap.model.vector.Direction;

public abstract class RandomController {

	public static List<Direction> getRandomDirections() {
		Random random = new Random(System.nanoTime());
		List<Direction> directions = new ArrayList<Direction>();
		for (Direction direction : Direction.values()) {
			if (direction == Direction.NONE) continue;
			directions.add(direction);
		}
		Collections.shuffle(directions, random);
		return directions;
	}

	public static long getRandom(long lowerBound, long upperBound) {
		return (long) (lowerBound + (Math.random() * ((upperBound - lowerBound) + 1)));
	}
	
}
