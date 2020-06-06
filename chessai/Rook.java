package chessai;

/**
 * A rook in chess.
 *
 * @author Richard Hu
 */
public class Rook extends Piece {

    /**
     * Creates a rook with the given color.
     *
     * @param color chess.Color to assign to this rook.
     */
    public Rook(Color color) {
        _color = color;
    }

    @Override
    char abbr() {
        return 'R';
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