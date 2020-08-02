package chessai;

/**
 * A rook in chess.
 *
 * @author Richard Hu
 */
public class Rook extends Piece {

    /**
     * Denotes the character abbreviation of the rook.
     */
    static final char ABBR = 'R';

    /**
     * Creates a rook with the given color.
     *
     * @param color Color to assign to this rook.
     * @param sq Location
     */
    public Rook(Color color, Square sq) {
        _color = color;
        _sq = sq;
        _hasMoved = false;
    }

    @Override
    Rook copy() {
        Rook copy = new Rook(_color, _sq);
        copy.setMoved(_hasMoved);
        return copy;
    }

    @Override
    char abbr() {
        return ABBR;
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♖';
            case BLACK -> '♜';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}