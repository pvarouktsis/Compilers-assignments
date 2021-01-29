import minipython.analysis.AnalysisAdapter;
import minipython.analysis.DepthFirstAdapter;
import minipython.node.*;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Hashtable;

public class Visitor1 extends DepthFirstAdapter {

	private Hashtable varTable;
	private Hashtable valTable;
	private Hashtable funcTable;
	private Hashtable retTable;
	private Utils utils;
	private List<Error> errors;
	private int errorCounter;

	public Visitor1() {
		this.varTable = new Hashtable();
		this.valTable = new Hashtable();
		this.funcTable = new Hashtable();
		this.retTable = new Hashtable();
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor1", varTable, valTable, funcTable, retTable, errors, errorCounter);
	}

	public Visitor1(Hashtable varTable, Hashtable valTable, Hashtable funcTable, Hashtable paramTable, Hashtable retTable) {
		this.varTable = varTable;
		this.valTable = valTable;
		this.funcTable = funcTable;
		this.retTable = retTable;
		this.errors = new ArrayList<Error>();
		this.errorCounter = 0;
		this.utils = new Utils("Visitor1", varTable, valTable, funcTable, retTable, errors, errorCounter);
	}

	// define functions
	public void inAFunction(AFunction node) {
		String id = node.getId().toString().trim();
		int line = node.getId().getLine();

		if (!utils.checkParametersOrder(node)) {
			errorCounter++;
			errors.add(new Error(id, line,
				"Default parameters of function \"" + id + "\" must be declared last"));

			return;
		}

		if (!funcTable.containsKey(id)) {
			funcTable.put(id, new ArrayList<AFunction>(5));
			((ArrayList) funcTable.get(id)).add(node);

			return;
		}

		if (!utils.functionExists(node)) {
			((ArrayList) funcTable.get(id)).add(node);

			return;
		}		
		
		errorCounter++;
		errors.add(new Error(id, line,
			"Function \"" + id + "\" has already been defined"));


		utils.printDebugInfo();

		return;
	}

	// define parameters
	public void inAParameter(AParameter node) {
		String id = node.getId().toString().trim();

		varTable.put(id, node);
		valTable.put( id, (Type) utils.getType(node.getValue()) );

		utils.printDebugInfo();
	}

	// define ids 
	public void inARegularAssignmentStatement(ARegularAssignmentStatement node) {
		String id = node.getId().toString().trim();

		varTable.put(id, node);
		valTable.put(id, (Type) utils.getType(node.getExpression()));

		utils.printDebugInfo();
	}

	// check
	public void outAIdExpression(AIdExpression node) {
		String id = node.getId().toString().trim();

		if (!varTable.containsKey(id)) {
			int line = node.getId().getLine();
			errorCounter++;
			errors.add(new Error(id, line,
				"Variable \"" + id + "\" has not been defined"));

			return;
		}

		return;
	}

	// check
	public void outAForStatement(AForStatement node) {
		String fid = node.getFirstId().toString().trim();
		String sid = node.getSecondId().toString().trim();

		// check first id
		if (!varTable.containsKey(fid)) {
			int fline = node.getFirstId().getLine();
			errorCounter++;
			errors.add(new Error(fid, fline,
				"Variable \"" + fid + "\" has not been defined"));

			return;
		}

		// check second id
		if (!varTable.containsKey(sid)) {
			int sline = node.getSecondId().getLine();
			errorCounter++;
			errors.add(new Error(sid, sline,
				"Variable \"" + sid + "\" has not been defined"));

			return;
		}

		return;
	}

	// define & check
	public void outAAdditionExpression(AAdditionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}

	// define & check
	public void outASubtractionExpression(ASubtractionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}

	// define & check
	public void outAMultiplicationExpression(AMultiplicationExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}

	// define & check
	public void outADivisionExpression(ADivisionExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}

	// define & check
	public void outAModuloExpression(AModuloExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}

	// define & check
	public void outAPowerExpression(APowerExpression node) {
		PExpression leftExp = node.getLeftExpression();
		PExpression rightExp = node.getRightExpression();	

		checkType(node, leftExp, rightExp);

		utils.printDebugInfo();
	}


	// helper
	public void checkType(PExpression node, PExpression leftExp, PExpression rightExp) {
		String id = node.toString().trim();

		if (utils.getType(leftExp) == utils.getType(rightExp)) {
			valTable.put(id, (Type) utils.getType(leftExp)); // either left or right could work
		} else {
			int line = -1;
			errorCounter++;
			errors.add(new Error(id, line,
				"Unsupported operation"));
		}

		return;
	}

    /*
     * Getters and Setters
     */
    public Hashtable getVarTable() {
    	return varTable;
    }

    public Hashtable getValTable() {
    	return valTable;
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

