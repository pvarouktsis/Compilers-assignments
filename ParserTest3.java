import java.io.*;

import minipython.lexer.Lexer;
import minipython.parser.Parser;

import minipython.node.Start;

import java.util.Hashtable;


public class ParserTest3 {
  public static void main(String[] args) {
    try {
      Parser parser = 
        new Parser(
        new Lexer(
        new PushbackReader(
        new FileReader(args[0].toString()), 1024)));

      Start ast = parser.parse();
      
      // run the visitors
      VariableAnalyzerInspector vai = new VariableAnalyzerInspector();
      ast.apply(vai);
      FunctionAnalyzer fa = new FunctionAnalyzer(vai.getVartable(), vai.getValtable());
      ast.apply(fa);
      FunctionInspector fi = new FunctionInspector(fa.getVartable());
      ast.apply(fi);

      // print symbol table
      // System.out.println();
      // va.printVartable();
      // va.printValtable();
      // System.out.println();
      
      // printErrors
      if ((vai.getErrorCounter() != 0) || 
          (fa.getErrorCounter() != 0) || 
          (fi.getErrorCounter() != 0)) {
        vai.printErrors();
        fa.printErrors();
        fi.printErrors();
      }

    } catch (Exception e) {
      System.err.println(e);
    }
  }

}
