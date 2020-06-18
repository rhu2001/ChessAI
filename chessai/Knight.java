package chessai;

/**
 * A knight in chess.
 *
 * @author Richard Hu
 */
public class Knight extends Piece {

    /**
     * Creates a knight with the given color.
     *
     * @param color Color to assign to this knight.
     */
    public Knight(Color color) {
        _color = color;
    }

    @Override
    Knight copy() {
        return new Knight(_color);
    }

    @Override
    char abbr() {
        return 'N';
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
