package chessai;

/**
 * A bishop in chess.
 *
 * @author Richard Hu
 */
public class Bishop extends Piece {

    /**
     * Creates a bishop with the given color.
     *
     * @param color Color to assign to this bishop.
     */
    public Bishop(Color color) {
        _color = color;
    }

    @Override
    Bishop copy() {
        return new Bishop(_color);
    }

    @Override
    char abbr() {
        return 'B';
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