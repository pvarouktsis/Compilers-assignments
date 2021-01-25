import minipython.analysis.AnalysisAdapter;
import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class VariableAnalyzerInspector extends DepthFirstAdapter {

	private Hashtable vartable;
	private Hashtable valtable;
	private Hashtable exptable;
	private List<Error> errors;
	private int errorCounter;
	private int debugCounter;

	public VariableAnalyzerInspector() {
		this.vartable = new Hashtable();
		this.valtable = new Hashtable();
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.debugCounter = 0;
	}

	public VariableAnalyzerInspector(Hashtable vartable, Hashtable valtable) {
		this.vartable = vartable;
		this.valtable = valtable;
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.debugCounter = 0;
	}

	// define
	public void inARegularAssignmentStatement(ARegularAssignmentStatement node) {
		String id = node.getId().toString().trim();

		vartable.put(id, node);
		valtable.put( id, getType(node.getExpression()) );

		printDebugInfo();
	}

	// define
	public void inAParameter(AParameter node) {
		String id = node.getId().toString().trim();

		vartable.put(id, node);

		printDebugInfo();
	}

	// check
	public void outAIdExpression(AIdExpression node) {
		String id = node.getId().toString().trim();

		if (!vartable.containsKey(id)) {
			int line = node.getId().getLine();
			errorCounter++;
			errors.add(new Error(id, line,
				"Variable \"" + id + "\" has not been defined"));
		}
	}

	// check
	public void outAForStatement(AForStatement node) {
		String fid = node.getFirstId().toString().trim();
		String sid = node.getSecondId().toString().trim();

		// check first id
		if (!vartable.containsKey(fid)) {
			int fline = node.getFirstId().getLine();
			errorCounter++;
			errors.add(new Error(fid, fline,
				"Variable \"" + fid + "\" has not been defined"));
		}

		// check second id
		if (!vartable.containsKey(sid)) {
			int sline = node.getSecondId().getLine();
			errorCounter++;
			errors.add(new Error(sid, sline,
				"Variable \"" + sid + "\" has not been defined"));
		}
	}

	// define & check
	public void outAAdditionExpression(AAdditionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check
	public void outASubtractionExpression(ASubtractionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check
	public void outAMultiplicationExpression(AMultiplicationExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check
	public void outADivisionExpression(ADivisionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check
	public void outAModuloExpression(AModuloExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check
	public void outAPowerExpression(APowerExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		printDebugInfo();
	}

	// define & check (helper)
	public void checkType(PExpression node, PExpression leftExp, PExpression rightExp) {
		String id = node.toString();

		if (getType(leftExp) == getType(rightExp)) {
			valtable.put(id, (Type) getType(leftExp)); // either left or right could work
		} else {
			int line = -1;
			errorCounter++;
			errors.add(new Error(id, line,
				"Unsupported operation"));
		}

	}

	// define & check (helper)
	public Type getType(PExpression node) {

		// if expression is id
		if (node instanceof AIdExpression) {
			String id = ((AIdExpression) node).getId().toString().trim();
			if (valtable.containsKey(id)) {
				return (Type) valtable.get(id);
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
			String id = node.toString();
			if (valtable.containsKey(id)) {
				return (Type) valtable.get(id);
			}

		}

		// else
		return Type.NONE;
	}

	
	public void printDebugInfo() {
		System.out.println("\nCounter:" + ++debugCounter);
		System.out.print("vartable: ");
		printVartable();
		System.out.print("valtable: ");
		printValtable();

	}
	
	/*

		// // TODO
		// // with list because order
		// // can trick the compiler

		//showSymtable();

		// LinkedList args = node.getArgument();
		// if (symtable.containsKey(id)) {
		// 	AFunction otherNode = (AFunction) symtable.get(id);
		// 	LinkedList otherArgs = otherNode.getArgument();
			
		// 	// if the size of total parameters is the same
		// 	// raise error
		// 	if (args.size() == otherArgs.size()) {				
		// 		errorCounter++;
		// 		errors.add(new Error(id, line,
		// 			"Function " + id + " has already been defined"));

		// 	} else {
		// 		if (args.size() > otherArgs.size()) {
		// 			// count default parameters of current function
		// 			Iterator<AArgument> iter = args.iterator();
		// 			int defaultCounter = 0;
		// 			while (iter.hasNext())
		// 				if (iter.next().getValue() != null)
		// 					defaultCounter++;

		// 			// check for error
		// 			if (otherArgs.size() == (args.size() - defaultCounter)) {
		// 				errorCounter++;
		// 				errors.add(new Error(id, line,
		// 					"Function " + id + " has already been defined"));
		// 			} else {
		// 				symtable.put(id, node);
		// 			}

		// 		} else if (args.size() < otherArgs.size()) {
		// 			// count default parameters of other function
		// 			Iterator<AArgument> iter = otherArgs.iterator();
		// 			int defaultCounter = 0;
		// 			while (iter.hasNext())
		// 				if (iter.next().getValue() != null)
		// 					defaultCounter++;

		// 			// check for error
		// 			if ((otherArgs.size() - defaultCounter) == args.size()) {
		// 				errorCounter++;
		// 				errors.add(new Error(id, line,
		// 					"Function " + id + " has already been defined"));
		// 			} else {
		// 				symtable.put(id, node);
		// 			}

		// 		}

		// 	}

		// } else {
		// 	symtable.put(id, node);
		// 	//symtable.put(id, new ArrayList<AFunction>(5).add(node));
		// }


		// add the parameters of the function in symtable
		// LinkedList args = node.getArgument();
		// Iterator<AArgument> args_itr = args.iterator();
		// while (args_itr.hasNext()) {
		//  	AArgument arg = args_itr.next();
		// 	symtable.put(arg.getId().toString(), arg);
		// }

		// if (symtable.contains(id)) {
		// 	errors.add(new Error(id, line,
		// 		"Function " + id + " has already been defined"));
		// 	errorCounter++;
		// } else {
		// 	symtable_var.put(id, node);
		// }
	}

	public void outAAdditionExpression(AAdditionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();
		
		setOut(node, leftExp.getClass());

		System.out.println();
		//checkTypes(leftExp, rightExp);
	}

	public void outValue

	public void checkTypes(PExpression leftExp, PExpression rightExp) {
		if (leftExp.getClass() == rightExp.getClass()) {
			System.out.println("hello" + leftExp.getClass().toString() + rightExp.getClass().toString());
		}
	}

	*/

    public Hashtable getVartable() {
    	return vartable;
    }

    public void printVartable() {
    	System.out.println(vartable);
    }

	public Hashtable getValtable() {
	    return valtable;
    }

    public void printValtable() {
    	System.out.println(valtable);
    }

    public List<Error> getErrors() {
    	return errors;
    }

    public void printErrors() {
    	for (Error e : errors)
    		System.out.println(e.toString());
    }

    public int getErrorCounter() {
    	return errorCounter;
    }

}

