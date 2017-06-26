package Exercise;

public class MainTestFactory {
	public static void main(String args[]) {
		AbstractFactoryPrint addshape = FactoryProducer.getFactory("2D");
		MainMethod shape1 = addshape.print2d("CIRCLE");
		shape1.add(new Circle(111, 3));
		shape1.add(new Circle(222, 3));
             
		AbstractFactoryPrint addshape2 = FactoryProducer.getFactory("3D");
		MainMethod shape2 = addshape2.print3d("RECTANGLE");
		shape2.add(new Rectangle(1, 23, 34));
		shape2.remove(new Rectangle(1, 23, 34));
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().undoShape();
		StackSingleton.getAddActionInstance().reduShape();
//		StackSingleton.getAddActionInstance().reduShape();
		StackSingleton.getAddActionInstance().PrintShape();
	}
}
