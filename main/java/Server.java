import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private Engine engine;
    private ServerSocket socket;

    private InputStream in1, in2;
    private OutputStream out1, out2;

    private boolean player1ready, player2ready;


    private Thread thread;


    public Server() throws Exception {
        socket = new ServerSocket(8283);
        player1ready = false;
        player2ready = false;
        thread = Thread.currentThread();

        for (int i = 0; i < 2; i++) {
            Socket socket = this.socket.accept();
            if (i == 0) {
                in1 = socket.getInputStream();
                out1 = socket.getOutputStream();
                System.out.print("Player1 connected\n");
            } else {
                in2 = socket.getInputStream();
                out2 = socket.getOutputStream();
                System.out.println("Player2 connected\n");
            }
        }

        setupGame();
        while(!player1ready || !player2ready){}
        startGame();
    }

    public void setupGame() throws Exception {
        engine = new Engine();
        PlayerReader reader1 = new PlayerReader(in1, out1, engine, (byte) 1),
        reader2 = new PlayerReader(in2, out2, engine, (byte)2);
    }

    public void startGame() {
        while (!engine.isEnd) {
            int xCoor = -1, yCoor = -1;
            byte[] data = new byte[2];
            try {
                switch (engine.currentTurn) {
                    case 1: {
                        in1.read(data);
                        break;
                    }
                    case 2: {
                        in2.read(data);
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            xCoor = data[0];
            yCoor = data[1];
            if (!engine.shoot(xCoor, yCoor)) {
                engine.nextPlayer();
            }
        }
        String msg = "Player " + engine.currentTurn + " won";
        try {
            out2.write(msg.getBytes());
            out1.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class PlayerReader implements Runnable {
        InputStream in;
        OutputStream out;
        Engine e;
        int totalShips = 0;
        byte playerNum;

        PlayerReader(InputStream in, OutputStream out, Engine e, byte playerNum) {
            this.in = in;
            this.out = out;
            this.e = e;
            this.playerNum = playerNum;
            new Thread(this).start();
        }

        public void run() {
            try {
                //TODO 10 ships
                while (totalShips < 1) {
                    byte[] data = new byte[4];
                    if (in.read(data) != 0) {//read data about ship placement
                        System.out.println(data[0] + "" + data[1] + data[2] + data[3]);
                        e.setShip(playerNum, data[0], data[1], data[2], data[3]);
                        totalShips++;
                    }
                }
                if (playerNum == 1) {
                    player1ready = true;
                } else {
                    player2ready = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
