package AdapterdesignPattren;

public class chargerAdapter {
	private chargerAdaptee adaptee;

	public void charge() {
		adaptee = new chargerAdaptee();
        adaptee.plug();
	}
}
