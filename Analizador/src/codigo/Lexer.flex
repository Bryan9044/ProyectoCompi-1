package codigo;

import java_cup.runtime.*;
import java.io.*;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{
    private PrintWriter logWriter;
    
    // Constructor que acepta un archivo de salida para los tokens
    public Lexer(Reader in, String outputFile) throws IOException {
        this(in);
        logWriter = new PrintWriter(new FileWriter(outputFile));
        logWriter.println("=== ANÁLISIS LÉXICO ===");
        logWriter.println("TOKEN\t\t\tLEXEMA");
        logWriter.println("----------------------------------------");
    }
    
    private Symbol symbol(int type) {
        return symbol(type, null);
    }

    private Symbol symbol(int type, Object value) {
        // Escribir en el archivo de log
        if (logWriter != null) {
            String tokenName = getTokenName(type);
            String lexeme = (value != null) ? value.toString() : "";
            logWriter.printf("%-20s\t%s%n", tokenName, lexeme);
            logWriter.flush();
        }
        return new Symbol(type, yyline, yycolumn, value);
    }
    
    private String getTokenName(int type) {
        switch(type) {
            case sym.plus_operator: return "PLUS_OPERATOR";
            case sym.minus_operator: return "MINUS_OPERATOR";
            case sym.multiplication_operator: return "MULT_OPERATOR";
            case sym.division_operator: return "DIV_OPERATOR";
            case sym.int_division_operator: return "INT_DIV_OPERATOR";
            case sym.modulo_operator: return "MOD_OPERATOR";
            case sym.power_operator: return "POWER_OPERATOR";
            case sym.increment_operator: return "INCREMENT";
            case sym.decrement_operator: return "DECREMENT";
            case sym.left_parenthesis: return "LEFT_PAREN";
            case sym.right_parenthesis: return "RIGHT_PAREN";
            case sym.left_block: return "LEFT_BLOCK";
            case sym.right_block: return "RIGHT_BLOCK";
            case sym.assignment_operator: return "ASSIGN";
            case sym.equal_operator: return "EQUAL";
            case sym.not_equal_operator: return "NOT_EQUAL";
            case sym.greater_operator: return "GREATER";
            case sym.less_operator: return "LESS";
            case sym.greater_equal_operator: return "GREATER_EQUAL";
            case sym.less_equal_operator: return "LESS_EQUAL";
            case sym.or_operator: return "OR";
            case sym.and_operator: return "AND";
            case sym.not_operator: return "NOT";
            case sym.comma_keyword: return "COMMA";
            case sym.delimiter: return "DELIMITER";
            case sym.int_literal: return "INT_LITERAL";
            case sym.float_literal: return "FLOAT_LITERAL";
            case sym.bool_literal: return "BOOL_LITERAL";
            case sym.char_literal: return "CHAR_LITERAL";
            case sym.string_literal: return "STRING_LITERAL";
            case sym.identifier: return "IDENTIFIER";
            case sym.int_keyword: return "INT";
            case sym.float_keyword: return "FLOAT";
            case sym.bool_keyword: return "BOOL";
            case sym.char_keyword: return "CHAR";
            case sym.string_keyword: return "STRING";
            case sym.void_keyword: return "VOID";
            case sym.principal_keyword: return "PRINCIPAL";
            case sym.let_keyword: return "LET";
            case sym.input_keyword: return "INPUT";
            case sym.output_keyword: return "OUTPUT";
            case sym.loop_keyword: return "LOOP";
            case sym.exit_when_keyword: return "EXIT_WHEN";
            case sym.end_loop_$_keyword: return "END_LOOP$";
            case sym.for_keyword: return "FOR";
            case sym.step_keyword: return "STEP";
            case sym.to_keyword: return "TO";
            case sym.downto_keyword: return "DOWNTO";
            case sym.do_keyword: return "DO";
            case sym.return_keyword: return "RETURN";
            case sym.break_keyword: return "BREAK";
            case sym.decide_of_keyword: return "DECIDE_OF";
            case sym.elseif_keyword: return "ELIF";
            case sym.else_keyword: return "ELSE";
            case sym.EOF: return "EOF";
            default: return "UNKNOWN";
        }
    }
    
    public void closeLog() {
        if (logWriter != null) {
            logWriter.println("----------------------------------------");
            logWriter.println("=== FIN DEL ANÁLISIS ===");
            logWriter.close();
        }
    }
%}

/* Caracteres básicos */
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Digit = [0-9]
Letter = [a-zA-Z]
Underscore = _

/* Identificadores */
Identifier = {Letter}({Letter}|{Digit}|{Underscore})*

/* Enteros */
IntegerZero = 0
IntegerPositive = [1-9]{Digit}*
IntegerNegative = "-"{IntegerPositive}
IntegerLiteral = {IntegerZero}|{IntegerPositive}|{IntegerNegative}

