package chessai;

/**
 * A pawn in chess.
 *
 * @author Richard Hu
 */
public class Pawn extends Piece {

    /**
     * Denotes the character abbreviation of the pawn.
     */
    static final char ABBR = '\0';

    /**
     * Creates a pawn with the given color.
     *
     * @param color Color to assign to this pawn.
     * @param sq Location
     */
    public Pawn(Color color, Square sq) {
        _color = color;
        _sq = sq;
    }

    @Override
    Pawn copy() {
        return new Pawn(_color, _sq);
    }

    @Override
    char abbr() {
        return ABBR;
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
