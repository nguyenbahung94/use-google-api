package abstractFactory;

public class ShapeFactory extends AbstractFractoryMethod {

	@Override
	MainMethod getShape(String shapeType) {
		// TODO Auto-generated method stub
		if (shapeType == null) {
			return null;
		}
		if (shapeType.equalsIgnoreCase("CIRCLE")) {
			return new Circle();
		} else {
			if (shapeType.equalsIgnoreCase("RECTANGLE")) {
				return new Rectangle();
			}
		}
		return null;
	}

}
