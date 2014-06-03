package com.homerotl.ai.ui;

import com.homerotl.ai.AiInterface;
import com.homerotl.ai.engines.rei.Rei;

/**
 * Main class for the UI
 */
public class GolliroStartUI {

    public static void main(String[] args) {

        // Setup the UI
        TopFrame topFrame = new TopFrame();

        // Setup the AI engine
        AiInterface aiImpl = new Rei();
        aiImpl.setup(topFrame);

        topFrame.setAi(aiImpl);

        topFrame.setVisible(true);

    }
}
