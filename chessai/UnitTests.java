package chessai;

import org.junit.Test;
import static org.junit.Assert.*;

import static chessai.Square.*;
import static chessai.Move.*;

/**
 * Unit tests.
 *
 * @author Richard Hu
 */
public class UnitTests {

    @Test
    public void squareEqualityTests() {
        assertTrue(sq("a1") == sq("a1"));
        assertFalse(sq("a1") == sq("a2"));
    }

    @Test
    public void squarePossibleTests() {
        /* Basic 8 diagonal directions one space away. */
        assertTrue(sq("d4").isPossibleMove(sq("d5")));
        assertTrue(sq("d4").isPossibleMove(sq("e5")));
        assertTrue(sq("d4").isPossibleMove(sq("e4")));
        assertTrue(sq("d4").isPossibleMove(sq("e3")));
        assertTrue(sq("d4").isPossibleMove(sq("d3")));
        assertTrue(sq("d4").isPossibleMove(sq("c3")));
        assertTrue(sq("d4").isPossibleMove(sq("c4")));
        assertTrue(sq("d4").isPossibleMove(sq("c5")));

        /* Knight moves. */
        assertTrue(sq("d4").isPossibleMove(sq("e6")));
        assertTrue(sq("d4").isPossibleMove(sq("f5")));
        assertTrue(sq("d4").isPossibleMove(sq("f3")));
        assertTrue(sq("d4").isPossibleMove(sq("e2")));
        assertTrue(sq("d4").isPossibleMove(sq("c2")));
        assertTrue(sq("d4").isPossibleMove(sq("b3")));
        assertTrue(sq("d4").isPossibleMove(sq("b5")));
        assertTrue(sq("d4").isPossibleMove(sq("c6")));

        /* Maximum distance directions. */
        assertTrue(sq("a1").isPossibleMove(sq("a8")));
        assertTrue(sq("a1").isPossibleMove(sq("h8")));
        assertTrue(sq("a1").isPossibleMove(sq("h1")));
        assertTrue(sq("h8").isPossibleMove(sq("a8")));
        assertTrue(sq("h8").isPossibleMove(sq("a1")));
        assertTrue(sq("h8").isPossibleMove(sq("h1")));
        assertTrue(sq("h1").isPossibleMove(sq("a8")));
        assertTrue(sq("a8").isPossibleMove(sq("h1")));

        /* Failure cases. */
        assertFalse(sq("a1").isPossibleMove(sq("c4")));
        assertFalse(sq("d3").isPossibleMove(sq("a2")));
    }

    @Test
    public void squareDirectionTests() {
        System.out.println(sq("a1").direction(sq("b2")));
    }

    @Test
    public void moveEqualityTests() {
        assertTrue(mv("a1-a2", 'N') == mv("a1-a2", 'N'));
        assertFalse(mv("a1-a2", 'N') == mv("a1-a2", '\0'));
        assertFalse(mv("a1-a3", 'N') == mv("a1-a2", 'N'));
        assertFalse(mv("a1-a3", 'N') == mv("a1-a2", 'K'));
    }

    @Test
    public void moveLegalityTests() {
        Board b = new Board();

        System.out.println(b);

        /*
         * White moves
         */

        /* All legal pawn moves. */
        assertTrue(b.isLegal(mv("a2-a3")));
        assertTrue(b.isLegal(mv("b2-b3")));
        assertTrue(b.isLegal(mv("c2-c3")));
        assertTrue(b.isLegal(mv("d2-d3")));
        assertTrue(b.isLegal(mv("e2-e3")));
        assertTrue(b.isLegal(mv("f2-f3")));
        assertTrue(b.isLegal(mv("g2-g3")));
        assertTrue(b.isLegal(mv("h2-h3")));

        assertTrue(b.isLegal(mv("a2-a4")));
        assertTrue(b.isLegal(mv("b2-b4")));
        assertTrue(b.isLegal(mv("c2-c4")));
        assertTrue(b.isLegal(mv("d2-d4")));
        assertTrue(b.isLegal(mv("e2-e4")));
        assertTrue(b.isLegal(mv("f2-f4")));
        assertTrue(b.isLegal(mv("g2-g4")));
        assertTrue(b.isLegal(mv("h2-h4")));

        /* All legal knight moves. */
        assertTrue(b.isLegal(mv("b1-a3")));
        assertTrue(b.isLegal(mv("b1-c3")));
        assertTrue(b.isLegal(mv("g1-f3")));
        assertTrue(b.isLegal(mv("g1-h3")));

        /* Illegal backwards pawn move. */
        assertFalse(b.isLegal(mv("d2-d1")));

        /* Illegal rook moves. */
        assertFalse(b.isLegal(mv("a1-a2")));
        assertFalse(b.isLegal(mv("a1-a3")));
        assertFalse(b.isLegal(mv("a1-b2")));
        assertFalse(b.isLegal(mv("a1-b3")));

        /* Illegal knight moves. */
        assertFalse(b.isLegal(mv("b1-a1")));
        assertFalse(b.isLegal(mv("b1-d2")));

        /* Illegal bishop moves. */
        assertFalse(b.isLegal(mv("c1-b2")));
        assertFalse(b.isLegal(mv("c1-a2")));

        /* Illegal queen moves. */
        assertFalse(b.isLegal(mv("d1-c2")));
        assertFalse(b.isLegal(mv("d1-a2")));
        assertFalse(b.isLegal(mv("d1-b2")));

        /* Illegal king moves. */
        assertFalse(b.isLegal(mv("e1-e2")));
        assertFalse(b.isLegal(mv("e1-a2")));

        /* Illegal castles. */
        assertFalse(b.isLegal(mv("e1-c1")));
        assertFalse(b.isLegal(mv("e1-g1")));

        /*
         * Black moves (illegal).
         */
        assertFalse(b.isLegal(mv("d7-d6")));
        assertFalse(b.isLegal(mv("d7-d5")));
        assertFalse(b.isLegal(mv("g8-f6")));

        assertTrue(b.isPossible(mv("d7-d6")));
        assertTrue(b.isPossible(mv("d7-d5")));
        assertTrue(b.isPossible(mv("g8-f6")));

        b.makeMove(mv("e2-e3"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        /*
         * White moves (illegal)
         */
        assertFalse(b.isLegal(mv("a2-a3")));
        assertFalse(b.isLegal(mv("a2-a4")));
        assertFalse(b.isLegal(mv("f1-d3")));

        /*
         * Black moves
         */

        /* Pawn moves. */
        assertTrue(b.isLegal(mv("d7-d6")));
        assertTrue(b.isLegal(mv("d7-d5")));

        /* Illegal castles. */
        assertFalse(b.isLegal(mv("e8-c8")));
        assertFalse(b.isLegal(mv("e8-g8")));

        b.makeMove(mv("d7-d5"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("g1-f3"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("b8-c6"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("f1-d3"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("c8-e6"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("e1-g1"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("d8-d7"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("b1-c3"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));

        b.makeMove(mv("e8-c8"), null);
        System.out.println(b);
        System.out.println(b.getPieces(Color.WHITE));
        System.out.println(b.getPieces(Color.BLACK));
    }
}
