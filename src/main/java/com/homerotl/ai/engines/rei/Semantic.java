package com.homerotl.ai.engines.rei;

import com.homerotl.ai.Connotation;
import com.homerotl.ai.engines.*;

import java.io.*;
import java.util.*;

/**
 * Takes care of semantics, meaning in context and conversations.
 * This is where the good stuff happens
 */
public class Semantic {

    private final static String DICTIONARY_DIRECTORY = "DICT";
    private final static String BRAIN_FILE_NAME = "BRAIN.TXT";

    private Lexic lex;
    private Syntax syn;
    private LinkedList<Sentence> conversation;
    private List<Synapse> brain;

    public Semantic(Lexic lex, Syntax syn) {
        conversation = new LinkedList<Sentence>();
        brain = new ArrayList<Synapse>();
        this.lex = lex;
        this.syn = syn;
    }

    public Sentence process(Sentence inputSentence) {

        conversation.add(inputSentence);

        if (inputSentence.isLexicalDefinition()) {
            Token noun = inputSentence.getTokens().get(3);
            try {
                TokenType tt = TokenType.valueOf(noun.toString());
                lex.addToken(inputSentence.getTokens().get(0).toString(),tt,lex.getSource());
                return new Sentence(Connotation.NEUTRAL, new Token(TokenType.ACK));
            } catch (IllegalArgumentException ex) {
                return new Sentence(Connotation.QUESTION, noun);
            }
            // It is another type of definition, but not lexical
            // let it continue
        }

        Sentence answer = null;

        if (inputSentence.containsUnknownTokens()) {

            Token unknownToken = inputSentence.getAnUnknownToken();
            answer = new Sentence(Connotation.QUESTION, unknownToken);

        } else {

            if (inputSentence.getConnotation().equals(Connotation.QUESTION)) {
                // Try to find answer
                // TODO: I do not know anything yet
                answer = new Sentence(Connotation.QUESTION);
            } else {


                List<Token> tokenChain = parseSimpleStatement(inputSentence);

                if (tokenChain!=null) {
                    addPath(tokenChain, this.brain);
                }

                answer = new Sentence(Connotation.NEUTRAL, new Token(TokenType.ACK));
            }
        }

        conversation.add(answer);

        return answer;

    }

    private List<Token> parseSimpleStatement(Sentence inputSentence) {
        ArrayList<Token> returnList = new ArrayList<Token>();
        boolean firstConcept = false;
        boolean relationship = false;
        boolean secondConcept = false;

        for (Token aToken :inputSentence.getTokens() ) {
            if (!firstConcept && aToken.getType()==TokenType.NOUN) {
                returnList.add(aToken);
                firstConcept = true;
            }
            if (firstConcept && !relationship && aToken.getType()==TokenType.VERB) {
                returnList.add(aToken);
                relationship = true;
            }
            if (firstConcept && relationship && !secondConcept &&
                    ( aToken.getType()==TokenType.ADJECTIVE || aToken.getType()==TokenType.NOUN )) {
                returnList.add(aToken);
                secondConcept = true;
            }
        }

        if (firstConcept && relationship && secondConcept) {
            return returnList;
        }

        System.out.println("We were not able to figure the meaning of this");

        return null;
    }

    private void addPath(List<Token> tokenChain, List<Synapse> brainLevel) {

        if (tokenChain.size()<1) {
            return;
        }

        Synapse foundSynapse = null;
        for (Synapse synapse : brainLevel) {
            if (synapse.getToken().equals(tokenChain.get(0))) {
                foundSynapse = synapse;
                System.out.println("Found it, we will make this connection stronger");
                synapse.stronger();
            }
        }
        if (foundSynapse==null) {
            foundSynapse = new Synapse(tokenChain.get(0));
            brainLevel.add(foundSynapse);
        }
        if (tokenChain.size()>0) {
            addPath(tokenChain.subList(1,tokenChain.size()), foundSynapse.getConnections());
        }
    }

    /**
     * Format
     * TT_ID:W,(TT_ID:W,(TT_ID:W,()))
     */
    public void loadIdeas() {
        if (!new File(brainFile()).exists()) {
            return;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(brainFile()));
            String line = null;
            while ((line = br.readLine()) != null) {
                readSynapseLine(line, brain);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            try { br.close(); } catch (IOException e) { }
        }
    }

    /**
     * Format
     * TT_ID:W(TT_ID:W(TT_ID:W()))
     */
    private void readSynapseLine(String line, List<Synapse> connectionSpace) {
        String token = line.substring(0,line.indexOf(":"));
        long strength = Long.parseLong(line.substring(line.indexOf(":") + 1, line.indexOf("(")));
        String connectionsStr = line.substring(line.indexOf("(")+1,line.lastIndexOf(")"));

        Synapse aNewSynapse = new Synapse(lex.getTokenByText(token),strength);

        StringTokenizer connectionsTokenizer = new StringTokenizer(connectionsStr,",");
        while (connectionsTokenizer.hasMoreTokens()) {
            connectionsTokenizer.nextToken();
        }

    }

    public void saveIdeas() {
        BufferedWriter bw = null;

        try {
            bw =  new BufferedWriter(new FileWriter(brainFile()));

            for (Synapse aSynapse : brain) {
                bw.write(aSynapse.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) { }
        }
    }

    private String brainFile() {
        return DICTIONARY_DIRECTORY + File.separator + BRAIN_FILE_NAME;
    }
}
