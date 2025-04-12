package org.mangorage.game.network.core;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.game.network.Connection;
import org.mangorage.game.network.packets.Packet;
import org.mangorage.game.network.packets.PacketId;
import org.mangorage.game.network.packets.clientbound.S2CCommitTurnPacket;
import org.mangorage.game.network.packets.clientbound.S2CGridUpdatePacket;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;
import org.mangorage.game.network.packets.serverbound.C2SLeavePacket;
import org.mangorage.game.network.packets.serverbound.C2SSelectGridPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class PacketHandler {
    public static final PacketHandler INSTANCE = new PacketHandler()
            .register(0, S2CGridUpdatePacket.ID, Direction.S2C, S2CGridUpdatePacket::decode)
            .register(1, S2CCommitTurnPacket.ID, Direction.S2C, S2CCommitTurnPacket::decode)

            .register(2, C2SJoinPacket.ID, Direction.C2S, C2SJoinPacket::decode)
            .register(3, C2SLeavePacket.ID, Direction.C2S, C2SLeavePacket::decode)
            .register(4, C2SSelectGridPacket.ID, Direction.C2S, C2SSelectGridPacket::decode);

    public static void init() {}

    private final Map<Integer, PacketId<?>> packetIdById = new HashMap<>();
    private final Map<PacketId<?>, RegisteredPacket> idByPacketId = new HashMap<>();

    private record RegisteredPacket(int id, Direction direction, Function<SimpleByteBuf, ? extends Packet> decoder) {}
    
    PacketHandler() {}
    
    <T extends Packet> PacketHandler register(final int id, PacketId<T> packetId, Direction direction, Function<SimpleByteBuf, T> decoder) {
        idByPacketId.put(packetId, new RegisteredPacket(id, direction, decoder));
        packetIdById.put(id, packetId);
        return this;
    }

    public SimpleByteBuf encodePacket(Packet packet) {
        var id = idByPacketId.get(packet.getId());
        if (id == null) throw new IllegalStateException("Packet %s is not registered...".formatted(packet.getClass()));

        var buf = SimpleByteBuf.of();
        buf.writeInt(id.id());
        packet.encode(buf);

        return buf;
    }
    
    public Optional<Packet> decodePacket(SimpleByteBuf smartByteBuf) {
        try {
            var intId = smartByteBuf.readInt();
            var packetId = packetIdById.get(intId);
            if (packetId == null)
                return Optional.empty();
            var registeredPacket = idByPacketId.get(packetId);
            if (registeredPacket == null)
                return Optional.empty();
            return Optional.of(registeredPacket.decoder().apply(smartByteBuf));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void handlePacket(Packet packet, Connection connection) {
        var id = idByPacketId.get(packet.getId());
        if (id == null) throw new IllegalStateException("Packet %s is not registered...".formatted(packet.getClass()));
        if (id.direction() != connection.getIncomingDirection()) throw new IllegalStateException("Attempted to handle packet on incorrect side, expected %s, got %s".formatted(connection.getIncomingDirection(), id.direction()));
        packet.handle(connection);
    }
}
