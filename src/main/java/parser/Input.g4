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
    'header'
        header=JAVA
    
    'tokens' '['
        tokensfromfile=tokenslist
    ']'

    'skip' '['
        tokenstoskip=skiplist
    ']'
    {
        String headerJava = $header.text;
        $desc = new FileDescription(
            new Header(headerJava.substring(2, headerJava.length() - 1)),
            $tokensfromfile.holder,
            $tokenstoskip.holder
        );
    }
;

skiplist returns [SkipTokensHolder holder] :
    currentskip=skiptoken ';' skiptail=skiplist
    {
            $holder = new SkipTokensHolder(
                ScalaUtils.<String>appendToList(
                    $skiptail.holder.tokens(),
                    $currentskip.holder.token()
                )
            );
    }

    | currentskip = skiptoken
    {
        $holder = new SkipTokensHolder(
            ScalaUtils.<String>singleElementList(
                $currentskip.holder.token()
            )
        );
    }

    |
    {
        $holder = new SkipTokensHolder(
            ScalaUtils.<String>emptyList()
        );
    }
;

skiptoken returns [SkipTokenHolder holder] :
    TOKEN_NAME
    {
        $holder = new SkipTokenHolder(
            $TOKEN_NAME.text
        );
    }
;

tokenslist returns [TokensHolder holder] :
    currenttoken=token ';' tokenslisttail=tokenslist
    {
        $holder = new TokensHolder(
            ScalaUtils.appendTupleToList(
                $tokenslisttail.holder.tokens(),
                $currenttoken.holder.name(),
                $currenttoken.holder.regexp()
            )
        );
    }

    | currenttoken=token
    {
        $holder = new TokensHolder(
            ScalaUtils.singleTupleList(
                $currenttoken.holder.name(),
                $currenttoken.holder.regexp()
            )
        );
    }

    |
    {
        $holder = new TokensHolder(
            ScalaUtils.emptyTupleList()
        );
    }
;

token returns [TokenHolder holder] :
    TOKEN_NAME '=' TOKEN_REGEXP
    {
        $holder = new TokenHolder($TOKEN_NAME.text, $TOKEN_REGEXP.text);
    }
;

WS : [ \n\t\r]+ -> skip;
JAVA : '{' (~[{}]+ JAVA?)* '}';
TOKEN_NAME : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')*;
// TODO: дать возможность указать \"
TOKEN_REGEXP : '"'(~["])*'"';
