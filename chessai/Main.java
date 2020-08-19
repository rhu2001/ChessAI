package chessai;

import java.util.Arrays;
import java.util.Scanner;
import static chessai.Move.*;

public class Main {

    public static void main(String... args) {
        Board b = new Board();
        Scanner keyboard = new Scanner(System.in);
        String mv;

        while (true) {
            System.out.println(b);
            System.out.println("White pieces: " + b._whitePieces);
            System.out.println("Black pieces: " + b._blackPieces);
            System.out.println("White king, Black king: " + Arrays.toString(b._kingSquares));
            mv = keyboard.nextLine();
            while (!b.isLegal(mv(mv))) {
                mv = keyboard.nextLine();
            }
            b.makeMove(mv(mv), null);
        }
    }
}
