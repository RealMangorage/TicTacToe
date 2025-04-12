package org.mangorage.game.network;
import org.mangorage.game.network.packets.serverbound.C2SJoinPacket;
import org.mangorage.network.api.Connection;
import org.mangorage.network.api.Direction;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void initNewClient(final String host, final int port, final String username, final char[] password) {
        new Thread(() -> {
            initClient(host, port, username, password);
        }).start();
    }

    private static void initClient(final String host, final int port, String username, char[] password) {
        try (final Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(1000000);
            Connection connection = Connection.ofThreaded(socket, Network.INSTANCE, Direction.SERVER);

            Thread.sleep(1000);
            connection.send(new C2SJoinPacket(username, password));
            Network.setPlayerConnection(connection);
            new Scanner(System.in).nextLine(); // keep the thread alive, because why not
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}