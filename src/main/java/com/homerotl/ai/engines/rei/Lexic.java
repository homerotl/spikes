package com.homerotl.ai.engines.rei;

import com.homerotl.ai.engines.Source;
import com.homerotl.ai.engines.SourceType;
import com.homerotl.ai.engines.Token;
import com.homerotl.ai.engines.TokenType;

import java.io.*;
import java.util.*;

/**
 * Provides Lexic functions
 */
public class Lexic {

    private long maxSourceId;
    private long maxTokenId;

    private final static String DICTIONARY_DIRECTORY = "DICT";
    private final static String SOURCES_FILE_NAME = "SOURCES.TXT";

    private Map<String, Token> dictionary;
    private Map<Long, Source> sourcesById;
    private Map<String, Source> sourcesByName;
    private Source source;

    public Lexic() {
        dictionary = new HashMap<String, Token>();
        sourcesById = new HashMap<Long, Source>();
        sourcesByName = new HashMap<String, Source>();
    }

    public void readDictionaries() {
        if (!new File(DICTIONARY_DIRECTORY).exists()) {
            new File(DICTIONARY_DIRECTORY).mkdir();
        }

        readSources();

        for (TokenType tt : TokenType.values()) {
            if (tt.hasDictionary()) {
                if (dictionaryFileExists(tt)) {
                    readDictionary(tt);
                }
            }
        }
    }

    /**
     * File format:
     * ID,SOURCE_TYPE,NAME,TIMESTAMP
     */
    private void readSources() {

        if (!new File(sourcesFileName()).exists()) {
            return;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(sourcesFileName()));
            String line = null;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                long anId = Long.parseLong(tokenizer.nextToken());
                if (anId > maxSourceId) {
                    maxSourceId = anId;
                }
                SourceType type = SourceType.fromId(Long.parseLong(tokenizer.nextToken()));
                String name = tokenizer.nextToken();
                long timeStamp = Long.parseLong(tokenizer.nextToken());
                Source aSource = new Source(anId, type, name, timeStamp);
                sourcesById.put(anId,aSource);
                sourcesByName.put(name,aSource);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            try { br.close(); } catch (IOException e) { }
        }
    }

    public List<Token> parseInputString(String input, Source source) throws UnknownTokenException {
        this.source = source;
        ArrayList<Token> returnList = new ArrayList<Token>();

        StringTokenizer st = new StringTokenizer(input, " .");

        while (st.hasMoreTokens()) {
            Token aToken = getToken(st.nextToken().trim().toUpperCase(), source);
            returnList.add(aToken);
        }

        return returnList;
    }

    /**
     * Format:
     * TOKEN_ID,TOKEN,SOURCE_ID,TIMESTAMP
     */
    public void saveDictionaries() {
        if (!new File(DICTIONARY_DIRECTORY).exists()) {
            new File(DICTIONARY_DIRECTORY).mkdir();
        }

        saveSources();

        Map<TokenType,BufferedWriter> dictionaries = new HashMap<TokenType,BufferedWriter>();

        try {

            for (String key : dictionary.keySet()) {

                Token aToken = dictionary.get(key);

                if (!dictionaries.containsKey(aToken.getType())) {
                    if (dictionaryFileExists(aToken.getType())) {
                        new File(dictionaryFileName(aToken.getType())).delete();
                    }
                    dictionaries.put(aToken.getType(), new BufferedWriter(new FileWriter(dictionaryFileName(aToken.getType()))));
                }

                dictionaries.get(aToken.getType()).write(aToken.getId() + ",");
                dictionaries.get(aToken.getType()).write(aToken.toString() + ",");
                dictionaries.get(aToken.getType()).write(aToken.getSource().getId() + ",");
                dictionaries.get(aToken.getType()).write(""+aToken.getTimeStamp());
                dictionaries.get(aToken.getType()).newLine();

            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            try {
                for(TokenType tt :dictionaries.keySet() ) {
                    dictionaries.get(tt).flush();
                    dictionaries.get(tt).close();
                }
            } catch (IOException e) { }
        }
    }

    /**
     * Format:
     * ID,SOURCE_TYPE,NAME,TIMESTAMP
     */
    private void saveSources() {

        if (new File(sourcesFileName()).exists()) {
            new File(sourcesFileName()).delete();
        }

        BufferedWriter bw = null;

        try {
            bw =  new BufferedWriter(new FileWriter(sourcesFileName()));

            for (String name : sourcesByName.keySet()) {
                bw.write(sourcesByName.get(name).getId() + ",");
                bw.write(sourcesByName.get(name).getType().getId() + ",");
                bw.write(name + ",");
                bw.write(sourcesByName.get(name).getTimeStamp()+ "");
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

    public String prepareResponse(List<Token> tokens) {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<tokens.size();i++) {
            sb.append(tokens.get(i).toString());
            if (i+1<tokens.size()) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Format
     * TOKEN_ID,TOKEN,SOURCE_ID,TIMESTAMP
     */
    private void readDictionary(TokenType tt) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(dictionaryFileName(tt)));
            String line = null;
            while ((line = br.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                long id = Long.parseLong(tokenizer.nextToken());
                if (id > maxTokenId) {
                    maxTokenId = id;
                }
                String token = tokenizer.nextToken();
                long sourceId = Long.parseLong(tokenizer.nextToken());
                long timeStamp = Long.parseLong(tokenizer.nextToken());
                Token aToken = new Token(id, token,tt,sourcesById.get(sourceId),timeStamp);
                dictionary.put(aToken.toString(),aToken);
            }
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        } finally {
            try { br.close(); } catch (IOException e) { }
        }
    }

    private Token getToken(String tokenString, Source source) {
        if (dictionary.containsKey(tokenString)) {
            return dictionary.get(tokenString);
        } else {
            return new Token(tokenString, TokenType.UNKNOWN, source);
        }
    }

    private boolean dictionaryFileExists(TokenType tt) {
        return new File(dictionaryFileName(tt)).exists();
    }

    private String dictionaryFileName(TokenType tt) {
        return DICTIONARY_DIRECTORY + File.separator + tt.getFileName();
    }

    private String sourcesFileName() {
        return DICTIONARY_DIRECTORY + File.separator + SOURCES_FILE_NAME;
    }

    public boolean hasToken(String tokenString) {
        return dictionary.containsKey(tokenString);
    }

    public void addToken(String tokenText, TokenType type, Source source) {
        maxTokenId = maxTokenId + 1;
        Token aToken = new Token(maxTokenId, tokenText, type, source, System.currentTimeMillis());
        dictionary.put(aToken.toString(),aToken);
    }

    public boolean hasSourceByName(String name) {
        return sourcesByName.containsKey(name);
    }

    public void addSource(Source source) {
        maxSourceId = maxSourceId + 1;
        source.setId(maxSourceId);
        source.setTimeStamp(System.currentTimeMillis());
        sourcesById.put(source.getId(),source);
        sourcesByName.put(source.getName(),source);
    }

    public Source getSource() {
        return source;
    }

    public Source getSourceByName(String name) {
        return sourcesByName.get(name);
    }

    public Token getTokenByText(String text) {
        return this.dictionary.get(text);
    }
}
