package com.homerotl.ai.engines;

import com.homerotl.ai.Connotation;

import java.util.ArrayList;
import java.util.List;

/**
 * A sentence
 */
public final class Sentence {

    private Connotation connotation;

    private boolean definition;

    private List<Token> tokens;

    public Sentence(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Sentence(Connotation connotation, Token ... tokens) {
        this.connotation = connotation;
        if (this.tokens==null) {
            this.tokens = new ArrayList<Token>();
        }
        for (Token aToken : tokens) {
            this.tokens.add(aToken);
        }
    }

    public Connotation getConnotation() {
        return this.connotation;
    }
    public List<Token> getTokens() {
        return tokens;
    }

    public boolean containsUnknownTokens() {
        boolean unknownTokens = false;
        if (tokens!=null && tokens.size()>0) {
            for (Token aToken : tokens) {
                if (aToken.getType()==TokenType.UNKNOWN) {
                    return true;
                }
            }
        }
        return unknownTokens;
    }

    public Token getAnUnknownToken() {
        // TODO: Right now return the first unknown token, but in the future lets inquire
        // about them on a non-sequential way
        Token unknownToken = null;
        if (tokens!=null && tokens.size()>0) {
            for (Token aToken : tokens) {
                if (aToken.getType()==TokenType.UNKNOWN) {
                    return aToken;
                }
            }
        }
        return unknownToken;
    }

    public boolean isLexicalDefinition() {
        return definition;
    }

    public void setDefinition(boolean definition) {
        this.definition = definition;
    }

    public void setConnotation(Connotation connotation) {
        this.connotation = connotation;
    }
}
