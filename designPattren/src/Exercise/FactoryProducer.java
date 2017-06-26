package Exercise;

public class FactoryProducer {
	public static AbstractFactoryPrint getFactory(String choice) {
		if (choice.equalsIgnoreCase("2D")) {
			return new Print2Dfactory();
		} else {
			if (choice.equalsIgnoreCase("3D")) {
				return new Print3Dfactory();
			}
		}
		return null;
	}
}
