package chessai;

/**
 * A knight in chess.
 *
 * @author Richard Hu
 */
public class Knight extends Piece {

    /**
     * Denotes the character abbreviation of the knight.
     */
    static final char ABBR = 'N';

    /**
     * Creates a knight with the given color.
     *
     * @param color Color to assign to this knight.
     * @param sq Location
     */
    public Knight(Color color, Square sq) {
        _color = color;
        _sq = sq;
    }

    @Override
    Knight copy() {
        return new Knight(_color, _sq);
    }

    @Override
    char abbr() {
        return ABBR;
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♘';
            case BLACK -> '♞';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}
