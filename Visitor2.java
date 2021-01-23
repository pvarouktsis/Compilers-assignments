import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Visitor2 extends DepthFirstAdapter {

	private Hashtable symtable;
	//private Stack stack;
	private List<Error> errors;
	private int errorCounter;

	public Visitor2() {
		this.symtable = new Hashtable();
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	public Visitor2(Hashtable symtable) {
		this.symtable = symtable;
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	public void inAFunctionCall(AFunctionCall node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (symtable.containsKey(id)) {
			LinkedList<PExpression> args = node.getExpression();
			LinkedList<AParameter> params = ((AFunction) symtable.get(id)).getParameter();

			// check the size of parameters
			if (params.size() == args.size()) {
				// if params == args
				// args are ok
			
			} else if (params.size() > args.size()) {
				// if params > args 
				// count the default parameters
				Iterator<AParameter> iter = params.iterator();
				int defaultCounter = 0;
				
				while (iter.hasNext())
					if (iter.next().getValue() != null)
						defaultCounter++;

				// if the (params - default_params) > args
				// args should be more
				if (params.size() - defaultCounter > args.size()) {
					errorCounter++;
					errors.add(new Error(id, line,
						"Wrong call of \"" + id + "\" function"));
				}

				// else 
				// pass
			
			} else if (params.size() < args.size()) {
				// if params < args
				// args should be less
				errorCounter++;
				errors.add(new Error(id, line,
					"Wrong call of \"" + id + "\" function"));
			}

		} else {
			errorCounter++;
			errors.add(new Error(id, line,
				"Function \"" + id + "\" has not been defined"));
		
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