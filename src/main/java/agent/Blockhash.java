/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author arwillis
 */
public class Blockhash implements Serializable {

    byte[] hash;

    Blockhash(byte[] _hash) {
        //hash = _hash;
        setHash(_hash);
    }

    // for jackson
    Blockhash() {
    }

//    public static byte[] serializeObject(Blockhash b) throws IOException {
//        return b.hash;
//    }
//    private void writeObject(java.io.ObjectOutputStream stream)
//            throws IOException {
//        stream.writeObject(hash);
//    }
//
//    private void readObject(java.io.ObjectInputStream stream)
//            throws IOException, ClassNotFoundException {
//        hash = (byte[]) stream.readObject();
//    }
    public final void setHash(byte[] b) {//throws IOException {
        hash = new byte[32];
        if (b != null) {
            for (int i = 0; i < b.length && i < 32; i++) {
                hash[i] = b[i];
            }
        } else {             // randomly initialize the hash
            hash = new byte[32];
            for (byte ob : hash) {
                ob = (byte) ((Math.random() * 255) - 128);
            }
        }
    }

    @JsonSerialize(using = agent.ByteArraySerializer.class)
    public byte[] getHash() {
        return hash;
    }

//    public String getHash() {
//        return hash.toString();
//    }
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Blockhash)) {
            return false;
        }
        Blockhash bh = (Blockhash) o;
        return this.rangedEquals(bh, 0, 0, -1);
    }

    public boolean rangedEquals(Blockhash bhash, int start1, int start2, int length) {
        if (bhash == null) {
            return false;
        }
        if (length < 0) {
            length = Math.min(hash.length, bhash.hash.length);
        }
        while (hash[start1++] == bhash.hash[start2++] && --length > 0) {
        }
        return length == 0;
    }

    @Override
    public String toString() {
        if (hash == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        for (final byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }

}
