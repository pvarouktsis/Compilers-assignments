import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Utils {
	private String className;
	private Hashtable varTable;
	private Hashtable valTable;
	private Hashtable funcTable;
	private Hashtable retTable;
	private List<Error> errors;
	private int errorCounter;
	private static int debugCounter;

	public Utils(String className, Hashtable varTable, Hashtable valTable, Hashtable funcTable, Hashtable retTable, List<Error> errors, int errorCounter) {
		this.className = className;
		this.varTable = varTable;
		this.valTable = valTable;
		this.funcTable = funcTable;
		this.retTable = retTable;
		this.errors = errors;
		this.errorCounter = errorCounter;
		this.debugCounter = 1;
	}

	// helper
	public boolean checkParameters(AFunction first, AFunction second) {
		// TODO
		// def add(x, y, a)
		// def add(x, y, a=1, b=1)
		
		if (getSizeOfParameters(first) != getSizeOfParameters(second) &&
			getSizeOfRequiredParameters(first) != getSizeOfParameters(second))
			return true;

		return false;
	}

	// helper
	public boolean checkParametersOrder(AFunction node) {
		LinkedList<AParameter> params = node.getParameter();
		Iterator iter = params.listIterator(getSizeOfRequiredParameters(node));
		
		while (iter.hasNext()) {
			AParameter param = (AParameter) iter.next();
			if (param.getValue() == null)
				return false;
		}

		return true;
	}

	// helper
	public int getSizeOfParameters(AFunction node) {
		return node.getParameter().size();
	}

	// helper
	public int getSizeOfRequiredParameters(AFunction node) {
		LinkedList<AParameter> params = node.getParameter();
		Iterator iter = params.iterator();

		int countNonDefaultParameters = 0;
		while (iter.hasNext()) {
			if ( ((AParameter) iter.next()).getValue() != null )
				break;
			
			countNonDefaultParameters++;
		}

		return countNonDefaultParameters;
	}

	// helper
	public Type getType(Object node) {

		// if expression is id
		if (node instanceof AIdExpression) {
			String id = ((AIdExpression) node).getId().toString().trim();
			if (valTable.containsKey(id)) {
				return (Type) valTable.get(id);
			}

		// if expression is value
		} else if (node instanceof AValueExpression) {
			PValue value = ((AValueExpression) node).getValue();
			if (value instanceof ANumberValue) {
				return Type.NUMBER;
			} else if (value instanceof AStringValue) {
				return Type.STRING;
			}

		// if expression is operation
		} else if ((node instanceof AAdditionExpression) ||
			(node instanceof ASubtractionExpression) ||
			(node instanceof AMultiplicationExpression) ||
			(node instanceof ADivisionExpression) ||
			(node instanceof AModuloExpression) ||
			(node instanceof APowerExpression)) {
			
			String id = node.toString().trim();
			if (valTable.containsKey(id)) {
				return (Type) valTable.get(id);
			}

		}

		// else
		return Type.NONE;
	}

	// debug
	public void printDebugInfo() {
		System.out.println("\n" + className +  ": " + debugCounter++);
		
		System.out.print("varTable: ");
		printVarTable();
		System.out.print("valTable: ");
		printValTable();
		System.out.print("funcTable: ");
		printFuncTable();		
		System.out.print("retTable: ");
		printRetTable();

	}

	public void printVarTable() {
		System.out.println(varTable);
	}

	public void printValTable() {
		System.out.println(valTable);
	}

	public void printFuncTable() {
		System.out.println(funcTable);
	}

	public void printRetTable() {
		System.out.println(retTable);
	}

}
