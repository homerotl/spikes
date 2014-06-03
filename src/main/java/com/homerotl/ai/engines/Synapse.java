package com.homerotl.ai.engines;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a link to another concept
 */
public final class Synapse {

    private long strength;
    private Token token;

    public List<Synapse> getConnections() {
        return connections;
    }

    private List<Synapse> connections;

    public Synapse(Token token) {
        this.token = token;
        connections = new ArrayList<Synapse>();
    }

    public Synapse(Token token, long strength) {
        this.token = token;
        this.strength = strength;
        connections = new ArrayList<Synapse>();
    }

    public long getStrength() {
        return strength;
    }

    public void setStrength(long strength) {
        this.strength = strength;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public void stronger() {
        strength++;
    }

    public void weaker() {
        strength--;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.token.getId());
        sb.append(":");
        sb.append(this.getStrength());
        sb.append("(");
        for (int i=0;i<connections.size();i++) {
            sb.append(connections.get(i).toString());
            if (i+1<connections.size()) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
