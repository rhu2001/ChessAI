package chessai;

/**
 * The abstract chess.Piece class represents
 * all of the chess pieces.
 *
 * @author Richard Hu
 */
public abstract class Piece {

    /**
     * Returns a copy of this piece.
     *
     * @return Copy of this.
     */
    abstract Piece copy();

    /**
     * One-letter abbreviation of this
     * piece.
     *
     * @return Abbreviation of this piece.
     */
    abstract char abbr();

    /**
     * Unicode character corresponding to
     * this piece and color.
     *
     * @return This piece's symbol.
     */
    abstract char symbol();

    /**
     * Gets the Color of this piece.
     *
     * @return _color.
     */
    Color getColor() {
        return _color;
    }

    /**
     * Color of this piece.
     */
    protected Color _color;
}
