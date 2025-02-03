grammar vg_lang;
program
    : statement* EOF
    ;

statement
    : variableDeclaration
    | assignment
    | printStatement
    | comments
    | expressionStatement
    ;

variableDeclaration
    : 'var' IDENTIFIER '=' expression ';'
    ;
assignment
    : leftHandSide '=' expression ';'
    ;
leftHandSide
    : IDENTIFIER ( '[' expression ']' )*
    ;
printStatement
    : 'print' '(' expression (',' expression)* ')' ';'
    ;
expression
    : logicalOrExpression
    ;

logicalOrExpression
    : logicalAndExpression ( '||' logicalAndExpression )*
    ;

logicalAndExpression
    : equalityExpression ( '&&' equalityExpression )*
    ;

equalityExpression
    : relationalExpression ( ( '==' | '!=' ) relationalExpression )*
    ;

relationalExpression
    : additiveExpression ( ( '<' | '<=' | '>' | '>=' ) additiveExpression )*
    ;

additiveExpression
    : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
    ;

multiplicativeExpression
    : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
    ;

unaryExpression
    : ( '+' | '-' | '!' ) unaryExpression
    | postfixExpression
    ;

postfixExpression
    : primary ( '[' expression ']' )*
    ;

 primary : literal
    | IDENTIFIER
    | '(' expression ')'
    ;

 literal
     : INT
     | DOUBLE
     | STRING_LITERAL
     | TRUE
     | FALSE
     ;
expressionStatement
    : expression ';'
    ;
comments
    : SINGLE_LINE_COMMENT
    | MULTI_LINE_COMMENT
    ;

SINGLE_LINE_COMMENT
    : '##' ~[\r\n]* -> skip
    ;

MULTI_LINE_COMMENT
    : '/#' .*? '#/' -> skip
    ;
IDENTIFIER
    : [a-zA-Z_] [a-zA-Z_0-9]*
    ;
TRUE    : 'true';
FALSE   : 'false';
INT
    : [0-9]+
    ;

DOUBLE
    : [0-9]+ '.' [0-9]+
    ;

STRING_LITERAL
    : '"' ( ESC_SEQ | ~["\\\r\n] )* '"'
    ;

fragment ESC_SEQ
    : '\\' [btnfr"\\]
    | UNICODE_ESC
    ;

fragment UNICODE_ESC
    : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment HEX_DIGIT
    : [0-9a-fA-F]
    ;
WS
    : [ \t\r\n]+ -> skip
    ;