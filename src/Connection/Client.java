package Connection;

import GUI.Board;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

/**
 * class represents a Client for a TCP-IP connection
 */
public class Client extends Thread {

    /**
     * Board
     */
    private Board board;

    /**
     * Socket
     */
    private final Socket connection;

    /**
     * Server
     */
    private final Server server;

    /**
     * ObjectOutputStream for updating board
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * ObjectInputStream for updating board
     */
    private ObjectInputStream objectInputStream;

    /**
     * random number for generating the same random pieces
     */
    private Random random;

    /**
     * lineCount for counting the removed lines
     */
    private int lineCount;

    /**
     * constructor
     *
     * @param connection Socket
     * @param server     Server
     */
    public Client(Socket connection, Server server) {
        this.connection = connection;
        this.server = server;
        try {
            objectOutputStream = new ObjectOutputStream(this.connection.getOutputStream());
            objectInputStream = new ObjectInputStream(this.connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connection accepted with " + connection.getRemoteSocketAddress());
        while (true) {
            try {
                Board board = (Board) (objectInputStream.readObject());
                this.board = board;
                int currentLines = board.getRmLineCount();
                NetworkPackage networkPackage = new NetworkPackage(board, currentLines - lineCount);
                lineCount = currentLines;
                server.sendPackage(this, networkPackage);
                Thread.sleep(10);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                break;
            }
        }
        System.out.println("Connection closed with " + connection.getRemoteSocketAddress());
        server.closeConnections();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(board, client.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }

    /**
     * Sends the Package
     *
     * @param networkPackage Networkpackage from the server
     * @throws IOException IOException
     */
    public void sendPackage(NetworkPackage networkPackage) throws IOException {
        networkPackage.setRandom(random);
        objectOutputStream.writeObject(networkPackage);
        objectOutputStream.flush();
        objectOutputStream.reset();
    }

    /**
     * setter for random number for generating the same random pieces
     *
     * @param random random
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * closes the connection
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (IOException e) {
            System.out.println("Connection couldn't be closed..");
        }
    }
}
