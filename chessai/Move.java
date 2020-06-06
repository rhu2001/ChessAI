package chessai;

import static chessai.Square.*;

public class Move {

    /**
     * Returns the move starting and ending at the specified squares
     * with the given captured piece.
     *
     * @param s Starting and ending squares in String format.
     * @param captured Captured piece.
     *
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
     * Returns the move starting and ending at the specified squares
     * with the given captured piece.
     *
     * @param from Starting square.
     * @param to Destination square.
     * @param captured Captured piece.
     *
     * @return Move with specified parameters.
     */
    static Move mv(Square from, Square to, char captured) {
        if (from != null && to != null) {
            return switch (captured) {
                case '\0' -> _moves[from.index()][to.index()][0];
                case 'K' -> _moves[from.index()][to.index()][1];
                case 'Q' -> _moves[from.index()][to.index()][2];
                case 'R' -> _moves[from.index()][to.index()][3];
                case 'B' -> _moves[from.index()][to.index()][4];
                case 'N' -> _moves[from.index()][to.index()][5];
                case 'P' -> _moves[from.index()][to.index()][6];
                default -> throw new IllegalStateException("Unexpected value: " + captured);
            };
        } else {
            return null;
        }
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
     */
    private Move(Square from, Square to, char captured) {
        _from = from;
        _to = to;
        _captured = captured;
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
     * Starting and destination Squares.
     */
    private final Square _from, _to;

    /**
     * The piece that is captured by this move. Null character if not a capture.
     */
    private final char _captured;

    static {
        for (Square from : ALL_SQUARES) {
            for (Square to : ALL_SQUARES) {
                if (from.isPossibleMove(to)) {
                    _moves[from.index()][to.index()][0] = new Move(from, to, '\0');
                    _moves[from.index()][to.index()][1] = new Move(from, to, 'K');
                    _moves[from.index()][to.index()][2] = new Move(from, to, 'Q');
                    _moves[from.index()][to.index()][3] = new Move(from, to, 'R');
                    _moves[from.index()][to.index()][4] = new Move(from, to, 'B');
                    _moves[from.index()][to.index()][5] = new Move(from, to, 'N');
                    _moves[from.index()][to.index()][6] = new Move(from, to, 'P');
                }
            }
        }
    }
}
