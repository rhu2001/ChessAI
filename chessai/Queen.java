package chessai;

/**
 * A queen in chess.
 *
 * @author Richard Hu
 */
public class Queen extends Piece {

    /**
     * Denotes the character abbreviation of the queen.
     */
    static final char ABBR = 'Q';

    /**
     * Creates a queen with the given color.
     *
     * @param color Color to assign to this queen.
     * @param sq Location
     */
    public Queen(Color color, Square sq) {
        _color = color;
        _sq = sq;
    }

    @Override
    Queen copy() {
        return new Queen(_color, _sq);
    }

    @Override
    char abbr() {
        return ABBR;
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♕';
            case BLACK -> '♛';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}
