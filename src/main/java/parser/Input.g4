grammar Input;

options {
    language = Java;
}

@header {
package parser;
import input.*;
import utils.ScalaUtils;
}

inputfile returns [FileDescription desc] :
    HEADER LEFT_BRACKET
        headercode=javacode
    RIGHT_BRACKET

    TOKENS LEFT_BRACKET
        tokensfromfile=tokenslist
    RIGHT_BRACKET
    {
        $desc = new FileDescription(new Header($headercode.code), $tokensfromfile.holder);
    }
;

tokenslist returns [TokensHolder holder] :
    currenttoken=token SEMI tokenslisttail=tokenslist
    {
        $holder = new TokensHolder(
            ScalaUtils.appendToList(
                $tokenslisttail.holder.tokens(),
                $currenttoken.holder.name(),
                $currenttoken.holder.regexp()
            )
        );
    }

    | currenttoken=token
    {
        $holder = new TokensHolder(
            ScalaUtils.singleElementList(
                $currenttoken.holder.name(),
                $currenttoken.holder.regexp()
            )
        );
    }

    |
    {
        $holder = new TokensHolder(
            ScalaUtils.emptyList()
        );
    }
;

token returns [TokenHolder holder] :
    TOKEN_NAME EQ TOKEN_REGEXP
    {
        $holder = new TokenHolder($TOKEN_NAME.text, $TOKEN_REGEXP.text);
    }
;

javacode returns [String code] :
    JAVA
    {
        $code = $JAVA.text;
    }
;

WS : [ \n\t\r]+ -> skip;
JAVA : 'hh';
LEFT_BRACKET : '{';
RIGHT_BRACKET : '}';
HEADER : 'header';
TOKENS : 'tokens';
SEMI : ';';
TOKEN_NAME : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')*;
TOKEN_REGEXP : ('a'..'z'|'A'..'Z'|'0'..'9'|'+'|'*')+;
EQ : '=';
