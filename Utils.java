import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Utils {
	private String className;
	private Hashtable symtable;
	private Hashtable functable;
	private List<Error> errors;
	private int errorCounter;
	private static int debugCounter;

	public Utils(String className, Hashtable symtable, Hashtable functable, List<Error> errors, int errorCounter) {
		this.className = className;
		this.symtable = symtable;
		this.functable = functable;
		this.errors = errors;
		this.errorCounter = errorCounter;
		this.debugCounter = 1;
	}

	// helper
	public boolean functionExists(AFunction node) {
		String id = node.getId().toString().trim();
		List<AFunction> functions = (ArrayList) symtable.get(id);

		for (AFunction function : functions)
			if (!checkParameters(node, function))
				return true;

			return false;
		}

	// helper
	public boolean checkParameters(AFunction first, AFunction second) {
	// TODO
	// if getSizeOfParameters == getSizeOfRequiredParameters
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

	/*********************************/

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
	public Type getType(Node node) {

		// if node is function
		if (node instanceof PFunction) {
			PStatement stm = ((AFunction) node).getStatement();
			if (stm instanceof AReturnStatement) {
				PExpression exp = ((AReturnStatement) stm).getExpression();
				return (Type) getType(exp);
			}
		}

		// if node is expression
		if (node instanceof PExpression) {	
			// if expression is id
			if (node instanceof AIdExpression) {
				String id = ((AIdExpression) node).getId().toString().trim();
				if (symtable.containsKey(id)) {
					return (Type) symtable.get(id);
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
				if (symtable.containsKey(id)) {
					return (Type) symtable.get(id);
				}
			
			// if expression is function call
			} else if (node instanceof AFunctionCallExpression) {
				PFunctionCall function = ((AFunctionCallExpression) node).getFunctionCall();
				String id = ((AFunctionCall) function).getId().toString().trim();
				if (symtable.containsKey(id)) {
					return (Type) symtable.get(id);
				}
			}
		}

		// if node is value
		if (node instanceof PValue) {
			if (node instanceof ANumberValue) {
				return Type.NUMBER;
			} else if (node instanceof AStringValue) {
				return Type.STRING;
			}				
		}

		return Type.NONE;
	}

	// debug
		public void printDebugInfo() {
			System.out.println("\n" + className +  ": " + debugCounter++);

			System.out.print("symtable: ");
			printSymtable();
			System.out.print("functable: ");
			printFunctable();

		}

		public void printSymtable() {
			System.out.println(symtable);
		}

		public void printFunctable() {
			System.out.println(functable);
		}
	}
