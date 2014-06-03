package com.homerotl.ai.engines.rei;

import com.homerotl.ai.engines.Token;

/**
 * Exception to handle unknown tokens
 */
public class UnknownTokenException extends Exception {
    private Token token;

    public UnknownTokenException(Token token) {
        super();
        this.token =  token;
    }

    public Token getToken() {
        return token;
    }
}
