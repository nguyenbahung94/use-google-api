package abstractFactory;

public class MainMethodTest {
	public static void main(String args[]) {
		AbstractFractoryMethod addshape = FractoryProducer.getFactory();
		MainMethod shape1 = addshape.getShape("CIRCLE");
		shape1.add(new Circle(129, 3));

		MainMethod shape2 = addshape.getShape("RECTANGLE");
		shape2.add(new Rectangle(1, 23, 34));
		shape2.remove(new Rectangle(2, 23, 34));
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().reduShape();
		StackSingleton.getAddActionInstance().reduShape();
		StackSingleton.getAddActionInstance().PrintShape();
	}
}