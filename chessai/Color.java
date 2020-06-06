package chessai;

/**
 * The chess.Color of a chess.Piece object denotes whether
 * a particular piece belongs to the white (W)
 * or black (B) player.
 *
 * @author Richard Hu
 */
public enum Color {

    /**
     * The possible colors of a piece.
     */
    WHITE, BLACK;

    /**
     * Returns the opposite color of this color
     *
     * @return This color's opposite.
     */
    Color opposite() {
        return switch (this) {
            case WHITE -> BLACK;
            case BLACK -> WHITE;
            default -> throw new RuntimeException("Unreachable statement");
        };
    }

    /** Returns the full name of this color.
     *
     * @return Full name of color.
     */
    String fullName() {
        return switch (this) {
            case WHITE -> "White";
            case BLACK -> "Black";
            default -> throw new RuntimeException("Unreachable statement");
        };
    }

    /** Returns abbreviation of this color.
     *
     * @return Abbreviated name of this color.
     */
    String abbr() {
        return switch (this) {
            case WHITE -> "W";
            case BLACK -> "B";
            default -> throw new RuntimeException("Unreachable statement");
        };
    }
}
