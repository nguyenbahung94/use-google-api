package abstractFactory;

public interface MainMethod {
	public void add(MainMethod mainMethod);

	public void remove(MainMethod mainMethod);
	
	public void undo();
}
