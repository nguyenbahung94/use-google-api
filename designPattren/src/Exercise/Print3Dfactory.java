package Exercise;

public class Print3Dfactory extends AbstractFactoryPrint {

	@Override
	MainMethod print2d(String shapeType) {
		return null;
	}

	@Override
	MainMethod print3d(String shapeType) {
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
