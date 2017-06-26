package abstractFactory;

public class Circle implements MainMethod {
	private int position;
	private int radius;

	public Circle(int position, int radius) {
		super();
		this.position = position;
		this.radius = radius;
	}

	public Circle() {
		super();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Circle [position=" + position + ", radius=" + radius;
	}

	@Override
	public void add(MainMethod mainMethod) {
		// TODO Auto-generated method stub
		StackSingleton.getAddActionInstance().addShape(mainMethod);
	}

	@Override
	public void remove(MainMethod mainMethod) {
		// TODO Auto-generated method stub
		StackSingleton.getAddActionInstance().removeShape(mainMethod);

	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		// StackSingleton.getAddActionInstance().undoShape();
	}

}
