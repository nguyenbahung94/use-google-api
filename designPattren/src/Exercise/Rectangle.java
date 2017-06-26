package Exercise;

public class Rectangle implements MainMethod {
	private int position;
	private int width;
	private int height;

	public Rectangle(int position, int width, int height) {
		super();
		this.position = position;
		this.width = width;
		this.height = height;
	}

	public Rectangle() {
		super();
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "Rectangle [position=" + position + ", width=" + width + ", height=" + height + "]";
	}

	@Override
	public void add(MainMethod mainMethod) {
		// TODO Auto-generated method stub
		StackSingleton.getAddActionInstance().addShape(mainMethod);
	}

	@Override
	public void remove(MainMethod mainMethod) {
		// TODO Auto-generated method stub
		StackSingleton.getAddActionInstance().removeShapeRectangle(mainMethod);
	}

	@Override
	public void undo() {
		// TODO Auto-generated method stub
		StackSingleton.getAddActionInstance().undoShape();
	}

}
