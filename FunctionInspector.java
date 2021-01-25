import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class FunctionInspector extends DepthFirstAdapter {

	private Hashtable vartable;
	private List<Error> errors;
	private int errorCounter;

	public FunctionInspector(Hashtable vartable) {
		this.vartable = vartable;
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	// check
	public void inAFunctionCall(AFunctionCall node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		// if function exists
		if (vartable.containsKey(id)) {
			LinkedList<PExpression> args = node.getExpression();
			LinkedList<AParameter> params = ((AFunction) vartable.get(id)).getParameter();

			// if params == args
			if (params.size() == args.size()) {
				// args are ok
			
			// if params > args 
			} else if (params.size() > args.size()) {
				// count the default parameters
				Iterator<AParameter> iter = params.iterator();
				int defaultCounter = 0;
				while (iter.hasNext())
					if (iter.next().getValue() != null)
						defaultCounter++;

				// if (params - default_params) > args
				if (params.size() - defaultCounter > args.size()) {
					// args should be more					
					errorCounter++;
					errors.add(new Error(id, line,
						"Wrong call of \"" + id + "\" function"));
				}

				// else 
				// args are ok
			
			} else if (params.size() < args.size()) {
				// if params < args
				// args should be less
				errorCounter++;
				errors.add(new Error(id, line,
					"Wrong call of \"" + id + "\" function"));
			}

		// if function not exists
		} else {
			// function has not been defined
			errorCounter++;
			errors.add(new Error(id, line,
				"Function \"" + id + "\" has not been defined"));
		
		}
	
	}

    public Hashtable getVartable() {
    	return vartable;
    }

    public void printVartable() {
    	System.out.println(vartable);
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