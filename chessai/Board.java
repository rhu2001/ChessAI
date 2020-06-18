package chessai;

import java.util.ArrayList;
import java.util.List;

import static chessai.Square.*;
import static chessai.Color.*;

/**
 * The board on which the game of
 * chess is played.
 *
 * @author Richard Hu
 */
public class Board {

    /**
     * The total number of possible rows or
     * columns.
     */
    static final int BOARD_SIZE = 8;

    /**
     * The total number of possible squares.
     */
    static final int NUM_SQUARES = BOARD_SIZE * BOARD_SIZE;

    /**
     * Creates a new board.
     */
    Board() {
        initialize();
    }

    /**
     * Initializes the board to the given parameters.
     */
    void initialize() {
        set(sq("a1"), new Rook(WHITE));
        set(sq("b1"), new Knight(WHITE));
        set(sq("c1"), new Bishop(WHITE));
        set(sq("d1"), new Queen(WHITE));
        set(sq("e1"), new King(WHITE));
        set(sq("f1"), new Bishop(WHITE));
        set(sq("g1"), new Knight(WHITE));
        set(sq("h1"), new Rook(WHITE));

        set(sq("a2"), new Pawn(WHITE));
        set(sq("b2"), new Pawn(WHITE));
        set(sq("c2"), new Pawn(WHITE));
        set(sq("d2"), new Pawn(WHITE));
        set(sq("e2"), new Pawn(WHITE));
        set(sq("f2"), new Pawn(WHITE));
        set(sq("g2"), new Pawn(WHITE));
        set(sq("h2"), new Pawn(WHITE));

        set(sq("a8"), new Rook(BLACK));
        set(sq("b8"), new Knight(BLACK));
        set(sq("c8"), new Bishop(BLACK));
        set(sq("d8"), new Queen(BLACK));
        set(sq("e8"), new King(BLACK));
        set(sq("f8"), new Bishop(BLACK));
        set(sq("g8"), new Knight(BLACK));
        set(sq("h8"), new Rook(BLACK));

        set(sq("a7"), new Pawn(BLACK));
        set(sq("b7"), new Pawn(BLACK));
        set(sq("c7"), new Pawn(BLACK));
        set(sq("d7"), new Pawn(BLACK));
        set(sq("e7"), new Pawn(BLACK));
        set(sq("f7"), new Pawn(BLACK));
        set(sq("g7"), new Pawn(BLACK));
        set(sq("h7"), new Pawn(BLACK));

        _movesMade.clear();

        _turn = WHITE;
    }

    /**
     * Return the contents of the square at SQ.
     *
     * @return The piece at sq.
     */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /**
     * Sets the square SQ to PIECE. If NEXT is
     * not null, sets the turn to NEXT. If
     * PROMOTION is not null and a promotion is valid,
     * the pawn is promoted to it.
     *
     * @param sq Square to set.
     * @param piece Piece to set SQ to.
     * @param next The color of the next turn.
     */
    void set(Square sq, Piece piece, Color next) {
        _board[sq.index()] = piece;

        if (next != null) {
            _turn = next;
        }
    }

    /**
     * Sets the square SQ to PIECE.
     *
     * @param sq Square set.
     * @param piece Piece to set SQ to.
     */
    void set(Square sq, Piece piece) {
        set(sq, piece, null);
    }

    /** Makes a move on the board.
     *
     * @param mv Move to make.
     * @param promotion The piece to promote to.
     */
    void makeMove(Move mv, Piece promotion) {
        assert isLegal(mv);

        if (get(mv.getTo()) != null && !mv.isCastle()) {
            _movesMade.add(mv.capture(get(mv.getTo()).abbr()));
        } else {
            _movesMade.add(mv);
        }

        if (mv.isCastle()) {
            Piece king = get(mv.getFrom());
            Piece rook = null;

            if (sq("c1") == mv.getTo()) {
                rook = get(sq("a1"));
            } else if (sq("g1") == mv.getTo()) {
                rook = get(sq("h1"));
            } else if (sq("c8") == mv.getTo()) {
                rook = get(sq("a8"));
            } else if (sq("g8") == mv.getTo()) {
                rook = get(sq("h8"));
            }

            set(mv.getFrom(), rook);
            set(mv.getTo(), king);
        } else if (get(mv.getFrom()) instanceof Pawn && mv.isPossiblePromotion()) {
            set(mv.getFrom(), null);
            set(mv.getTo(), promotion);
        } else {
            Piece moving = get(mv.getFrom());
            set(mv.getFrom(), null);
            set(mv.getTo(), moving);
        }

        _turn = _turn.opposite();
    }

    /**
     * Returns whether the specified move
     * is legal on this board.
     *
     * @param mv Move to check.
     * @return TRUE iff MV is a legal move
     * on this board.
     */
    boolean isLegal(Move mv) {
        return true;
    }

    void undo() {
        assert _movesMade.size() > 0;

        Move undo = _movesMade.get(_movesMade.size() - 1);
        Piece moving = get(undo.getTo());
        Piece captured = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===\n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            sb.append("    ").append(r + 1).append(" ");
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (get(sq(c, r)) != null) {
                    sb.append(get(sq(c, r)).symbol()).append(" ");
                } else {
                    sb.append("- ");
                }
            }
            sb.append("\n");
        }
        sb.append("    ").append("  ").append("a b c d e f g h\n");
        sb.append("Next move: ").append(_turn.fullName()).append("\n===\n");
        return sb.toString();
    }

    /**
     * Contents of this board by index().
     */
    private final Piece[] _board = new Piece[NUM_SQUARES];

    /**
     * The color that is to move.
     */
    private Color _turn;

    /**
     * Board history.
     */
    private final List<Move> _movesMade = new ArrayList<>();
}
