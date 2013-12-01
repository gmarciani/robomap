package robomap.model.vector;

import java.io.Serializable;

public class Vector implements Serializable {

	private static final long serialVersionUID = 2467159919118073955L;
	
	private int x;
	private int y;
	
	public Vector(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Vector(" + this.getX() + "," + this.getY() + ")";
	}
	
	public double getModule() {
		return Math.sqrt((Math.pow(this.getX(), 2)) + (Math.pow(this.getY(), 2)));
	}
	
	public static Vector sum(Vector vectorA, Vector vectorB) {
		int x = vectorA.getX() + vectorB.getX();
		int y = vectorA.getY() + vectorB.getY();
		return new Vector(x, y);
	}
	
	public static Vector sub(Vector vectorA, Vector vectorB) {
		int x = vectorA.getX() - vectorB.getX();
		int y = vectorA.getY() - vectorB.getY();
		return new Vector(x, y);
	}
	
	public static Vector mul(Vector vector, int scalar) {
		int x = scalar * vector.getX();
		int y = scalar * vector.getY();
		return new Vector(x, y);
	}
	
	public static int smul(Vector vectorA, Vector vectorB) {
		int xA = vectorA.getX();
		int yA = vectorA.getY();
		int xB = vectorB.getX();
		int yB = vectorB.getY();
		return (xA * xB) + (yA * yB);
	}
	
	public static int theta(Vector vectorA, Vector vectorB) {
		double moduleA = vectorA.getModule();
		double moduleB = vectorB.getModule();
		double scalar = Vector.smul(vectorA, vectorB);
		return (int) Math.round(Math.toDegrees((Math.acos(scalar / (moduleA * moduleB)))));
	}	
	
	public static Vector rotate(Vector vector, int rotation) {
		System.out.println(vector + " " + rotation);
		Vector versorX = new Vector(1, 0);
		int theta = theta(vector, versorX);
		System.out.println("theta: " + theta + ", " + (theta + rotation));
		int x = (int) Math.round(Math.cos(Math.toRadians(theta + rotation)));
		int y = (int) Math.round(Math.sin(Math.toRadians(theta + rotation)));
		System.out.println(x + " " + y);
		return new Vector(x, y);
	}

}
