package chessai;

/**
 * The abstract chess.Piece class represents
 * all of the chess pieces.
 *
 * @author Richard Hu
 */
public abstract class Piece {

    /**
     * Gets the chess.Color of this piece.
     *
     * @return _color.
     */
    Color getColor() {
        return _color;
    }

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
     * chess.Color of this piece.
     */
    protected Color _color;
}