package com.homerotl.ai.ui;

import com.homerotl.ai.AIOutput;
import com.homerotl.ai.AiInterface;
import com.homerotl.ai.Connotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Homero on 5/10/14.
 */
public final class TopFrame extends JFrame implements AIOutput {

    private AiInterface ai;

    private JTextField inputField;
    private JTextArea outputArea;
    private JButton sendMessageButton;
    private JButton positiveFeedback;
    private JButton negativeFeedback;

    public TopFrame() {
        super("GolliroAI");
        setResizable(false);
        setSize(300,300);
        setLocationRelativeTo(null);

        setupComponents();
    }

    @Override
    public void receiveAITextOutput(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputArea.append(text+"\r\n");
            }
        });
    }

    @Override
    public void receiveAIConnotationOutput(Connotation connotation) {
        // TODO: Nothing here yet
    }

    public void setupComponents() {

        inputField = new JTextField();
        outputArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputArea);

        outputArea.setEditable(false);
        sendMessageButton = new JButton("say");
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        topPanel.add(inputField,BorderLayout.CENTER);
        topPanel.add(sendMessageButton,BorderLayout.LINE_END);

        positiveFeedback = new JButton("+");
        negativeFeedback = new JButton("-");

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        bottomPanel.add(positiveFeedback, BorderLayout.LINE_START);
        bottomPanel.add(negativeFeedback, BorderLayout.LINE_END);

        this.getContentPane().add(topPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.getContentPane().add(bottomPanel,BorderLayout.PAGE_END);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void setAi(final AiInterface ai) {
        this.ai = ai;
        sendMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputField.getText()!=null && inputField.getText().trim().length()>0) {
                    outputArea.append(">>"+inputField.getText()+"\r\n");
                    String textToSend = inputField.getText()+"";
                    inputField.setText("");
                    ai.input(textToSend, Connotation.NEUTRAL);
                }
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ai.stop();
                System.out.println("Before system exit");
                System.exit(0);
            }
        });
    }
}
