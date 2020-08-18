grammar UCUM;

// Derived from the UCUM syntax described at: http://unitsofmeasure.org/ucum.html#section-Grammar-of-Units-and-Unit-Terms

mainTerm
    : term EOF
    ;

term
    : component
    | '/' term
    | component '/' term
    | component '.' term
    ;

component
    : '(' term ')'
    | '(' term ')' annotationSymbols
    | annotatable annotationSymbols
    | annotationSymbols
    | annotatable
    | digitSymbols
    ;

annotatable
    : simpleUnit
    | simpleUnit exponent
    ;

simpleUnit
    : simpleUnitSymbols
    | squareBracketsSymbols
    | squareBracketsSymbols simpleUnitSymbols
    | simpleUnitSymbols squareBracketsSymbols
    | simpleUnitSymbols squareBracketsSymbols simpleUnitSymbols
    ;

simpleUnitSymbols
    : (terminalUnitSymbol)+
    ;

annotationSymbols
    : '{' (withinCbSymbol)+ '}'
    ;

squareBracketsSymbols
    : '[' (withinSbSymbol)+ ']'
    ;

withinSbSymbol
    : withinCbOrSbSymbol
    | '{'
    | '}'
    ;

withinCbSymbol
    : withinCbOrSbSymbol
    | ' '
    | '['
    | ']'
    ;

withinCbOrSbSymbol
    : terminalUnitSymbol
    | '"'
    | '('
    | ')'
    | '+'
    | '-'
    | '.'
    | '/'
    | '='
    ;

terminalUnitSymbol
    : NON_DIGIT_TERMINAL_UNIT_SYMBOL
    | DIGIT_SYMBOL
    ;

exponent
    : ('+' | '-') digitSymbols
    | digitSymbols
    ;

digitSymbols
    : (DIGIT_SYMBOL)+
    ;

NON_DIGIT_TERMINAL_UNIT_SYMBOL
    : '!'
    | '#'
    | '$'
    | '%'
    | '&'
    | '\''
    | '*'
    | ','
    | ':'
    | ';'
    | '<'
    | '>'
    | '?'
    | '@'
    | 'A' .. 'Z'
    | '\\'
    | '^'
    | '_'
    | '`'
    | 'a' .. 'z'
    | '|'
    | '~'
    ;

DIGIT_SYMBOL
    : '0' .. '9'
    ;
