import minipython.analysis.AnalysisAdapter;
import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Visitor1 extends DepthFirstAdapter {
	private Hashtable symtable;
	private Hashtable functable;
	private List<Error> errors;
	private int errorCounter;
	private Utils utils;

	public Visitor1() {
		this.symtable = new Hashtable();
		this.functable = new Hashtable();
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor1", symtable, functable, errors, errorCounter);
	}

	public Visitor1(Hashtable symtable, Hashtable functable) {
		this.symtable = symtable;
		this.functable = new Hashtable();
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor1", symtable, functable, errors, errorCounter);
	}

	// in function
	// parse ONLY parameters,
	// not statement
    public void caseAFunction(AFunction node) {
        inAFunction(node);
        
        if(node.getId() != null) {
            node.getId().apply(this);
        }

        Object temp[] = node.getParameter().toArray();
        for(int i = 0; i < temp.length; i++) {
            ((PParameter) temp[i]).apply(this);
        }
        
        outAFunction(node);
    }

	// define functions
	public void inAFunction(AFunction node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (!symtable.containsKey(id)) {
			functable.put(id, node);
			utils.printDebugInfo();
		}

		// if (!utils.checkParametersOrder(node)) {
		// 	addError(id, line,
		// 		"Default parameters of function \"" + id + "\" must be declared last");

		// 	return;
		// }

		// if (!symtable.containsKey(id)) {
		// 	symtable.put(id, new ArrayList<AFunction>(5));
		// 	((ArrayList) symtable.get(id)).add(node);
		// 	utils.printDebugInfo();

		// 	return;
		// }

		// if (!utils.functionExists(node)) {
		// 	((ArrayList) symtable.get(id)).add(node);
		// 	utils.printDebugInfo();

		// 	return;
		// }		
		
		// addError(id, line,
		// 	"Function \"" + id + "\" has already been defined");
	}

	// define parameters
	public void inAParameter(AParameter node) {
		String id = node.getId().toString().trim();

		symtable.put(id, (Type) utils.getType(node.getValue()));
		utils.printDebugInfo();
	}

	// define ids
	public void inARegularAssignmentStatement(ARegularAssignmentStatement node) {
		String id = node.getId().toString().trim();

		symtable.put(id, (Type) utils.getType(node.getExpression()));
		utils.printDebugInfo();
	}

	// check ids
	public void outAIdExpression(AIdExpression node) {
		String id = node.getId().toString().trim();

		if (!symtable.containsKey(id)) {
			int line = node.getId().getLine();
			addError(id, line,
				"Variable \"" + id + "\" has not been defined");
		}
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

