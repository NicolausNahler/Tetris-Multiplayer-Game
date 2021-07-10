package Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Random;

/**
 * class represents a Server for a TCP-IP connection
 */
public class Server extends Thread {

    /**
     * Port for Socket
     */
    public static final int PORT = 10023;

    /**
     * Set of clients
     */
    public static HashSet<Client> clients = new HashSet<>();

    /**
     * serverSocket for the connection
     */
    private ServerSocket serverSocket;

    @Override
    public void run() {
        Random random = new Random(new Random().nextLong());
        try (ServerSocket server = new ServerSocket(PORT)) {
            this.serverSocket = server;
            System.out.println("Server ready at port " + PORT);
            while (clients.size() < 2) {
                Socket connection = server.accept();
                Client client = new Client(connection, this);
                client.start();
                clients.add(client);
                client.setRandom(random);
            }
            System.out.println("Both Players connected");
        } catch (Exception e) {
            System.out.println("Server no longer running.");
        }
    }

    /**
     * sends the package to client
     *
     * @param client         client
     * @param networkPackage networkPackage
     */
    public void sendPackage(Client client, NetworkPackage networkPackage) {
        clients.forEach(n -> {
            try {
                if (n != client) {
                    n.sendPackage(networkPackage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * closes the connections
     */
    public void closeConnections() {
        clients.forEach(Client::closeConnection);
        clients.clear();
    }

    /**
     * closes the serverSocket
     *
     * @throws IOException IOException
     */
    public void close() throws IOException {
        serverSocket.close();
    }
}

