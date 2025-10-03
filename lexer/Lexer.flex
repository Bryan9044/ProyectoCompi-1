%%

%class MiniLexer
%public
%unicode
%standalone

%%

principal { System.out.println("TOKEN: PRINCIPAL"); }
int { System.out.println("TOKEN: INT"); }
[a-zA-Z_][a-zA-Z0-9_]* { System.out.println("TOKEN: ID (" + yytext() + ")"); }
[0-9]+ { System.out.println("TOKEN: NUM (" + yytext() + ")"); }
"+" { System.out.println("TOKEN: PLUS"); }
[ \t\n\r]+ { }
. { System.err.println("Error lexico: " + yytext()); }