package codigo;
import java_cup.runtime.*;

%%

%class Lexer
%public
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

%eofval{
    return symbol(sym.EOF);
%eofval}

%%

let { return symbol(sym.let_keyword); }
int { return symbol(sym.int_keyword); }

"=" { return symbol(sym.assignment_operator); }
"$" { return symbol(sym.delimiter); }

[a-zA-Z_][a-zA-Z0-9_]* { return symbol(sym.identifier, yytext()); }
[0-9]+ { return symbol(sym.int_literal, yytext()); }

"+" { return symbol(sym.plus_operator); }
"-" { return symbol(sym.minus_operator); }
"*" { return symbol(sym.multiplication_operator); }
"/" { return symbol(sym.division_operator); }

[ \t\n\r]+ { /* ignorar */ }
. { System.err.println("Error lexico: " + yytext()); }