package org.mangorage.buffer.api;

import java.util.Arrays;

import java.nio.charset.StandardCharsets;

public final class SimpleByteBuf {

    public static SimpleByteBuf of(int size) {
        return new SimpleByteBuf(size);
    }

    public static SimpleByteBuf of() {
        return new SimpleByteBuf();
    }

    public static SimpleByteBuf wrap(byte[] array) {
        return new SimpleByteBuf(array);
    }

    private byte[] buffer;
    private int readIndex = 0;
    private int writeIndex = 0;


    SimpleByteBuf(byte[] array) {
        this.buffer = array;
        this.writeIndex = array.length;
    }

    SimpleByteBuf(int initialCapacity) {
        buffer = new byte[initialCapacity];
    }

    SimpleByteBuf() {
        this(256); // Default initial capacity
    }

    public int readableBytes() {
        return writeIndex - readIndex;
    }

    public int writableBytes() {
        return buffer.length - writeIndex;
    }

    public void ensureWritable(int minWritableBytes) {
        if (writableBytes() < minWritableBytes) {
            int newCapacity = Math.max(buffer.length * 2, buffer.length + minWritableBytes);
            buffer = Arrays.copyOf(buffer, newCapacity);
        }
    }

    // Write methods
    public SimpleByteBuf writeByte(byte value) {
        ensureWritable(1);
        buffer[writeIndex++] = value;
        return this;
    }

    public SimpleByteBuf writeBytes(byte[] bytes) {
        ensureWritable(bytes.length);
        System.arraycopy(bytes, 0, buffer, writeIndex, bytes.length);
        writeIndex += bytes.length;
        return this;
    }

    public SimpleByteBuf writeInt(int value) {
        ensureWritable(4);
        buffer[writeIndex++] = (byte) (value >>> 24);
        buffer[writeIndex++] = (byte) (value >>> 16);
        buffer[writeIndex++] = (byte) (value >>> 8);
        buffer[writeIndex++] = (byte) value;
        return this;
    }

    public SimpleByteBuf writeLong(long value) {
        ensureWritable(8);
        buffer[writeIndex++] = (byte) (value >>> 56);
        buffer[writeIndex++] = (byte) (value >>> 48);
        buffer[writeIndex++] = (byte) (value >>> 40);
        buffer[writeIndex++] = (byte) (value >>> 32);
        buffer[writeIndex++] = (byte) (value >>> 24);
        buffer[writeIndex++] = (byte) (value >>> 16);
        buffer[writeIndex++] = (byte) (value >>> 8);
        buffer[writeIndex++] = (byte) value;
        return this;
    }

    // Write an int array
    public SimpleByteBuf writeIntArray(int[] array) {
        ensureWritable(4 + array.length * 4); // 4 bytes for the length, and 4 bytes per int
        writeInt(array.length); // Write length of array
        for (int value : array) {
            writeInt(value); // Write each int
        }
        return this;
    }

    // Write a String
    public SimpleByteBuf writeString(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        ensureWritable(4 + bytes.length); // 4 bytes for length and the byte array
        writeInt(bytes.length); // Write length of string
        writeBytes(bytes); // Write the string bytes
        return this;
    }

    // Read methods
    public byte readByte() {
        if (readableBytes() < 1) {
            throw new IndexOutOfBoundsException("No readable bytes left.");
        }
        return buffer[readIndex++];
    }

    public byte[] readBytes(int length) {
        if (readableBytes() < length) {
            throw new IndexOutOfBoundsException("Not enough bytes to read.");
        }
        byte[] result = new byte[length];
        System.arraycopy(buffer, readIndex, result, 0, length);
        readIndex += length;
        return result;
    }

    public int readInt() {
        if (readableBytes() < 4) {
            throw new IndexOutOfBoundsException("Not enough bytes to read an int.");
        }
        int value = (buffer[readIndex++] & 0xFF) << 24 |
                (buffer[readIndex++] & 0xFF) << 16 |
                (buffer[readIndex++] & 0xFF) << 8 |
                (buffer[readIndex++] & 0xFF);
        return value;
    }

    public long readLong() {
        if (readableBytes() < 8) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a long.");
        }
        long value = (long) (buffer[readIndex++] & 0xFF) << 56 |
                (long) (buffer[readIndex++] & 0xFF) << 48 |
                (long) (buffer[readIndex++] & 0xFF) << 40 |
                (long) (buffer[readIndex++] & 0xFF) << 32 |
                (long) (buffer[readIndex++] & 0xFF) << 24 |
                (long) (buffer[readIndex++] & 0xFF) << 16 |
                (long) (buffer[readIndex++] & 0xFF) << 8 |
                (long) (buffer[readIndex++] & 0xFF);
        return value;
    }

    // Read an int array
    public int[] readIntArray() {
        int length = readInt(); // Read the length of the array
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = readInt(); // Read each int
        }
        return result;
    }

    // Read a String
    public String readString() {
        int length = readInt(); // Read the length of the string
        byte[] bytes = readBytes(length); // Read the string bytes
        return new String(bytes, StandardCharsets.UTF_8); // Convert to string
    }

    public SimpleByteBuf writeCharArray(char[] array) {
        ensureWritable(4 + array.length * 2); // 4 bytes for length + 2 per char
        writeInt(array.length); // Write the array length
        for (char c : array) {
            writeChar(c);
        }
        return this;
    }

    public char[] readCharArray() {
        int length = readInt(); // Read length
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = readChar();
        }
        return result;
    }

    public char readChar() {
        if (readableBytes() < 2) {
            throw new IndexOutOfBoundsException("Not enough bytes to read a char.");
        }
        return (char) (((buffer[readIndex++] & 0xFF) << 8) |
                (buffer[readIndex++] & 0xFF));
    }

    public SimpleByteBuf writeChar(char value) {
        ensureWritable(2);
        buffer[writeIndex++] = (byte) (value >>> 8);
        buffer[writeIndex++] = (byte) value;
        return this;
    }

    // Reset the read index
    public void resetReaderIndex() {
        readIndex = 0;
    }

    // Get the underlying byte array
    public byte[] array() {
        return buffer;
    }

    // For debugging
    @Override
    public String toString() {
        return "SimpleByteBuf [readIndex=" + readIndex + ", writeIndex=" + writeIndex + ", buffer=" + Arrays.toString(buffer) + "]";
    }
}
