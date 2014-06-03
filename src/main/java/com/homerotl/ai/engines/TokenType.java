package com.homerotl.ai.engines;

/**
 * Types of elements on the language
 */
public enum TokenType {

    ACK,
    VERB(true,"VERBS.DIC"),
    NOUN(true,"NOUNS.DIC"),
    ARTICLE(true,"ARTICLES.DIC"),
    ADJECTIVE(true,"ADJECTIVES.DIC"),
    SYMBOL(true,"SYMBOLS.DIC"),
    INTERJECTION(true,"INTERJECTION.DIC"),
    PRONOUN(true,"PRONOUN.DIC"),
    UNKNOWN;

    private boolean dictionary;

    private String fileName;

    TokenType() { }

    TokenType(boolean dictionary, String fileName) {
        this.dictionary = dictionary;
        this.fileName = fileName;
    }

    public boolean hasDictionary() {
        return dictionary;
    }

    public String getFileName() {
        return this.fileName;
    }

}
