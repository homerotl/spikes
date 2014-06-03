package com.homerotl.ai.engines.rei;

import com.homerotl.ai.AIOutput;
import com.homerotl.ai.Connotation;
import com.homerotl.ai.engines.Sentence;
import com.homerotl.ai.engines.Source;

import java.util.List;

/**
 * A Rei worker thread
 */
public final class ReiThread extends Thread {

    private boolean running;
    private String latestInput;
    private Sentence latestOutput;

    private AIOutput output;
    private Lexic lex;
    private Semantic sem;
    private Syntax syn;
    private Source source;

    public ReiThread(Lexic lex, Syntax syn, Semantic sem, AIOutput output, Source source) {
        this.lex = lex;
        this.syn = syn;
        this.sem = sem;
        this.output = output;
        this.source = source;
    }

    public void input(String what, Connotation how) {
        latestInput = what;
    }

    public void stopWorker() {
        running = false;
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            if (latestInput != null && latestInput.length() > 0) {
                String aCopy = new String(latestInput);
                latestInput = null;
                Sentence response = null;
                try {
                    response = sem.process(syn.parseTokens(lex.parseInputString(aCopy, source)));
                } catch (UnknownTokenException ex) {
                    //response = new Sentence()
                }
                output.receiveAITextOutput(lex.prepareResponse(syn.prepareResponse(response)));
                output.receiveAIConnotationOutput(response.getConnotation());

            } else {
                // No interactive processing to do
                // Let's study!
                study();
            }
        }
    }

    private void study() {

        // TODO: Look at offline sources for more knowledge
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
