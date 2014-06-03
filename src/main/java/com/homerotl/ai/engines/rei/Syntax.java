package com.homerotl.ai.engines.rei;

import com.homerotl.ai.Connotation;
import com.homerotl.ai.engines.Sentence;
import com.homerotl.ai.engines.Token;
import com.homerotl.ai.engines.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Syntax module
 */
public class Syntax {

    private Lexic lex;

    private static final TokenType[] DEFINITION = {
        TokenType.UNKNOWN , TokenType.VERB, TokenType.ARTICLE, TokenType.NOUN
    };

    public Syntax(Lexic lex) {
        this.lex = lex;
    }

    public Sentence parseTokens(List<Token> tokens) {
        Sentence sentence = new Sentence(tokens);
        sentence.setDefinition(isDefinition(tokens));
        sentence.setConnotation(isQuestion(tokens)?Connotation.QUESTION:Connotation.NEUTRAL);
        return sentence;
    }

    private boolean isQuestion(List<Token> tokens) {
        return tokens.get(tokens.size()-1).toString().equals("?");
    }

    private boolean isDefinition(List<Token> tokens) {

        if (!syntaxMatch(tokens, DEFINITION)) {
            return false;
        }

        return (tokens.get(1).toString().equals("IS") &&
                (
                tokens.get(2).toString().equals("A") || tokens.get(2).toString().equals("AN"))&&
                    tokens.get(3).getType()==TokenType.NOUN);

    }

    public boolean syntaxMatch(List<Token> tokens, TokenType[] syntax) {
        if (tokens.size()!=syntax.length) {
            return false;
        }
        for (int i=0; i<syntax.length;i++) {
            if (tokens.get(i).getType()!=syntax[i]) {
                return false;
            }
        }
        return true;
    }

    public List<Token> prepareResponse(Sentence response) {
        if (response.getConnotation().equals(Connotation.QUESTION)) {
            List<Token> tokens =  new ArrayList<Token>();
            tokens.addAll(response.getTokens());
            tokens.add(lex.getTokenByText("?"));
            return tokens;
        }
        return response.getTokens();
    }

}
