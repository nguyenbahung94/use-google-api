package Exercise;

public class Print2Dfactory extends AbstractFactoryPrint {

	@Override
	MainMethod print2d(String shapeType) {
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

	@Override
	MainMethod print3d(String type) {
		return null;
	}

}
