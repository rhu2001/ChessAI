package chessai;

/**
 * A queen in chess.
 *
 * @author Richard Hu
 */
public class Queen extends Piece {

    /**
     * Creates a queen with the given color.
     *
     * @param color Color to assign to this queen.
     */
    public Queen(Color color) {
        _color = color;
    }

    @Override
    char abbr() {
        return 'Q';
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
