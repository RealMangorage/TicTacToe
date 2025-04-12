package org.mangorage.network.internal;

import org.mangorage.buffer.api.SimpleByteBuf;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import org.mangorage.network.api.Packet;
import org.mangorage.network.api.PacketHandler;
import org.mangorage.network.api.PacketHandlerBuilder;
import org.mangorage.network.api.PacketId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class PacketHandlerImpl implements PacketHandlerBuilder, PacketHandler {
    private final Map<Integer, PacketId<?>> packetIdById = new HashMap<>();
    private final Map<PacketId<?>, RegisteredPacket> idByPacketId = new HashMap<>();

    private record RegisteredPacket(int id, Direction direction, Function<SimpleByteBuf, ? extends Packet> decoder) {}
    
    public PacketHandlerImpl() {}

    @Override
    public <T extends Packet> PacketHandlerBuilder register(final int id, final PacketId<T> packetId, final Direction direction, final Function<SimpleByteBuf, T> decoder) {
        idByPacketId.put(packetId, new RegisteredPacket(id, direction, decoder));
        packetIdById.put(id, packetId);
        return this;
    }

    @Override
    public PacketHandler build() {
        return this;
    }

    @Override
    public SimpleByteBuf encodePacket(final Packet packet) {
        var id = idByPacketId.get(packet.getId());
        if (id == null) throw new IllegalStateException("Packet %s is not registered...".formatted(packet.getClass()));

        var buf = SimpleByteBuf.of();
        buf.writeInt(id.id());
        packet.encode(buf);

        return buf;
    }

    @Override
    public Optional<Packet> decodePacket(final SimpleByteBuf smartByteBuf) {
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

    @Override
    public void handlePacket(final Packet packet, final Connection connection) {
        var id = idByPacketId.get(packet.getId());
        if (id == null) throw new IllegalStateException("Packet %s is not registered...".formatted(packet.getClass()));
        if (id.direction() != connection.getIncomingDirection()) throw new IllegalStateException("Attempted to handle packet on incorrect side, expected %s, got %s".formatted(connection.getIncomingDirection(), id.direction()));
        packet.handle(connection);
    }
}
