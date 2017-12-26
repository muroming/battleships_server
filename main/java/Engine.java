import java.util.Random;

public class Engine {

    private byte[][] player1, player2;
    /*
    1 - ship
    0 - empty
    2 -  hit ship
     */

    private byte shipsPlayer1, shipsPlayer2;
    byte currentTurn; //player id
    boolean isEnd;

    public Engine() {
        player1 = new byte[10][10];
        player2 = new byte[10][10];
        shipsPlayer1 = 1;//TODO set to 10
        shipsPlayer2 = 1;
        for (int i = 0; i < 10; i++) {//init players
            for (int j = 0; j < 10; j++) {
                player1[i][j] = 0;
                player2[i][j] = 0;
            }
        }
        currentTurn = 1;
        //TODO currentTurn = (byte) (new Random().nextInt() % 2);
    }

    public boolean shoot(int xCoor, int yCoor) {
        boolean hit = false;
        switch (currentTurn) {
            case 1: {
                hit = player2[yCoor][xCoor] == 1;
                if (hit) {
                    player2[yCoor][xCoor] = 2;
                    if (isShipDead(player2, xCoor, yCoor)) {
                        shipsPlayer2--;
                    }
                }
                break;
            }
            case 2: {
                hit = player1[yCoor][xCoor] == 1;
                if (hit) {
                    player1[yCoor][xCoor] = 2;
                    if (isShipDead(player1, xCoor, yCoor)) {
                        shipsPlayer1--;
                    }
                }
                break;
            }
        }
        isEnd = shipsPlayer1 == 0 || shipsPlayer2 == 0;
        //TODO remove field print
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(player1[i][j] + " ");
            }
            System.out.print("        ");
            for (int j = 0; j < 10; j++) {
                System.out.print(player2[i][j] + " ");
            }
            System.out.println();
        }
        return hit;
    }

    boolean isShipDead(byte[][] player, int xCoor, int yCoor) {
        if (xCoor != 10) {
            if (player[yCoor][xCoor + 1] == 1) {
                return false;
            }
            if (yCoor != 0) {
                if (player[yCoor - 1][xCoor + 1] == 1) {
                    return false;
                }
            }
            if (yCoor != 10) {
                if (player[yCoor + 1][xCoor + 1] == 1) {
                    return false;
                }
            }
        }
        if (xCoor != 0) {
            if (player[yCoor][xCoor - 1] == 1) {
                return false;
            }
            if (yCoor != 0) {
                if (player[yCoor - 1][xCoor - 1] == 1) {
                    return false;
                }
            }
            if (yCoor != 10) {
                if (player[yCoor + 1][xCoor - 1] == 1) {
                    return false;
                }
            }
        }
        if (yCoor != 0) {
            if (player[yCoor - 1][xCoor] == 1) {
                return false;
            }
        }
        if (yCoor != 10) {
            if (player[yCoor + 1][xCoor] == 1) {
                return false;
            }
        }

        return true;
    }

    public void nextPlayer() {
        if (currentTurn == 1) {
            currentTurn = 2;
        } else {
            currentTurn = 1;
        }
    }

    public void setShip(int player, int shipLength, int xCoor, int yCoor, int rotation) {
        switch (player) {
            case 1: {
                switch (rotation) {
                    case 0: { //horizontal
                        for (int i = 0; i < shipLength; i++) {
                            player1[yCoor][xCoor + i] = 1;
                        }
                        break;
                    }
                    case 1: {//vertical
                        for (int i = 0; i < shipLength; i++) {
                            player1[yCoor + i][xCoor] = 1;
                        }
                        break;
                    }
                }
                break;
            }
            case 2: {
                switch (rotation) {
                    case 0: { //horizontal
                        for (int i = 0; i < shipLength; i++) {
                            player2[yCoor][xCoor + i] = 1;
                        }
                        break;
                    }
                    case 1: {//vertical
                        for (int i = 0; i < shipLength; i++) {
                            player2[yCoor + i][xCoor] = 1;
                        }
                        break;
                    }
                }
                break;
            }
        }
        //TODO delete this
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(player1[i][j] + " ");
            }
            System.out.print("        ");
            for (int j = 0; j < 10; j++) {
                System.out.print(player2[i][j] + " ");
            }
            System.out.println();
        }
    }
}
