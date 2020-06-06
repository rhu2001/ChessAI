package chessai;

/**
 * A king in chess.
 *
 * @author Richard Hu
 */
public class King extends Piece {

    /**
     * Creates a king with the given color.
     *
     * @param color chess.Color to assign to this king.
     */
    public King(Color color) {
        _color = color;
    }

    @Override
    char abbr() {
        return 'K';
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♔';
            case BLACK -> '♚';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}
