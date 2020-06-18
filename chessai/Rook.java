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
     * @param color Color to assign to this rook.
     */
    public Rook(Color color) {
        _color = color;
    }

    @Override
    Rook copy() {
        return new Rook(_color);
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