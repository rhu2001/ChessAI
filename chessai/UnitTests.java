package chessai;

import org.junit.Test;
import static org.junit.Assert.*;

import static chessai.Square.*;
import static chessai.Move.*;
import static chessai.Color.*;

/**
 * Unit tests.
 *
 * @author Richard Hu
 */
public class UnitTests {

    @Test
    public void squareEqualityTests() {
        assertSame(sq("a1"), sq("a1"));
        assertNotSame(sq("a1"), sq("a2"));
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
    public void configurationTests() {
        Board b = new Board();
        System.out.println(b);

        b.initialize(new String[][] {
                {"wp"},
                {null, "wp"},
                {null, null, "wp"},
                {null, null, null, "wp"},
                {null, null, null, null, "wp"},
                {null, null, null, null, null, "wp"},
                {null, null, null, null, null, null, "wp"},
                {null, null, null, null, null, null, null, "wp"}
        }, WHITE);
        assertEquals(b.get(sq(0, 0)).abbr(), '\0');
        assertEquals(b.get(sq(1, 1)).abbr(), '\0');
        assertEquals(b.get(sq(2, 2)).abbr(), '\0');
        assertEquals(b.get(sq(3, 3)).abbr(), '\0');
        assertEquals(b.get(sq(4, 4)).abbr(), '\0');
        assertEquals(b.get(sq(5, 5)).abbr(), '\0');
        assertEquals(b.get(sq(6, 6)).abbr(), '\0');
        assertEquals(b.get(sq(7, 7)).abbr(), '\0');

        assertNull(b.get(sq(0, 1)));

        b = new Board(null, WHITE);
        assertNull(b.get(sq("a1")));
        assertNull(b.get(sq("b2")));
    }

    @Test
    public void moveEqualityTests() {
        assertSame(mv("a1-a2", 'N'), mv("a1-a2", 'N'));
        assertNotSame(mv("a1-a2", 'N'), mv("a1-a2", '\0'));
        assertNotSame(mv("a1-a3", 'N'), mv("a1-a2", 'N'));
        assertNotSame(mv("a1-a3", 'N'), mv("a1-a2", 'K'));
    }

    @Test
    public void moveLegalityTests() {
        Board b = new Board();

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

        b.makeMove(mv("e2-e3"));
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
    }

    @Test
    public void undoTests() {
        Board b = new Board();

        /*
         * Tests basic undoing.
         */

        b.makeMove(mv("e2-e4"));
        b.makeMove(mv("e7-e5"));

        System.out.println(b);

        b.undo();
        b.undo();

        System.out.println(b);
        System.out.println(b.getPieces(WHITE));
        System.out.println(b.getPieces(BLACK));

        /*
         * Tests undoing with captures.
         */

        b.initialize(new String[][] {
                {"wq", "bp"},
                {"bq", "wp"},
                {},
                {},
                {},
                {},
                {},
                {}
        }, WHITE);

        assertEquals(b.get(sq(0, 0)).abbr(), 'Q');
        assertEquals(b.get(sq(0, 0)).getColor(), WHITE);

        assertEquals(b.get(sq(1, 0)).abbr(), '\0');
        assertEquals(b.get(sq(1, 0)).getColor(), BLACK);

        assertEquals(b.get(sq(0, 1)).abbr(), 'Q');
        assertEquals(b.get(sq(0, 1)).getColor(), BLACK);

        assertEquals(b.get(sq(1, 1)).abbr(), '\0');
        assertEquals(b.get(sq(1, 1)).getColor(), WHITE);

        b.makeMove(mv(sq(0, 0), sq(1, 0)));
        b.makeMove(mv(sq(0, 1), sq(1, 1)));
        b.makeMove(mv(sq(1, 0), sq(1, 1)));

        assertNull(b.get(sq(0, 0)));
        assertNull(b.get(sq(1, 0)));
        assertNull(b.get(sq(0, 1)));

        assertEquals(b.get(sq(1, 1)).abbr(), 'Q');
        assertEquals(b.get(sq(1, 1)).getColor(), WHITE);

        b.undo();
        b.undo();
        b.undo();

        assertEquals(b.get(sq(0, 0)).abbr(), 'Q');
        assertEquals(b.get(sq(0, 0)).getColor(), WHITE);

        assertEquals(b.get(sq(1, 0)).abbr(), '\0');
        assertEquals(b.get(sq(1, 0)).getColor(), BLACK);

        assertEquals(b.get(sq(0, 1)).abbr(), 'Q');
        assertEquals(b.get(sq(0, 1)).getColor(), BLACK);

        assertEquals(b.get(sq(1, 1)).abbr(), '\0');
        assertEquals(b.get(sq(1, 1)).getColor(), WHITE);

        /*
         * Tests undoing with promotions and captures.
         */

        b.initialize(new String[][] {
                {null, "wn"},
                {"bp", "wq"},
                {},
                {},
                {},
                {},
                {null, "wp"},
                {"br", null}
        }, BLACK);

        assertEquals(b.get(sq(1, 0)).abbr(), 'N');
        assertEquals(b.get(sq(1, 0)).getColor(), WHITE);

        assertEquals(b.get(sq(0, 1)).abbr(), '\0');
        assertEquals(b.get(sq(0, 1)).getColor(), BLACK);

        assertEquals(b.get(sq(1, 1)).abbr(), 'Q');
        assertEquals(b.get(sq(1, 1)).getColor(), WHITE);

        assertEquals(b.get(sq(1, 6)).abbr(), '\0');
        assertEquals(b.get(sq(1, 6)).getColor(), WHITE);

        assertEquals(b.get(sq(0, 7)).abbr(), 'R');
        assertEquals(b.get(sq(0, 7)).getColor(), BLACK);

        assertNull(b.get(sq(1, 7)));

        System.out.println(b);
        System.out.println(b.getPieces(WHITE));
        System.out.println(b.getPieces(BLACK));
        b.makeMove(mv(sq(0, 1), sq(1, 0)), 'q');

        assertEquals(b.get(sq(1, 0)).abbr(), 'Q');
        assertEquals(b.get(sq(1, 0)).getColor(), BLACK);

        System.out.println(b);
        System.out.println(b.getKingSquare(WHITE));
        System.out.println(b.getKingSquare(BLACK));

        b.makeMove(mv("b7-b8"), 'R');
        b.makeMove(mv("a8-b8"));
        b.makeMove(mv("b2-a2"));

        b.undo();
        b.undo();
        b.undo();
        b.undo();

        System.out.println(b);
        System.out.println(b.getPieces(WHITE));
        System.out.println(b.getPieces(BLACK));

        assertEquals(b.get(sq(1, 0)).abbr(), 'N');
        assertEquals(b.get(sq(1, 0)).getColor(), WHITE);

        assertEquals(b.get(sq(0, 1)).abbr(), '\0');
        assertEquals(b.get(sq(0, 1)).getColor(), BLACK);

        assertEquals(b.get(sq(1, 1)).abbr(), 'Q');
        assertEquals(b.get(sq(1, 1)).getColor(), WHITE);

        assertEquals(b.get(sq(1, 6)).abbr(), '\0');
        assertEquals(b.get(sq(1, 6)).getColor(), WHITE);

        assertEquals(b.get(sq(0, 7)).abbr(), 'R');
        assertEquals(b.get(sq(0, 7)).getColor(), BLACK);

        assertNull(b.get(sq(1, 7)));
    }

    @Test
    public void checkTests() {

        /*
         * Default starting board. Neither king
         * should be in check.
         */
        Board b = new Board();

        assertFalse(b.inCheck(WHITE));
        assertFalse(b.inCheck(BLACK));

        /*
         * Black Rook puts white King in check and
         * white Pawn puts black King in check.
         */
        b.initialize(new String[][] {
                {"wk", "br"},
                {},
                {},
                {},
                {},
                {},
                {null, "wp"},
                {"bk"}
        }, WHITE);

        assertTrue(b.inCheck(WHITE));
        assertTrue(b.inCheck(BLACK));

        /*
         * Black Knight puts white King in check.
         * Black King not in check.
         */
        b.initialize(new String[][] {
                {null, "wk"},
                {},
                {"bn"},
                {},
                {},
                {},
                {},
                {"bk"}
        }, WHITE);

        assertTrue(b.inCheck(WHITE));
        assertFalse(b.inCheck(BLACK));

        /*
         * White Bishop puts black King in check.
         * White King not in check.
         */
        b.initialize(new String[][] {
                {null, "wk"},
                {"wb"},
                {},
                {},
                {},
                {},
                {},
                {null, null, null, null, null, null, "bk"}
        }, WHITE);

        assertFalse(b.inCheck(WHITE));
        assertTrue(b.inCheck(BLACK));
    }

    @Test
    public void possibleMovesTests() {
        Board b = new Board();
        assertFalse(b.possibleMovesUpdated(WHITE));
        assertFalse(b.possibleMovesUpdated(BLACK));

        /*
         * All possible white Pawn moves.
         */
        assertTrue(b.possibleMoves(WHITE).contains(mv("a2-a3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("a2-a4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("b2-b3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("b2-b4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("c2-c3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("c2-c4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("d2-d3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("d2-d4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("e2-e3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("e2-e4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("f2-f3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("f2-f4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("g2-g3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("g2-g4")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("h2-h3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("h2-h4")));

        /*
         * All possible white Knight moves.
         */
        assertTrue(b.possibleMoves(WHITE).contains(mv("b1-a3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("b1-c3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("g1-f3")));
        assertTrue(b.possibleMoves(WHITE).contains(mv("g1-h3")));

        /*
         * All possible black Pawn moves.
         */
        assertTrue(b.possibleMoves(BLACK).contains(mv("a7-a6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("a7-a5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("b7-b6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("b7-b5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("c7-c6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("c7-c5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("d7-d6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("d7-d5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("e7-e6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("e7-e5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("f7-f6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("f7-f5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("g7-g6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("g7-g5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("h7-h6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("h7-h5")));

        /*
         * All possible black Knight moves.
         */
        assertTrue(b.possibleMoves(BLACK).contains(mv("b8-a6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("b8-c6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("g8-f6")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("g8-h6")));

        assertTrue(b.possibleMovesUpdated(WHITE));
        assertTrue(b.possibleMovesUpdated(BLACK));

        assertEquals(20, b.possibleMoves(WHITE).size());
        assertEquals(20, b.possibleMoves(BLACK).size());

        b.makeMove(mv("d2-d4"));
        assertFalse(b.possibleMovesUpdated(WHITE));
        assertFalse(b.possibleMovesUpdated(BLACK));

        assertTrue(b.possibleMoves(WHITE).contains(mv("d4-d5")));
        assertTrue(b.possibleMoves(BLACK).contains(mv("a7-a6")));

        b.undo();
        assertFalse(b.possibleMovesUpdated(WHITE));
        assertFalse(b.possibleMovesUpdated(BLACK));

        System.out.println("STupid bugyy test");

        b.initialize(new String[][] {
                {"bk"},
                {null, null, null, "wn"},
                {},
                {null, "wn"},
                {},
                {},
                {},
                {null, null, null, null, null, null, null, "wb"}
        }, BLACK);
        assertFalse(b.possibleMovesUpdated(WHITE));
        assertFalse(b.possibleMovesUpdated(BLACK));
        assertTrue(b.possibleMoves(BLACK).isEmpty());
    }

    @Test
    public void checkMateTests() {
        Board b = new Board();

        assertFalse(b.checkmate());
        b.makeMove(mv("d2-d4"));
        assertFalse(b.checkmate());

        b.initialize(new String[][] {
                {"wk", null, null, "br"},
                {null, null, "br"},
                {},
                {},
                {},
                {},
                {},
                {}
        }, WHITE);
        assertTrue(b.checkmate());

        b.set(sq(3, 7), new Queen(WHITE, sq(3, 7)));
        System.out.println(b);
        assertFalse(b.checkmate());

        b.initialize(new String[][] {
                {"bk"},
                {null, "wq"},
                {"wb"},
                {},
                {},
                {},
                {},
                {}
        }, BLACK);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {"wk"},
                {null, "bq"},
                {"bp"},
                {},
                {},
                {},
                {},
                {}
        }, WHITE);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {},
                {},
                {},
                {},
                {},
                {null, null, null, null, "wk"},
                {null, null, null, "wp", "wp"},
                {null, null, null, null, "bk"},
        }, BLACK);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {null, "wk", null, null, null, null, null, "br"},
                {"wp", "wp", "wp"},
                {},
                {},
                {},
                {},
                {},
                {},
        }, WHITE);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {null, null, null, null, null, "br"},
                {null, null, null, null, "wn", "bp", "bp", "bk"},
                {},
                {},
                {},
                {null, null, null, null, null, null, null, "wr"},
                {},
                {},
        }, BLACK);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {},
                {"bb", "bb"},
                {},
                {},
                {},
                {},
                {null, null, null, null, null, null, null, "wp"},
                {null, null, null, null, null, null, null, "wk"},
        }, WHITE);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {null, null, null, null, null, "br", "bk"},
                {null, null, null, null, null, "bp", "wq", "bp"},
                {null, null, null, null, null, null, null, "wb"},
                {},
                {},
                {},
                {},
                {},
        }, BLACK);
        assertTrue(b.checkmate());

        b.initialize(new String[][] {
                {},
                {},
                {},
                {},
                {},
                {null, "bk"},
                {},
                {"wk", null, null, null, null, "bq"},
        }, WHITE);
        assertTrue(b.checkmate());
    }
}
