import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Hashtable;

public class Visitor1 extends DepthFirstAdapter {

	private Hashtable symtable;
	//private Stack stack;
	private List<Error> errors;
	private int errorCounter;

	public Visitor1() {
		this.symtable = new Hashtable();
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	public Visitor1(Hashtable symtable) {
		this.symtable = symtable;
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	// add the defined variables in symtable
	public void inARegularAssignmentStatement(ARegularAssignmentStatement node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		// TODO scope
		symtable.put(id, node);

		printSymtable();
	}

	// check if the id is already defined
	public void inAIdExpression(AIdExpression node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (!symtable.containsKey(id)) {
			errorCounter++;
			errors.add(new Error(id, line,
				"Variable \"" + id + "\" has not been defined"));
		}

		printSymtable();
	}

	// TODO
	// with a list 
	// order can trick the compiler
	public void inAFunction(AFunction node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (symtable.containsKey(id)) {

		} else {
			symtable.put(id, node);
			insertParameters(node.getParameter());
		}


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

	// insert the args into the symtable (as global) variables
	public void insertParameters(LinkedList params) {
		Iterator<AParameter> iter = params.iterator();
		while (iter.hasNext()) {
			AParameter param = iter.next();
			String paramId = param.getId().toString().trim();
			int paramLine = param.getId().getLine();

			// insert it in the symtable
			symtable.put(paramId, param);
		}
	}

    public Hashtable getSymtable() {
    	return symtable;
    }

    public void printSymtable() {
    	System.out.println(symtable);
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

