package com.homerotl.ai;

/**
 * Interface for AI Output
 */
public interface AIOutput {

    void receiveAITextOutput(String text);

    void receiveAIConnotationOutput(Connotation connotation);
}
