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
     * @param color Color to assign to this king.
     * @param sq Location
     */
    public King(Color color, Square sq) {
        _color = color;
        _sq = sq;
        _hasMoved = false;
    }

    @Override
    King copy() {
        King copy = new King(_color, _sq);
        copy.setMoved(_hasMoved);
        return copy;
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
