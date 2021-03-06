package chessai;

/**
 * A bishop in chess.
 *
 * @author Richard Hu
 */
public class Bishop extends Piece {

    /**
     * Denotes the character abbreviation of the bishop.
     */
    static final char ABBR = 'B';

    /**
     * Creates a bishop with the given color.
     *
     * @param color Color to assign to this bishop.
     * @param sq Location
     */
    public Bishop(Color color, Square sq) {
        _color = color;
        _sq = sq;
    }

    @Override
    Bishop copy() {
        return new Bishop(_color, _sq);
    }

    @Override
    char abbr() {
        return ABBR;
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♗';
            case BLACK -> '♝';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}