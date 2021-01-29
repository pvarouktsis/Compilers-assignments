import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Visitor2 extends DepthFirstAdapter {

	private Hashtable vartable;
	private Hashtable valtable;
	private Hashtable funcTable;
	private Hashtable retTable;
	private Utils utils;
	private List<Error> errors;
	private int errorCounter;
	private int debugCounter;

	public Visitor2(Hashtable vartable, Hashtable valtable, Hashtable funcTable, Hashtable retTable) {
		this.vartable = vartable;
		this.valtable = valtable;
		this.funcTable = funcTable;
		this.retTable = retTable;
		this.utils = new Utils("Visitor2", vartable, valtable, funcTable, retTable, errors, errorCounter);
		this.errors = new ArrayList<Error>();
		errorCounter = 0;
	}

	// check
	public void outAFunctionCall(AFunctionCall node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		// if function not exists
		if (!funcTable.containsKey(id)) {
			errorCounter++;
			errors.add(new Error(id, line,
				"Function \"" + id + "\" has not been defined"));
			return;
		}
    
    // if function exists
		List<AFunction> funcs = (ArrayList) funcTable.get(id);

    LinkedList<PExpression> args = node.getExpression();
    Iterator iterArgs = args.iterator();

		// check if any matches
		for (AFunction func : funcs) {
      
      LinkedList<AParameter> params = func.getParameter();
      Iterator iterParams = params.iterator();

      // if arguments < required parameters
      // then add error and return
      if (args.size() < utils.getSizeOfRequiredParameters(func)) {
        errorCounter++;
        errors.add(new Error(id, line,
          "Wrong call of \"" + id + "\" function"));
        return;
      }
      
      // add types to valtable
      while (iterArgs.hasNext()) {
        // if arguments > parameters
        // then add error and return
        if (!iterParams.hasNext()) {
          errorCounter++;
          errors.add(new Error(id, line,
            "Wrong call of \"" + id + "\" function"));
          return;
        }

        PExpression arg = (PExpression) iterArgs.next();

        AParameter param = (AParameter) iterParams.next();
        String paramId = param.getId().toString().trim();

        // already defined parameters in valtable
        // will be replace with new types
        valtable.put(paramId, (Type) utils.getType(arg));
      }
    }

		utils.printDebugInfo();
	}

	  /*
	   * Getters and Setters
	   */
    public Hashtable getValtable() {
    	return valtable;
    }

    public Hashtable getVartable() {
    	return vartable;
    }

    public Hashtable getFuncTable() {
    	return funcTable;
    }

    public Hashtable getRetTable() {
    	return retTable;
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
