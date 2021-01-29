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
      
      // TODO
      // hashtables here

      // run the visitors
      Visitor1 v1 = new Visitor1();
      ast.apply(v1);
      Visitor2 v2 = new Visitor2(v1.getVarTable(), v1.getValTable(), v1.getFuncTable(), v1.getRetTable());
      ast.apply(v2);

      // printErrors
      if ((v1.getErrorCounter() != 0) || 
          (v2.getErrorCounter() != 0)) {
        v1.printErrors();
        v2.printErrors();
      }

    } catch (Exception e) {
      System.err.println(e);
    }
  }

}