/* Flotantes */
FloatZero = 0\.0
FloatPositiveDecimal = 0\.{Digit}*[1-9]+
FloatNegativeDecimal = "-"{FloatPositiveDecimal}
FloatWithInteger = [1-9]{Digit}*\.{Digit}*[1-9]+
FloatWithIntegerZeroDecimal = [1-9]{Digit}*\.0
FloatNegativeWithInteger = "-"{FloatWithInteger}
FloatNegativeWithIntegerZero = "-"{FloatWithIntegerZeroDecimal}
FloatLiteral = {FloatZero}|{FloatPositiveDecimal}|{FloatNegativeDecimal}|{FloatWithInteger}|{FloatWithIntegerZeroDecimal}|{FloatNegativeWithInteger}|{FloatNegativeWithIntegerZero}
CharLiteral = \'[^\']\'
StringLiteral = \"([^\"\n\\]|\\n)*\"

%%

<YYINITIAL> {
    /* Espacios en blanco */
    {WhiteSpace}        { /* ignorar */ }


    "++"                { return symbol(sym.increment_operator, yytext()); }
    "--"                { return symbol(sym.decrement_operator, yytext()); }
    "//"                { return symbol(sym.int_division_operator, yytext()); } 
    "=="                { return symbol(sym.equal_operator, yytext()); }
    "!="                { return symbol(sym.not_equal_operator, yytext()); }
    ">="                { return symbol(sym.greater_equal_operator, yytext()); }
    "<="                { return symbol(sym.less_equal_operator, yytext()); }


    "~"                 { return symbol(sym.or_operator, yytext()); }
    "@"                 { return symbol(sym.and_operator, yytext()); } 
    "Σ"                 { return symbol(sym.not_operator, yytext()); }


    /* Operadores normales */
    "+"                 { return symbol(sym.plus_operator, yytext()); }
    "-"                 { return symbol(sym.minus_operator, yytext()); }
    "*"                 { return symbol(sym.multiplication_operator, yytext()); }
    "/"                 { return symbol(sym.division_operator, yytext()); }
    "%"                 { return symbol(sym.modulo_operator, yytext()); }
    "^"                 { return symbol(sym.power_operator, yytext()); }
    ">"                 { return symbol(sym.greater_operator, yytext()); }
    "<"                 { return symbol(sym.less_operator, yytext()); }
    "!"                 { return symbol(sym.left_exclamation, yytext()); }
    "¡"                 { return symbol(sym.right_exclamation, yytext()); }
    "="                 { return symbol(sym.assignment_operator, yytext()); }

    /* Paréntesis y bloques o llaves */
    "є"                 { return symbol(sym.left_parenthesis, yytext()); }
    "э"                 { return symbol(sym.right_parenthesis, yytext()); }
    "¿"                 { return symbol(sym.left_block, yytext()); }
    "?"                 { return symbol(sym.right_block, yytext()); }

    /* Separadores */
    ","                 { return symbol(sym.comma_keyword, yytext()); }
    "$"                 { return symbol(sym.delimiter, yytext()); }

    "int"             { return symbol(sym.int_keyword, yytext()); }
    "float"           { return symbol(sym.float_keyword, yytext()); }
    "bool"            { return symbol(sym.bool_keyword, yytext()); }
    "char"            { return symbol(sym.char_keyword, yytext()); }
    "string"          { return symbol(sym.string_keyword, yytext()); }
    "void"            { return symbol(sym.void_keyword, yytext()); }
    "principal"       { return symbol(sym.principal_keyword, yytext()); }
    "let"             { return symbol(sym.let_keyword, yytext()); }
    "input"           { return symbol(sym.input_keyword, yytext()); }
    "output"          { return symbol(sym.output_keyword, yytext()); }
    "loop"            { return symbol(sym.loop_keyword, yytext()); }
    "exit when"      { return symbol(sym.exit_when_keyword, yytext()); }
    "end loop$"      { return symbol(sym.end_loop_$_keyword, yytext()); }
    "for"            { return symbol(sym.for_keyword, yytext()); }
    "step"          { return symbol(sym.step_keyword, yytext()); }
    "to"        { return symbol(sym.to_keyword, yytext()); }
    "downto"       { return symbol(sym.downto_keyword, yytext()); }
    "do"    { return symbol(sym.do_keyword, yytext()); }
    "return"   { return symbol(sym.return_keyword, yytext()); }
    "break"    { return symbol(sym.break_keyword, yytext()); }
    "decide of"     { return symbol(sym.decide_of_keyword, yytext()); }
    "elif"       { return symbol(sym.elseif_keyword, yytext()); }
    "else"       { return symbol(sym.else_keyword, yytext()); }

    "True"              { return symbol(sym.bool_literal, yytext()); }
    "False"             { return symbol(sym.bool_literal, yytext()); }

    /* Literales */
    {IntegerLiteral}    { return symbol(sym.int_literal, yytext()); }
    {FloatLiteral}      { return symbol(sym.float_literal, yytext()); }
    {CharLiteral}       { 
        String text = yytext();
        return symbol(sym.char_literal, text.substring(1, text.length()-1));
    }
    {StringLiteral}     { 
        String text = yytext();
        return symbol(sym.string_literal, text.substring(1, text.length()-1));
    }

    //ID
    {Identifier}        { return symbol(sym.identifier, yytext()); }




    /* Error */
    .                   { throw new Error("Carácter ilegal en línea " + (yyline+1) + 
                                        ": '" + yytext() + "'"); }
}

<<EOF>>                 { 
                          closeLog();
                          return symbol(sym.EOF); 
                        }