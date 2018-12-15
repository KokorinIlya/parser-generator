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

    'rules' '['
        ruleslist=rules
    ']'
    {
        String headerJava = $header.text.trim();
        $desc = new FileDescription(
            new Header(headerJava.substring(2, headerJava.length() - 1)),
            $tokensfromfile.holder,
            $tokenstoskip.holder,
            $ruleslist.holder
        );
    }
;

rules returns [RulesHolder holder] :
    currentrule=parserrule ';;' ruleslisttail=rules
    {
        $holder = new RulesHolder(
            ScalaUtils.<Rule>appendToList(
                $ruleslisttail.holder.rules(),
                $currentrule.bnfRule
            )
        );
    }

    |
    {
        $holder = new RulesHolder(
            ScalaUtils.<Rule>emptyList()
        );
    }
;

parserrule returns [Rule bnfRule] :
    rulename=NAME '(' curparams=parametersrule ')' '->' result=parameterrule '::' '=' curalternatives=rulealternatives
    {
        $bnfRule = new Rule(
            $curalternatives.holder,
            $curparams.holder,
            $result.param
        );
    }
;

parametersrule returns [ParametersHolder holder] :
    list=parameterslistrule
    {
        $holder = $list.holder;
    }

    |
    {
        $holder = new ParametersHolder(
            ScalaUtils.<Parameter>emptyList()
        );
    }
;

parameterslistrule returns [ParametersHolder holder] :
    singeparameter=parameterrule ',' parameterslisttail=parameterslistrule
    {
        $holder = new ParametersHolder(
            ScalaUtils.<Parameter>appendToList(
                $parameterslisttail.holder.parameters(),
                $singeparameter.param
            )
        );
    }

    | singleparameter=parameterrule
    {
        $holder = new ParametersHolder(
            ScalaUtils.<Parameter>singleElementList($singleparameter.param)
        );
    }
;

parameterrule returns [Parameter param] :
    paramname=NAME ':' paramtype=NAME
    {
        $param = new Parameter(
            $paramname.text,
            $paramtype.text
        );
    }
;

rulealternatives returns [AlternativesHolder holder] :
    currentalternative=alternative alternativestail=rulealternativesmaybenull
    {
        $holder = new AlternativesHolder(
            ScalaUtils.<RuleBody>appendToList(
                $alternativestail.holder.alternatives(),
                $currentalternative.body
            )
        );
    }
;

rulealternativesmaybenull returns [AlternativesHolder holder] :
    '|' currentalternative=alternative alternativestail=rulealternativesmaybenull
    {
        $holder = new AlternativesHolder(
            ScalaUtils.<RuleBody>appendToList(
                $alternativestail.holder.alternatives(),
                $currentalternative.body
            )
        );
    }

    |
    {
        $holder = new AlternativesHolder(
            ScalaUtils.<RuleBody>emptyList()
        );
    }
;

alternative returns [RuleBody body] :
    currentcode=maybecoderule curassignment=assignmentrule alternativetail=alternative
    {
        RuleBodyEntry curEntry = new OrdinaryRuleBodyEntry(
            $currentcode.holder,
            $curassignment.ass
        );
        BodyEntries curEntries = new BodyEntries(
            ScalaUtils.<RuleBodyEntry>appendToList(
                $alternativetail.body.entries().entries(),
                curEntry
            )
        );
        $body = new RuleBody(
            curEntries,
            $alternativetail.body.resultCode()
        );
    }

    | currentcode=maybecoderule 'eps' resultcode=maybecoderule
    {
     RuleBodyEntry curEntry = new EpsilonRuleBodyEntry(
         $currentcode.holder
     );
     BodyEntries curEntries = new BodyEntries(
         ScalaUtils.<RuleBodyEntry>appendToList(
             curEntry
         )
     );
     $body = new RuleBody(
         curEntries,
         $resultcode.holder
     );
    }

    | currentcode=maybecoderule
    {
        $body = new RuleBody(
            new BodyEntries(
                ScalaUtils.<RuleBodyEntry>emptyList()
            ),
            $currentcode.holder
        );
    }
;

assignmentrule returns [Assignment ass] :
    variablename=NAME '=' tokenname=NAME
    {
        $ass = new TokenAssignment(
            $variablename.text,
            $tokenname.text
        );
    }

    | variablename=NAME '=' rulename=NAME '(' assarguments=argumentsrule ')'
    {
        $ass = new RuleAssignment(
            $variablename.text,
            $rulename.text,
            $assarguments.holder
        );
    }
;

argumentsrule returns [ArgumentsHolder holder] :
    argslist=argumentslistrule
    {
        $holder = $argslist.holder;
    }

    |
    {
        $holder = new ArgumentsHolder(
            ScalaUtils.<String>emptyList()
        );
    }
;

argumentslistrule returns [ArgumentsHolder holder] :
    singleargument=NAME ',' agrumentslisttail=argumentslistrule
    {
        $holder = new ArgumentsHolder(
            ScalaUtils.<String>appendToList(
                $agrumentslisttail.holder.arguments(),
                $singleargument.text
            )
        );
    }

    | singleargument=NAME
    {
        $holder = new ArgumentsHolder(
            ScalaUtils.<String>singleElementList(
               $singleargument.text
            )
        );
    }
;


maybecoderule returns [CodeHolder holder] :
    code=JAVA
    {
        $holder = new CodeHolder(
            ScalaUtils.<String>fullOption($code.text)
        );
    }

    |
    {
        $holder = new CodeHolder(
            ScalaUtils.<String>emptyOption()
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
    NAME
    {
        $holder = new SkipTokenHolder(
            $NAME.text
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
    NAME '=' TOKEN_REGEXP
    {
        $holder = new TokenHolder($NAME.text, $TOKEN_REGEXP.text);
    }
;

WS : [ \n\t\r]+ -> skip;
JAVA : '{' (~[{}]+ JAVA?)* '}';
NAME : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')*;
// TODO: дать возможность указать \"
TOKEN_REGEXP : '"'(~["])*'"';
