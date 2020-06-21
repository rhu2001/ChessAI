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
        /*
         * Basic 8 diagonal directions one space away.
         */
        assertTrue(sq("d4").isPossibleMove(sq("d5")));
        assertTrue(sq("d4").isPossibleMove(sq("e5")));
        assertTrue(sq("d4").isPossibleMove(sq("e4")));
        assertTrue(sq("d4").isPossibleMove(sq("e3")));
        assertTrue(sq("d4").isPossibleMove(sq("d3")));
        assertTrue(sq("d4").isPossibleMove(sq("c3")));
        assertTrue(sq("d4").isPossibleMove(sq("c4")));
        assertTrue(sq("d4").isPossibleMove(sq("c5")));

        /*
         * Knight moves.
         */
        assertTrue(sq("d4").isPossibleMove(sq("e6")));
        assertTrue(sq("d4").isPossibleMove(sq("f5")));
        assertTrue(sq("d4").isPossibleMove(sq("f3")));
        assertTrue(sq("d4").isPossibleMove(sq("e2")));
        assertTrue(sq("d4").isPossibleMove(sq("c2")));
        assertTrue(sq("d4").isPossibleMove(sq("b3")));
        assertTrue(sq("d4").isPossibleMove(sq("b5")));
        assertTrue(sq("d4").isPossibleMove(sq("c6")));

        /*
         * Maximum distance directions.
         */
        assertTrue(sq("a1").isPossibleMove(sq("a8")));
        assertTrue(sq("a1").isPossibleMove(sq("h8")));
        assertTrue(sq("a1").isPossibleMove(sq("h1")));
        assertTrue(sq("h8").isPossibleMove(sq("a8")));
        assertTrue(sq("h8").isPossibleMove(sq("a1")));
        assertTrue(sq("h8").isPossibleMove(sq("h1")));
        assertTrue(sq("h1").isPossibleMove(sq("a8")));
        assertTrue(sq("a8").isPossibleMove(sq("h1")));

        /*
         * Failure cases.
         */
        assertFalse(sq("a1").isPossibleMove(sq("c4")));
        assertFalse(sq("d3").isPossibleMove(sq("a2")));
    }

    @Test
    public void squareDirectionTests() {

    }

    @Test
    public void moveEqualityTests() {
        assertTrue(mv("a1-a2", 'N') == mv("a1-a2", 'N'));
        assertFalse(mv("a1-a2", 'N') == mv("a1-a2", '\0'));
        assertFalse(mv("a1-a3", 'N') == mv("a1-a2", 'N'));
        assertFalse(mv("a1-a3", 'N') == mv("a1-a2", 'K'));
    }
}
