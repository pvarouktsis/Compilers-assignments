import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class FunctionAnalyzer extends DepthFirstAdapter {
	private Hashtable vartable;
	private Hashtable valtable;
	private List<Error> errors;
	private int errorCounter;
	private int debugCounter;

	public FunctionAnalyzer() {
		this.vartable = new Hashtable();
		this.valtable = new Hashtable();
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.debugCounter = 0;
	}

	public FunctionAnalyzer(Hashtable vartable, Hashtable valtable) {
		this.vartable = vartable;
		this.valtable = valtable;
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.debugCounter = 0;
	}

	// definition
	public void inAFunction(AFunction node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (vartable.containsKey(id)) {
			// pass

		} else {
			vartable.put(id, node);
			// TODO
		}

		printDebugInfo();
	}


	public void printDebugInfo() {
		System.out.println("\nCounter:" + ++debugCounter);
		System.out.print("vartable: ");
		printVartable();
		System.out.print("valtable: ");
		printValtable();

	}

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
