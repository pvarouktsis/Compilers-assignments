package code;

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

	// constructor with symtable, functable
	public Visitor2(Hashtable symtable, Hashtable functable) {
		this.symtable = symtable;
		this.functable = functable;
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor2", symtable, functable);
	}

	// in a function_definition parse, do not read the statements
	public void caseAFunction(AFunction node) {
        caseAFunction(node, false);
    }

    // in a function_call parse, read the statements
    public void caseAFunction(AFunction node, boolean flag) {
        inAFunction(node);

        // we could remove it, but we will keep it
        if (node.getId() != null) {
            node.getId().apply(this);
        }

        // we could remove it, but we will keep it
        Object temp[] = node.getParameter().toArray();
        for (int i = 0; i < temp.length; i++) {
            ((PParameter) temp[i]).apply(this);
        }

        // if function_call, run statement
        if (flag) {
        	if (node.getStatement() != null) {
            	node.getStatement().apply(this);
        	}
        }

        outAFunction(node);
    }

    // define and check function_call
    public void caseAFunctionCall(AFunctionCall node) {
        inAFunctionCall(node);

        // we could remove it, but we will keep it
        if(node.getId() != null) {
            node.getId().apply(this);
        }
        
        // we could remove it, but we will keep it
        Object temp[] = node.getExpression().toArray();
        for(int i = 0; i < temp.length; i++) {
            ((PExpression) temp[i]).apply(this);
        }

        // call the function to find the return type (if there is return)
        String id = node.getId().toString().trim();
        if (functable.containsKey(id)) {
            AFunction function = (AFunction) functable.get(id);
    		caseAFunction(function, true);
    		//utils.printDebugInfo();
        }

        outAFunctionCall(node);
    }

    // define and check function call
	public void inAFunctionCall(AFunctionCall node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		// if function not exists, raise error
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

		// if arguments < required_parameters or arguments > parameters, raise error
		if (args.size() < utils.getSizeOfRequiredParameters(function) || 
			args.size() > utils.getSizeOfParameters(function)) {
			addError(id, line,
				"Wrong call of \"" + id + "\" function");
			return;
		}

		// put the argument types of function_call to the parameters of function_definition
		while (iterArgs.hasNext()) {
			PExpression arg = (PExpression) iterArgs.next();
			AParameter param = (AParameter) iterParams.next();

			String paramId = param.getId().toString().trim();

			symtable.put(paramId, (Type) utils.getType(arg));
			//utils.printDebugInfo();
		}
	}

	// define the type of function_calls
    public void outAFunctionCall(AFunctionCall node) {
    	String id = node.getId().toString().trim();
		AFunction function = (AFunction) functable.get(id);
    	
    	symtable.put(id, (Type) utils.getType(function));
    	//utils.printDebugInfo();
    }

    // define and check
	public void outAAdditionExpression(AAdditionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();

		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation addition");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	// define and check
	public void outASubtractionExpression(ASubtractionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation subtraction");
			return;
		}

		// if string type, raise error
		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation subtraction");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	// define and check
	public void outAMultiplicationExpression(AMultiplicationExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation multiplication");
			return;
		}

		// if string type, raise error
		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation multiplication");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	// define and check
	public void outADivisionExpression(ADivisionExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	
		
		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation division");
			return;
		}

		// if string type, raise error
		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation division");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	// define and check
	public void outAModuloExpression(AModuloExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation modulo");
			return;
		}

		// if string type, raise error
		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation modulo");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	// define and check
	public void outAPowerExpression(APowerExpression node) {
		String id = node.toString().trim();
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		// if different types, raise error
		if (utils.getType(leftExp) != utils.getType(rightExp)) {
			addError(id, -1, "Unsupported operation power");
			return;
		}

		// if string type, raise error
		if (utils.getType(leftExp) == Type.STRING) {
			addError(id, -1, "Unsupported operation power");
			return;
		}

		symtable.put(id, (Type) utils.getType(leftExp)); // either left or right could work			
		//utils.printDebugInfo();
	}

	/*
	 * Getters/Setters - Helpers
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
