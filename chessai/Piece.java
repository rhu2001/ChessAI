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
     * Sets _hasMoved.
     *
     * @param hasMoved Value to set to.
     */
    void setMoved(boolean hasMoved) {
        _hasMoved = hasMoved;
    }

    /**
     * TRUE iff this piece has moved.
     *
     * @return _hasMoved.
     */
    boolean hasMoved() {
        return _hasMoved;
    }

    /**
     * Updates _sq.
     *
     * @param location New value of _sq.
     */
    void moveTo(Square location) {
        _sq = location;
    }

    /**
     * Gets _sq.
     *
     * @return _sq.
     */
    Square getLocation() {
        return _sq;
    }

    @Override
    public String toString() {
        return String.valueOf(abbr()) + _sq;
    }

    /**
     * Color of this piece.
     */
    protected Color _color;

    /**
     * TRUE iff this rook has moved.
     */
    protected boolean _hasMoved;

    /**
     * This piece's current location.
     */
    protected Square _sq;
}
