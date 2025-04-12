package org.mangorage.game.network.packets.serverbound;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.Game;
import org.mangorage.game.network.Network;
import org.mangorage.game.players.RemotePlayer;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketId;

public final class C2SJoinPacket implements Packet {
    public static final PacketId<C2SJoinPacket> ID = PacketId.create();

    public static C2SJoinPacket decode(SimpleByteBuf buf) {
        return new C2SJoinPacket(buf.readString(), buf.readCharArray());
    }

    private final String username;
    private final char[] password;

    public C2SJoinPacket(final String username, final char[] password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void encode(SimpleByteBuf buffer) {
        buffer.writeString(username);
        buffer.writeCharArray(password);
    }

    @Override
    public void handle(Connection connection) {
        System.out.println("Player Joined!");
        Game.getHostServerFrame().playerJoined(new RemotePlayer(username));
    }

    @Override
    public PacketId<?> getId() {
        return ID;
    }
}
