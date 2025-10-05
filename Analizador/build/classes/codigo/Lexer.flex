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

principal { return symbol(sym.PRINCIPAL); }
int { return symbol(sym.INT); }
[a-zA-Z_][a-zA-Z0-9_]* { return symbol(sym.ID, yytext()); }
[0-9]+ { return symbol(sym.NUM, yytext()); }
"+" { return symbol(sym.PLUS); }
[ \t\n\r]+ { /* ignorar */ }
. { System.err.println("Error lexico: " + yytext()); }