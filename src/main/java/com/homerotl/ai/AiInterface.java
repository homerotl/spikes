package com.homerotl.ai;

/**
 * Interface to be implemented by AI engines
 */
public interface AiInterface {

    void setup(AIOutput output);

    /**
     * Input
     * @param what
     * @param how
     */
    void input(String what, Connotation how);

    void stop();

    void positiveFeedback();

    void negativeFeedback();
}
