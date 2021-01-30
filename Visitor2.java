import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Visitor2 extends DepthFirstAdapter {
	private Hashtable symtable;
	private Hashtable functable;
	private List<Error> errors;
	private int errorCounter;
	private Utils utils;

	public Visitor2(Hashtable symtable, Hashtable functable) {
		this.symtable = symtable;
		this.functable = functable;
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor2", symtable, functable, errors, errorCounter);
	}

	// in a regular parse avoid to 
	// read the statement of function
	public void caseAFunction(AFunction node) {
        caseAFunction(node, false);
    }

    // in a function call parse
    // read the statements of function
    public void caseAFunction(AFunction node, boolean flag) {
        inAFunction(node);

        if (node.getId() != null) {
            node.getId().apply(this);
        }
        
        Object temp[] = node.getParameter().toArray();
        for (int i = 0; i < temp.length; i++) {
            ((PParameter) temp[i]).apply(this);
        }

        if (flag) {
        	if (node.getStatement() != null) {
            	node.getStatement().apply(this);
        	}
        }

        outAFunction(node);
    }

    // define and check function call
    public void caseAFunctionCall(AFunctionCall node) {
        inAFunctionCall(node);

        // call the function and run it
        // to find the return type
        String id = node.getId().toString().trim();
    	caseAFunction((AFunction) functable.get(id), true);
    	utils.printDebugInfo();
        
        outAFunctionCall(node);
    }

    // define and check function call
	public void inAFunctionCall(AFunctionCall node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		// if function not exists
		if (!functable.containsKey(id)) {
			addError(id, line,
				"Function \"" + id + "\" has not been defined");
			return;
		}

		AFunction function = (AFunction) functable.get(id);
		
		LinkedList<PExpression> args = node.getExpression();
		Iterator iterArgs = args.iterator();

		LinkedList<AParameter> params = function.getParameter();
		Iterator iterParams = params.iterator();

		// if arguments < required parameters or arguments > parameters
		if (args.size() < utils.getSizeOfRequiredParameters(function) || 
			args.size() > utils.getSizeOfParameters(function)) {
			addError(id, line,
				"Wrong call of \"" + id + "\" function");
			return;
		}

		// add types to symtable
		while (iterArgs.hasNext()) {
			PExpression arg = (PExpression) iterArgs.next();
			AParameter param = (AParameter) iterParams.next();

			String paramId = param.getId().toString().trim();

			symtable.put(paramId, (Type) utils.getType(arg));
			utils.printDebugInfo();
		}
	}

	// define function call and return type
    public void outAFunctionCall(AFunctionCall node) {
    	String id = node.getId().toString().trim();
		AFunction function = (AFunction) functable.get(id);
    	
    	symtable.put(id, (Type) utils.getType(function));
    	utils.printDebugInfo();
    }

    // define & check
	public void outAAdditionExpression(AAdditionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();

		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation addition");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	// define & check
	public void outASubtractionExpression(ASubtractionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation subtraction");
			return;
		}

		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation subtraction");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	// define & check
	public void outAMultiplicationExpression(AMultiplicationExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation multiplication");
			return;
		}

		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation multiplication");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	// define & check
	public void outADivisionExpression(ADivisionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	
		
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation division");
			return;
		}

		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation division");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	// define & check
	public void outAModuloExpression(AModuloExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation modulo");
			return;
		}

		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation modulo");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	// define & check
	public void outAPowerExpression(APowerExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation power");
			return;
		}

		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation power");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		utils.printDebugInfo();
	}

	/*
	 * Getters - Setters - Helpers
	 */
	public Hashtable getSymtable() {
		return symtable;
	}

	public Hashtable getFunctable() {
		return functable;
	}

	public void addError(String id, int line, String msg) {
		errorCounter++;
		errors.add(new Error(id, line, msg));
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
