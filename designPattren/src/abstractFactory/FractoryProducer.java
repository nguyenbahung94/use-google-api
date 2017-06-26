package abstractFactory;

public class FractoryProducer {
	public static AbstractFractoryMethod getFactory() {
		return new ShapeFactory();
	}
}
