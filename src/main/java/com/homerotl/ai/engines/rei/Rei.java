package com.homerotl.ai.engines.rei;

import com.homerotl.ai.AIOutput;
import com.homerotl.ai.AiInterface;
import com.homerotl.ai.Connotation;
import com.homerotl.ai.engines.Sentence;
import com.homerotl.ai.engines.Source;
import com.homerotl.ai.engines.SourceType;
import com.homerotl.ai.engines.TokenType;

/**
 * Rei implementation of the Ai Interface
 */
public final class Rei implements AiInterface {

    private Lexic lex;
    private Syntax syn;
    private Semantic sem;

    private AIOutput output;
    private ReiThread worker;

    @Override
    public void setup(AIOutput output) {
        this.output = output;

        lex = new Lexic();
        syn = new Syntax(lex);
        sem = new Semantic(lex, syn);

        lex.readDictionaries();

        initialSetup();

        sem.loadIdeas();

        worker = new ReiThread(lex, syn, sem, this.output, lex.getSourceByName("HOMERO"));
        worker.start();
    }

    private void initialSetup() {

        if (!lex.hasSourceByName("HOMERO")) { lex.addSource(new Source(SourceType.DOGMA,"HOMERO"));  }

        if (!lex.hasToken("YOU")) { lex.addToken("YOU", TokenType.NOUN, lex.getSourceByName("HOMERO")); }
        if (!lex.hasToken("I")) { lex.addToken("I", TokenType.NOUN, lex.getSourceByName("HOMERO")); }

        for (TokenType tt : TokenType.values()) {
            if (tt.hasDictionary()) {
                if (!lex.hasToken(tt.name())) { lex.addToken(tt.name(), TokenType.NOUN, lex.getSourceByName("HOMERO")); }
            }
        }

        if (!lex.hasToken("IS")) { lex.addToken("IS", TokenType.VERB, lex.getSourceByName("HOMERO")); }
        if (!lex.hasToken("ARE")) { lex.addToken("ARE", TokenType.VERB, lex.getSourceByName("HOMERO")); }
        if (!lex.hasToken("AM")) { lex.addToken("AM", TokenType.VERB, lex.getSourceByName("HOMERO")); }

        if (!lex.hasToken("A")) { lex.addToken("A", TokenType.ARTICLE, lex.getSourceByName("HOMERO")); }
        if (!lex.hasToken("AN")) { lex.addToken("AN", TokenType.ARTICLE, lex.getSourceByName("HOMERO")); }

        if (!lex.hasToken("?")) { lex.addToken("?", TokenType.SYMBOL, lex.getSourceByName("HOMERO")); }
    }

    @Override
    public void input(String what, Connotation how) {
        worker.input(what, how);
    }

    @Override
    public void stop() {

        // Write down dictionary
        lex.saveDictionaries();
        sem.saveIdeas();
        worker.stopWorker();
        try {
            worker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void positiveFeedback() {

    }

    @Override
    public void negativeFeedback() {

    }

}
