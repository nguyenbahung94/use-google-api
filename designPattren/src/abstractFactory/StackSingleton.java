package ExcercisePattern;

import java.util.ArrayList;
import java.util.Stack;

public class StackSingleton {
	private static Stack<String> addActionInstance;
	private static Stack<String> stackActionUndo;
	private static Stack<Object> stackShapeUndo, stackRemoveShape;
	private static ArrayList<Object> listShape;
	private static StackSingleton stackSingleton;

	private StackSingleton() {
		addActionInstance = new Stack<>();
		listShape = new ArrayList<Object>();
		stackActionUndo = new Stack<>();
		stackShapeUndo = new Stack<>();
		stackRemoveShape = new Stack<>();
	}

	public static StackSingleton getAddActionInstance() {
		if (stackSingleton == null) {
			stackSingleton = new StackSingleton();
		}
		return stackSingleton;
	}

	public void addActionstance() {
		addActionInstance.push("ADD");
	}

	public void addShape(MainMethod mainMethod) {
		System.out.println("add " + mainMethod);
		listShape.add(mainMethod);
		stackSingleton.addActionstance();
	}

	public void removeShapeCircle(MainMethod mainMethod) {
		System.out.println("remove " + mainMethod);
		for (int i = 0; i < listShape.size(); i++) {
			Circle tam = (Circle) listShape.get(i);
			Circle tam2 = (Circle) mainMethod;
			if (tam.getPosition() == tam2.getPosition()) {
				listShape.remove(i);
			}
		}
		stackRemoveShape.push(mainMethod);
		stackSingleton.removeActionstance();
	}

	public void removeShapeRectangle(MainMethod mainMethod) {
		System.out.println("remove " + mainMethod);
		for (int i = 0; i < listShape.size(); i++) {
			Rectangle tam = (Rectangle) listShape.get(i);
			Rectangle tam2 = (Rectangle) mainMethod;
			if (tam.getPosition() == tam2.getPosition()) {
				listShape.remove(i);
			}
		}
		stackRemoveShape.push(mainMethod);
		stackSingleton.removeActionstance();
	}

	public void removeActionstance() {
		addActionInstance.push("REMOVE");
	}

	public void undoShape() {
		String result = addActionInstance.pop();
		int listshapeSize = listShape.size();
		if (result.equalsIgnoreCase("ADD")) {
			if (listshapeSize != 0) {
				stackActionUndo.push("REMOVE");
				MainMethod mainTam = (MainMethod) listShape.get(listshapeSize - 1);
				stackShapeUndo.push(mainTam);
				listShape.remove((listshapeSize - 1));
			}

		} else {
			if (result.equalsIgnoreCase("REMOVE")) {
				MainMethod maintam = (MainMethod) stackRemoveShape.pop();
				listShape.add(maintam);
				stackShapeUndo.push(maintam);
				stackActionUndo.push("ADD");

			}
		}
	}

	public void reduShape() {

		if (stackActionUndo.isEmpty()) {
			System.out.println("stackActionUndo is empty");
		} else {
			String result = stackActionUndo.pop();
			if (result.equalsIgnoreCase("ADD")) {
				MainMethod maintam = (MainMethod) stackShapeUndo.pop();
				listShape.remove(maintam);
				addActionInstance.push("REMOVE");
			} else {
				if (result.equalsIgnoreCase("REMOVE")) {
					MainMethod mainTam = (MainMethod) stackShapeUndo.pop();
					listShape.add(mainTam);
					addActionInstance.push("ADD");
				}
			}
		}

	}

	public void PrintShape() {
		System.out.println("***********printl list of Shape 2D and 3D******************");
		for (Object maint : listShape) {
			System.out.println(maint.toString());
		}
	}
}
