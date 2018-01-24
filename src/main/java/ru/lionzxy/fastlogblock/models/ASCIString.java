package ru.lionzxy.fastlogblock.models;

import java.nio.charset.Charset;

public class ASCIString implements CharSequence {
    private final byte[] shortString;
    private int hashcode = -1;
    private final static Charset asci = Charset.forName("ASCII");

    public ASCIString(final byte[] fatString) {
        shortString = fatString;

        initHash();
    }

    public ASCIString(final String fatString) {
        shortString = fatString.getBytes(asci);

        initHash();
    }

    @Override
    public int length() {
        return shortString.length;
    }

    @Override
    public char charAt(final int index) {
        return (char) shortString[index];
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        return new String(shortString, asci).subSequence(start, end);
    }

    @Override
    public int hashCode() {
        if (hashcode == -1) {
            initHash();
        }
        return hashcode;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof ASCIString)) {
            return false;
        }
        final byte[] alienBytes = ((ASCIString) obj).shortString;
        if (alienBytes.length != shortString.length) {
            return false;
        }
        for (int i = 0; i < alienBytes.length; i++) {
            if (alienBytes[i] != shortString[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return new String(shortString, asci);
    }

    public byte[] getShortString() {
        return shortString;
    }

    private void initHash() {
        int h = 0;
        for (int i = 0; i < length(); i++) {
            h = 31 * h + shortString[i];
        }
        hashcode = h;
    }
}
