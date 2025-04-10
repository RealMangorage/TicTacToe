package org.mangorage.game.network.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class SmartByteBuf {
    public static SmartByteBuf create() {
        return new SmartByteBuf(Unpooled.buffer());
    }

    public static SmartByteBuf create(ByteBuf byteBuf) {
        return new SmartByteBuf(byteBuf);
    }

    private final ByteBuf buf;

    SmartByteBuf(ByteBuf buf) {
        this.buf = buf;
    }

    public ByteBuf raw() {
        return buf;
    }

    public int readableBytes() {
        return buf.readableBytes();
    }

    public void release() {
        buf.release();
    }

    // ===========================
    // === String ===============
    // ===========================

    public void write(String s) {
        write(s, StandardCharsets.UTF_8);
    }

    public void write(String s, Charset charset) {
        byte[] bytes = s.getBytes(charset);
        write(bytes.length);
        buf.writeBytes(bytes);
    }

    public String readString() {
        return readString(StandardCharsets.UTF_8);
    }

    public String readString(Charset charset) {
        int length = readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes, charset);
    }

    // ===========================
    // === Primitives ============
    // ===========================

    public void write(int value)       { buf.writeInt(value); }
    public void write(long value)      { buf.writeLong(value); }
    public void write(short value)     { buf.writeShort(value); }
    public void write(byte value)      { buf.writeByte(value); }
    public void write(float value)     { buf.writeFloat(value); }
    public void write(double value)    { buf.writeDouble(value); }
    public void write(boolean value)   { buf.writeBoolean(value); }

    public int readInt()       { return buf.readInt(); }
    public long readLong()     { return buf.readLong(); }
    public short readShort()   { return buf.readShort(); }
    public byte readByte()     { return buf.readByte(); }
    public float readFloat()   { return buf.readFloat(); }
    public double readDouble() { return buf.readDouble(); }
    public boolean readBoolean() { return buf.readBoolean(); }

    // ===========================
    // === Byte[] ================
    // ===========================

    public void write(byte[] bytes) {
        write(bytes.length);
        buf.writeBytes(bytes);
    }

    public byte[] readBytes() {
        int length = readInt();
        byte[] out = new byte[length];
        buf.readBytes(out);
        return out;
    }

    // ===========================
    // === UUID ==================
    // ===========================

    public void write(UUID uuid) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID() {
        long most = buf.readLong();
        long least = buf.readLong();
        return new UUID(most, least);
    }

    // ===========================
    // === int[] ================
    // ===========================

    public void writeIntArray(int[] array) {
        write(array.length); // Write the length first
        for (int value : array) {
            write(value); // Write each int
        }
    }

    public int[] readIntArray() {
        int length = readInt(); // Read the length first
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            array[i] = readInt(); // Read each int
        }
        return array;
    }
}
