package com.homerotl.ai.engines;

/**
 * Created by Homero on 5/10/14.
 */
public final class Token {

    private TokenType type;
    private String text;
    private Source source;
    private long id;
    private long timeStamp;

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(long id, String text, TokenType type, Source source, long timeStamp) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.source = source;
        this.timeStamp = timeStamp;
    }

    public Token(String text, TokenType type, Source source) {
        this.id = -1L;
        this.timeStamp = System.currentTimeMillis();
        this.text = text;
        this.type = type;
        this.source = source;
    }

    @Override
    public String toString() {
        if (type==TokenType.ACK) {
            return "OK";
        }
        return text;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public Source getSource() {
        return source;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof Token) ) return false;
        Token otherToken = (Token)other;
        return this.text.equals(otherToken.text) &&
                this.type == otherToken.type;
    }

}
