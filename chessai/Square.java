package chessai;

import java.util.regex.Pattern;

/**
 * An individual square on the board.
 * Squares are indexed from 0 - 63 and
 * are represented by a letter and number
 * (e.g. a1, b3). Squares are immutable
 * and unique: they are created by static
 * methods and retrieved by factory methods.
 *
 * @author Richard Hu
 */
public final class Square implements Comparable<Square> {

    /**
     * The total number of possible rows or
     * columns.
     */
    static final int BOARD_SIZE = 8;

    /**
     * The total number of possible squares.
     */
    static final int NUM_SQUARES = BOARD_SIZE * BOARD_SIZE;

    /**
     * The regular expression for a square
     * designation (e.g. a1, b3).
     */
    static final Pattern SQ_REGEX = Pattern.compile("([a-h][1-8])");

    /**
     * Return the unique square at COL ROW.
     *
     * @param col Column of square.
     * @param row Row of square.
     *
     * @return The square at the given column and row.
     */
    static Square sq(int col, int row) {
        if (!exists(row, col)) {
            throw new RuntimeException("row or column out of bounds");
        }
        return SQUARES[col][row];
    }

    /**
     * Return the unique square denoting the position in POS, in the
     * standard text format for a square (e.g. a4). Return null if POS
     * does not denote a valid square designation.
     *
     * @param pos Position of the square.
     *
     * @return The square at the position if it exists.
     */
    static Square sq(String pos) {
        if (SQ_REGEX.matcher(pos).matches()) {
            return sq(pos.charAt(0) - 'a', pos.charAt(1) - '1');
        }
        return null;
    }

    /**
     * Return this square's row position,
     * where 0 is the bottom row.
     *
     * @return _row.
     */
    int row() {
        return _row;
    }

    /**
     * Return this square's column position,
     * where 0 is the leftmost column.
     *
     * @return _col.
     */
    int col() {
        return _col;
    }

    /**
     * Return true iff COL ROW is a legal square.
     *
     * @param col Column to check.
     * @param row Row to check.
     *
     * @return Whether a COL ROW pairing exists.
     */
    static boolean exists(int col, int row) {
        return (char) row < BOARD_SIZE && (char) col < BOARD_SIZE;
    }

    /**
     * The square (COL, ROW).
     *
     * @param col Column of this square.
     * @param row Row of this square.
     */
    private Square(int col, int row) {
        _row = row;
        _col = col;
        _str = String.format("%c%d", (char) ('a' + _col), 1 + _row);
    }

    /**
     * Returns false iff under no circumstances can
     * a move from this square to OTHER be made legally.
     *
     * @param other Other square.
     *
     * @return Whether the move is possible.
     */
    boolean isPossibleMove(Square other) {
        return this != other
            && ((this.row() == other.row() || this.col() == other.col())
            || (Math.abs(this.col() - other.col()) == Math.abs(this.row() - other.row()))
            || (Math.abs(this.col() - other.col()) == 2 && Math.abs(this.row() - other.row()) == 1)
            || (Math.abs(this.row() - other.row()) == 2 && Math.abs(this.col() - other.col()) == 1));
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    /**
     * Return a unique number between 0 and NUM_SQUARES-1, inclusive,
     * for this chess.Square. All distinct squares have distinct index values.
     *
     * @return Index of this square.
     */
    int index() {
        return (_row << 3) + _col;
    }

    @Override
    public int compareTo(Square other) {
        return Integer.compare(index(), other.index());
    }

    @Override
    public int hashCode() {
        return index();
    }

    @Override
    public String toString() {
        return _str;
    }

    /**
     * The cache of all created squares, by row and column.
     */
    private static final Square[][] SQUARES = new Square[BOARD_SIZE][BOARD_SIZE];

    /**
     * A list of all Squares on a board.
     */
    static final Square[] ALL_SQUARES = new Square[NUM_SQUARES];

    static {
        for (int c = 0; c < BOARD_SIZE; c += 1) {
            for (int r = 0; r < BOARD_SIZE; r += 1) {
                Square sq = new Square(c, r);
                ALL_SQUARES[sq.index()] = SQUARES[c][r] = sq;
            }
        }
    }

    /**
     * This square's row and column.
     */
    private final int _row, _col;

    /**
     * This square's String denotation.
     */
    private final String _str;
}
