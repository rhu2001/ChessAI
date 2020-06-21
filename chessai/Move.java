package chessai;

import static chessai.Square.*;

/**
 * A possible move that can be made in chess.
 * Like Square objects, Moves are immutable
 * and unique and are generated and retrieved
 * by static factory methods.
 *
 * @author Richard Hu
 */
public class Move {

    /**
     * Returns the move starting and ending at the specified squares
     * with the given captured piece.
     *
     * @param from Starting square.
     * @param to Destination square.
     * @param captured Captured piece.
     * @return Move with specified parameters.
     */
    static Move mv(Square from, Square to, char captured) {
        if (from != null && to != null) {
            return findMove(captured, from, to);
        } else {
            return null;
        }
    }

    /**
     * Returns the move starting and ending at the specified squares
     * with the given captured piece.
     *
     * @param s Starting and ending squares in String format.
     * @param captured Captured piece.
     * @return Move with specified parameters.
     */
    static Move mv(String s, char captured) {
        s = s.trim();
        if (s.matches("[a-h][1-8]-[a-h][1-8]\\b.*")) {
            return mv(sq(s.substring(0, 2)), sq(s.substring(3, 5)),
                    captured);
        } else {
            return null;
        }
    }

    /**
     * Returns the move starting and ending at the specified squares.
     *
     * @param from Starting square.
     * @param to Ending square.
     * @return Move with specified parameters.
     */
    static Move mv(Square from, Square to) {
        return mv(from, to, '\0');
    }

    /**
     * Returns the move starting and ending at the specified squares.
     *
     * @param s Starting and ending squares in String format.
     * @return Move with specified parameters.
     */
    static Move mv(String s) {
        return mv(s, '\0');
    }

    /**
     * Finds a move given the captured piece and the
     * starting and destination squares.
     *
     * @param c Captured piece.
     * @param from Start square.
     * @param to Destination square.
     * @return Move corresponding to parameters.
     */
    private static Move findMove(char c, Square from, Square to) {
        return switch (c) {
            case '\0' -> _moves[from.index()][to.index()][0];
            case 'K' -> _moves[from.index()][to.index()][1];
            case 'Q' -> _moves[from.index()][to.index()][2];
            case 'R' -> _moves[from.index()][to.index()][3];
            case 'B' -> _moves[from.index()][to.index()][4];
            case 'N' -> _moves[from.index()][to.index()][5];
            case 'P' -> _moves[from.index()][to.index()][6];
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    /**
     * Constructs a move with the given starting and
     * destination squares and the type of piece being
     * captured.
     *
     * @param from Starting square.
     * @param to Destination square.
     * @param captured Piece being captured according to
     * its abbreviation. Null character if not a capture.
     * @param isCastle TRUE iff this is a castle move.
     */
    private Move(Square from, Square to, char captured, boolean isCastle) {
        _from = from;
        _to = to;
        _captured = captured;
        _isCastle = isCastle;

        _possiblePromotion = from.col() == to.col()
                && ((from.row() == 6 && to.row() == 7)
                || (from.row() == 1 && to.row() == 0));
    }

    /**
     * Gets starting square.
     *
     * @return _from.
     */
    Square getFrom() {
        return _from;
    }

    /**
     * Gets destination square.
     *
     * @return _to.
     */
    Square getTo() {
        return _to;
    }

    /**
     * Distance between _from and _to.
     *
     * @return Distance of this move.
     */
    int distance() {
        return _from.distance(_to);
    }

    /**
     * Direction of this move.
     *
     * @return Direction from _from to _to.
     */
    int direction() {
        return _from.direction(_to);
    }

    /**
     * Returns true iff this move is a capture.
     *
     * @return Whether this move is a capture.
     */
    boolean isCapture() {
        return _captured != '\0';
    }

    /**
     * Returns the piece that this move captures.
     *
     * @return _captured.
     */
    char getCaptured() {
        return _captured;
    }

    /**
     * Returns the equivalent of the current move but
     * with the specified captured piece.
     *
     * @param c Captured piece.
     * @return The capture move.
     */
    Move capture(char c) {
        if (_captured != '\0') {
            return findMove(c, _from, _to);
        } else {
            throw new IllegalStateException("This move is already a capture.");
        }
    }

    /**
     * TRUE iff this move is a castle.
     *
     * @return _isCastle.
     */
    boolean isCastle() {
        return _isCastle;
    }

    /**
     * TRUE iff this move is a possible promotion move.
     *
     * @return _possiblePromotion.
     */
    boolean isPossiblePromotion() {
        return _possiblePromotion;
    }

    @Override
    public String toString() {
        return _from + "-" + _to;
    }

    /**
     * The set of all possible Moves, indexed by row and column of
     *  start, row and column of destination, and the piece being captured.
     */
    private static Move[][][] _moves = new Move[NUM_SQUARES][NUM_SQUARES][7];

    /**
     * Starting and destination squares.
     */
    private final Square _from, _to;

    /**
     * The piece that is captured by this move. Null character if not a capture.
     */
    private final char _captured;

    /**
     * TRUE iff this move is a Castle or it could
     * possibly be a promotion, respectively.
     */
    private final boolean _isCastle, _possiblePromotion;

    static {
        for (Square from : ALL_SQUARES) {
            for (Square to : ALL_SQUARES) {
                if (from.isPossibleMove(to)) {
                    _moves[from.index()][to.index()][0] = new Move(from, to, '\0', false);
                    _moves[from.index()][to.index()][1] = new Move(from, to, 'K', false);
                    _moves[from.index()][to.index()][2] = new Move(from, to, 'Q', false);
                    _moves[from.index()][to.index()][3] = new Move(from, to, 'R', false);
                    _moves[from.index()][to.index()][4] = new Move(from, to, 'B', false);
                    _moves[from.index()][to.index()][5] = new Move(from, to, 'N', false);
                    _moves[from.index()][to.index()][6] = new Move(from, to, 'P', false);
                }
            }
        }
        _moves[sq("e1").index()][sq("c1").index()][0] = new Move(sq("e1"), sq("c1"), '\0', true);
        _moves[sq("e1").index()][sq("g1").index()][0] = new Move(sq("e1"), sq("g1"), '\0', true);
        _moves[sq("e8").index()][sq("c8").index()][0] = new Move(sq("e8"), sq("c8"), '\0', true);
        _moves[sq("e8").index()][sq("g8").index()][0] = new Move(sq("e8"), sq("g8"), '\0', true);
    }
}
