package chessai;

/**
 * A pawn in chess.
 *
 * @author Richard Hu
 */
public class Pawn extends Piece {

    /**
     * Creates a pawn with the given color.
     *
     * @param color Color to assign to this pawn.
     */
    public Pawn(Color color) {
        _color = color;
    }

    @Override
    char abbr() {
        return 'P';
    }

    @Override
    char symbol() {
        return switch (_color) {
            case WHITE -> '♙';
            case BLACK -> '♟';
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}
