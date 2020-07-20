package chessai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static chessai.Square.*;
import static chessai.Move.*;
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
        set(sq("a1"), new Rook(WHITE, sq("a1")));
        set(sq("b1"), new Knight(WHITE, sq("b1")));
        set(sq("c1"), new Bishop(WHITE, sq("c1")));
        set(sq("d1"), new Queen(WHITE, sq("d1")));
        set(sq("e1"), new King(WHITE, sq("e1")));
        updateKingSquare(sq("e1"), WHITE);
        set(sq("f1"), new Bishop(WHITE, sq("f1")));
        set(sq("g1"), new Knight(WHITE, sq("g1")));
        set(sq("h1"), new Rook(WHITE, sq("h1")));

        set(sq("a2"), new Pawn(WHITE, sq("a2")));
        set(sq("b2"), new Pawn(WHITE, sq("b2")));
        set(sq("c2"), new Pawn(WHITE, sq("c2")));
        set(sq("d2"), new Pawn(WHITE, sq("d2")));
        set(sq("e2"), new Pawn(WHITE, sq("e2")));
        set(sq("f2"), new Pawn(WHITE, sq("f2")));
        set(sq("g2"), new Pawn(WHITE, sq("g2")));
        set(sq("h2"), new Pawn(WHITE, sq("h2")));

        set(sq("a8"), new Rook(BLACK, sq("a8")));
        set(sq("b8"), new Knight(BLACK, sq("b8")));
        set(sq("c8"), new Bishop(BLACK, sq("c8")));
        set(sq("d8"), new Queen(BLACK, sq("d8")));
        set(sq("e8"), new King(BLACK, sq("e8")));
        updateKingSquare(sq("e8"), BLACK);
        set(sq("f8"), new Bishop(BLACK, sq("f8")));
        set(sq("g8"), new Knight(BLACK, sq("g8")));
        set(sq("h8"), new Rook(BLACK, sq("h8")));

        set(sq("a7"), new Pawn(BLACK, sq("a7")));
        set(sq("b7"), new Pawn(BLACK, sq("b7")));
        set(sq("c7"), new Pawn(BLACK, sq("c7")));
        set(sq("d7"), new Pawn(BLACK, sq("d7")));
        set(sq("e7"), new Pawn(BLACK, sq("e7")));
        set(sq("f7"), new Pawn(BLACK, sq("f7")));
        set(sq("g7"), new Pawn(BLACK, sq("g7")));
        set(sq("h7"), new Pawn(BLACK, sq("h7")));

        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 8; c++) {
                _whitePieces.add(get(sq(c, r)));
            }
        }

        for (int r = 6; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                _blackPieces.add(get(sq(c, r)));
            }
        }

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
        if (get(sq) != null) {
            removePiece(get(sq));
        }
        if (piece != null && !hasPiece(piece)) {
            addPiece(piece);
        }
        if (piece instanceof King) {
            updateKingSquare(sq, piece.getColor());
        }
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

        if (get(mv.getFrom()) instanceof King && mv.isCastle()) {
            List<Piece> pieces = getCastlePieces(mv);

            Piece king = pieces.get(0);
            Piece rook = pieces.get(1);

            int dir = rook.getLocation().direction(king.getLocation());

            king.moveTo(mv.getTo());

            set(mv.getFrom(), null);
            set(mv.getTo(), king);

            set(rook.getLocation(), null);
            rook.moveTo(king.getLocation().moveDest(dir, 1));
            set(rook.getLocation(), rook);
        } else if (get(mv.getFrom()) instanceof Pawn && mv.isPossiblePromotion()) {
            set(mv.getFrom(), null);
            set(mv.getTo(), promotion);
        } else {
            Piece moving = get(mv.getFrom());

            moving.moveTo(mv.getTo());

            set(mv.getFrom(), null);
            set(mv.getTo(), moving);
        }

        _turn = _turn.opposite();
    }

    boolean isLegal(Move mv) {
        return isPossible(mv) && get(mv.getFrom()).getColor() == _turn;
    }

    /**
     * Returns whether the specified move
     * is possible on this board.
     *
     * @param mv Move to check.
     * @return TRUE iff MV is a possible move
     * on this board.
     */
    boolean isPossible(Move mv) {
        if (mv == null || get(mv.getFrom()) == null) {
            return false;
        }

        if (mv.isCastle()) {
            return isPossibleCastle(mv);
        }

        return switch (get(mv.getFrom()).abbr()) {
            case 'B' -> isPossibleBishop(mv);
            case 'K' -> isPossibleKing(mv);
            case 'N' -> isPossibleKnight(mv);
            case '\0' -> isPossiblePawn(mv);
            case 'Q' -> isPossibleQueen(mv);
            case 'R' -> isPossibleRook(mv);
            default -> false;
        };
    }

    /**
     * TRUE iff MV is a possible castle move.
     *
     * @param mv Move to check.
     * @return Whether MV is a possible castle move.
     */
    boolean isPossibleCastle(Move mv) {
        assert mv.isCastle();

        List<Piece> pieces = getCastlePieces(mv);

        Piece king = pieces.get(0);
        Piece rook = pieces.get(1);

        if (!(king instanceof King && rook instanceof Rook)
                || (king.hasMoved() || rook.hasMoved())) {
            return false;
        }

        int dir = mv.getFrom().direction(mv.getTo());
        Square sq = mv.getFrom().moveDest(dir, 1);

        while (sq != mv.getTo()) {
            if (get(sq) != null || inCheck(sq, king.getColor())) {
                return false;
            }
            sq = sq.moveDest(dir, 1);
        }
        return !inCheck(sq, king.getColor());
    }

    /**
     * TRUE iff MV is a possible bishop move.
     *
     * @param mv Move to check.
     * @return Whether a bishop can make the move.
     */
    boolean isPossibleBishop(Move mv) {
        int dir = mv.direction();
        if (dir != 1 && dir != 3 && dir != 5 && dir != 7) {
            return false;
        }
        Square sq = mv.getFrom().moveDest(dir, 1);
        while (sq != mv.getTo()) {
            if (get(sq) != null) {
                return false;
            }
            sq = sq.moveDest(dir, 1);
        }
        return get(mv.getTo()) == null || get(mv.getTo()).getColor() != get(mv.getFrom()).getColor();
    }

    /**
     * TRUE iff MV is a possible king move.
     *
     * @param mv Move to check.
     * @return Whether a king can make the move.
     */
    boolean isPossibleKing(Move mv) {
        return mv.distance() == 1 && !inCheck(mv.getTo(), get(mv.getFrom()).getColor())
                && (get(mv.getTo()) == null || get(mv.getTo()).getColor() != get(mv.getFrom()).getColor());
    }

    /**
     * TRUE iff MV is a possible knight move.
     *
     * @param mv Move to check.
     * @return Whether a king can make the move.
     */
    boolean isPossibleKnight(Move mv) {
        int rowDiff = Math.abs(mv.getTo().row() - mv.getFrom().row());
        int colDiff = Math.abs(mv.getTo().col() - mv.getFrom().col());
        return ((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1))
                && (get(mv.getTo()) == null || get(mv.getTo()).getColor() != get(mv.getFrom()).getColor());
    }

    /**
     * TRUE iff MV is a possible pawn move.
     *
     * @param mv Move to check.
     * @return Whether a pawn can make the move.
     */
    boolean isPossiblePawn(Move mv) {
        Color color = get(mv.getFrom()).getColor();
        return switch (color) {
            case WHITE -> mv.getTo().row() > mv.getFrom().row()
                            && ((mv.direction() == 0 && get(mv.getTo()) == null)
                                || ((mv.direction() == 1 || mv.direction() == 7) && get(mv.getTo()) != null
                                    && get(mv.getTo()).getColor() != get(mv.getFrom()).getColor()))
                            && (mv.distance() == 1
                                || (!get(mv.getFrom()).hasMoved()
                                    && mv.direction() == 0 && mv.distance() == 2));
            case BLACK -> mv.getTo().row() < mv.getFrom().row()
                            && ((mv.direction() == 4 && get(mv.getTo()) == null)
                                || ((mv.direction() == 3 || mv.direction() == 5) && get(mv.getTo()) != null
                                    && get(mv.getTo()).getColor() != get(mv.getFrom()).getColor()))
                            && (mv.distance() == 1
                                || (!get(mv.getFrom()).hasMoved()
                                    && mv.direction() == 4 && mv.distance() == 2));
        };
    }

    /**
     * TRUE iff MV is a possible queen move.
     *
     * @param mv Move to check.
     * @return Whether a queen can make the move.
     */
    boolean isPossibleQueen(Move mv) {
        int dir = mv.direction();
        if (dir == -1) {
            return false;
        }
        Square sq = mv.getFrom().moveDest(dir, 1);
        while (sq != mv.getTo()) {
            if (get(sq) != null) {
                return false;
            }
            sq = sq.moveDest(dir, 1);
        }
        return get(mv.getTo()) == null || get(mv.getTo()).getColor() != get(mv.getFrom()).getColor();
    }

    /**
     * TRUE iff MV is a possible rook move.
     *
     * @param mv Move to check.
     * @return Whether a rook can make the move.
     */
    boolean isPossibleRook(Move mv) {
        int dir = mv.direction();
        if (dir != 0 && dir != 2 && dir != 4 && dir != 6) {
            return false;
        }
        Square sq = mv.getFrom().moveDest(dir, 1);
        while (sq != mv.getTo()) {
            if (get(sq) != null) {
                return false;
            }
            sq = sq.moveDest(dir, 1);
        }
        return get(mv.getTo()) == null || get(mv.getTo()).getColor() != get(mv.getFrom()).getColor();
    }

    /**
     * TRUE iff hypothetically, a square with a king of
     * a particular color is in check.
     *
     * @param sq Square to check.
     * @param color Color of hypothetical king in SQ.
     * @return Whether the square is in check.
     */
    boolean inCheck(Square sq, Color color) {
        for (Piece piece : getPieces(color.opposite())) {
            if (isLegal(mv(piece.getLocation(), sq))) {
                return true;
            }
        }
        return false;
    }

    /**
     * TRUE iff the side of COLOR is in check.
     *
     * @param color Side to check.
     * @return Whether the specified side is in check.
     */
    boolean inCheck(Color color) {
        return switch (color) {
            case WHITE -> inCheck(getKingSquare(WHITE), color);
            case BLACK -> inCheck(getKingSquare(BLACK), color);
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        };
    }

    /**
     * Returns the king and rook involved in a castle move.
     *
     * @param mv Move to check.
     * @return List containing king and rook.
     */
    List<Piece> getCastlePieces(Move mv) {
        assert mv.isCastle();

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

        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(king);
        pieces.add(rook);

        return pieces;
    }

    /**
     * Gets all pieces of a certain color.
     *
     * @param color Color of pieces.
     * @return Hashset of pieces.
     */
    HashSet<Piece> getPieces(Color color) {
        return switch (color) {
            case WHITE -> _whitePieces;
            case BLACK -> _blackPieces;
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        };
    }

    /**
     * TRUE iff this board contains PIECE
     *
     * @param piece Piece to check for.
     * @return Whether this board contains the piece.
     */
    boolean hasPiece(Piece piece) {
        return switch (piece.getColor()) {
            case WHITE -> _whitePieces.contains(piece);
            case BLACK -> _blackPieces.contains(piece);
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        };
    }

    /**
     * Adds a piece to the board to be tracked.
     *
     * @param piece Piece to add.
     */
    void addPiece(Piece piece) {
        switch (piece.getColor()) {
            case WHITE -> _whitePieces.add(piece);
            case BLACK -> _blackPieces.add(piece);
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        }
    }

    /**
     * Removes a piece from the board and returns it.
     *
     * @param piece Piece to remove.
     * @return Removed piece.
     */
    Piece removePiece(Piece piece) {
        switch (piece.getColor()) {
            case WHITE -> _whitePieces.remove(piece);
            case BLACK -> _blackPieces.remove(piece);
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        }
        return piece;
    }

    /**
     * Updates the king square.
     *
     * @param sq New square.
     * @param color King color.
     */
    void updateKingSquare(Square sq, Color color) {
        switch (color) {
            case WHITE -> _kingSquares[0] = sq;
            case BLACK -> _kingSquares[1] = sq;
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        }
    }

    /**
     * Returns the square of the king of the specified color.
     *
     * @param color King color.
     * @return Square of king.
     */
    Square getKingSquare(Color color) {
        return switch (color) {
            case WHITE -> _kingSquares[0];
            case BLACK -> _kingSquares[1];
            default -> throw new IllegalStateException("Piece color must be WHITE or BLACK.");
        };
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
     * All Pieces of a particular color.
     */
    final HashSet<Piece> _whitePieces = new HashSet<>(32),
            _blackPieces = new HashSet<>(32);

    /**
     * Locations of the kings.
     */
    final Square[] _kingSquares = new Square[2];

    /**
     * The color that is to move.
     */
    private Color _turn;

    /**
     * Board history.
     */
    private final List<Move> _movesMade = new ArrayList<>();
}
