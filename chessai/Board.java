package chessai;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Default starting configuration of a chess board.
     */
    static final String[][] DEFAULT_LAYOUT = {
            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {},
            {},
            {},
            {},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"}
    };

    /**
     * Converts a character to a Color.
     *
     * @param c First letter of color.
     * @return Corresponding color, or null if
     * character is invalid.
     */
    static Color getColor(char c) {
        if (String.valueOf(c).toLowerCase().equals("w")) {
            return WHITE;
        } else if (String.valueOf(c).toLowerCase().equals("b")) {
            return BLACK;
        } else {
            return null;
        }
    }

    /**
     * Generates a new piece from an abbreviation.
     *
     * @param abbr Piece abbreviation.
     * @param color Color of piece.
     * @param sq Square of piece.
     *
     * @return New piece with specified parameters.
     */
    static Piece generatePiece(char abbr, Color color, Square sq) {
        abbr = String.valueOf(abbr).toLowerCase().charAt(0);
        return switch (abbr) {
            case 'b' -> new Bishop(color, sq);
            case 'k' -> new King(color, sq);
            case 'n' -> new Knight(color, sq);
            case 'p', '\0' -> new Pawn(color, sq);
            case 'q' -> new Queen(color, sq);
            case 'r' -> new Rook(color, sq);
            default -> null;
        };
    }

    /**
     * Creates a new board.
     *
     * @param layout Starting layout of board.
     * @param turn Color of starting turn.
     */
    Board(String[][] layout, Color turn) {
        initialize(layout, turn);
    }

    /**
     * Creates a new board on the default
     * starting layout.
     */
    Board() {
        initialize();
    }

    /**
     * Initializes the board to the given parameters.
     *
     * @param layout Layout of board.
     * @param turn Color to move first.
     */
    void initialize(String[][] layout, Color turn) {
        this.clear();
        _whitePieces.clear();
        _blackPieces.clear();

        _possibleWhiteMoves.clear();
        _possibleBlackMoves.clear();

        _possibleWhiteMovesUpdated = false;
        _possibleBlackMovesUpdated = false;

        if (layout != null) {
            for (int r = 0; r < layout.length; r++) {
                for (int c = 0; c < layout.length; c++) {
                    if (c < layout[r].length && layout[r][c] != null) {
                        Color color = getColor(layout[r][c].charAt(0));
                        set(sq(c, r), generatePiece(layout[r][c].charAt(1), color, sq(c, r)));
                        if (get(sq(c, r)).abbr() == 'K') {
                            updateKingSquare(sq(c, r), color);
                        }
                    }
                }
            }
        }

        _movesMade.clear();
        _turn = turn;
    }

    /**
     * Initializes the board to the default starting
     * configuration.
     */
    void initialize() {
        initialize(DEFAULT_LAYOUT, WHITE);
    }

    /**
     * Clears the board.
     */
    void clear() {
        Arrays.fill(_board, null);
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
        _possibleWhiteMovesUpdated = false;
        _possibleBlackMovesUpdated = false;

        if (get(sq) != null) {
            removePiece(get(sq));
        }
        if (piece != null && !hasPiece(piece)) {
            addPiece(piece);
        }
        if (piece != null && piece.abbr() == King.ABBR) {
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

    /**
     * Makes a move on the board with no
     * promotion.
     *
     * @param mv Move to make.
     */
    void makeMove(Move mv) {
        makeMove(mv, null);
    }

    /** Makes a move on the board.
     *
     * @param mv Move to make.
     * @param promotion The piece to promote to.
     */
    void makeMove(Move mv, Character promotion) {
        assert isLegal(mv);

        if (get(mv.getTo()) != null && !mv.isCastle()) {
            _movesMade.add(new MovePair(mv.capture(get(mv.getTo()).abbr()), get(mv.getFrom()).abbr()));
        } else {
            _movesMade.add(new MovePair(mv, get(mv.getFrom()).abbr()));
        }

        if (get(mv.getFrom()).abbr() == King.ABBR && mv.isCastle()) {
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
        } else if (get(mv.getFrom()).abbr() == Pawn.ABBR && mv.isPossiblePromotion()) {
            set(mv.getFrom(), null);
            set(mv.getTo(), generatePiece(promotion, _turn, mv.getTo()));
        } else {
            Piece moving = get(mv.getFrom());

            moving.moveTo(mv.getTo());

            set(mv.getFrom(), null);
            set(mv.getTo(), moving);
        }

        _turn = _turn.opposite();
    }

    /**
     * Undoes the previous move made.
     *
     * @return TRUE iff the undo was successful.
     */
    boolean undo() {
        if (_movesMade.size() == 0) {
            return false;
        }

        Move mv = _movesMade.get(_movesMade.size() - 1).mv();
        char moving = _movesMade.remove(_movesMade.size() - 1).moving();

        set(mv.getTo(), generatePiece(mv.getCaptured(), _turn, mv.getTo()));
        set(mv.getFrom(), generatePiece(moving, _turn.opposite(), mv.getFrom()), _turn.opposite());

        return true;
    }

    /**
     * Checks if a move is legal on the current
     * board.
     *
     * @param mv Move to check.
     * @return TRUE iff MV is possible on the
     * current board and the color making the
     * move is the same as _turn.
     */
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

        if (possibleMovesUpdated(get(mv.getFrom()).getColor())) {
            return possibleMoves(get(mv.getFrom()).getColor()).contains(mv);
        }

        if (mv.isCastle()) {
            return isPossibleCastle(mv);
        }

        return switch (get(mv.getFrom()).abbr()) {
            case Bishop.ABBR -> isPossibleBishop(mv);
            case King.ABBR -> isPossibleKing(mv);
            case Knight.ABBR -> isPossibleKnight(mv);
            case Pawn.ABBR -> isPossiblePawn(mv);
            case Queen.ABBR -> isPossibleQueen(mv);
            case Rook.ABBR -> isPossibleRook(mv);
            default -> false;
        };
    }

    //TODO Implement better check and checkmate conditions

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

        if (!(king.abbr() == King.ABBR && rook.abbr() == Rook.ABBR)
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
     * Checks all the squares between the start and end
     * of a move for occupation. Returns TRUE iff all
     * the squares between the start and end are empty or
     * all but the last are empty and the last is the
     * opposite color.
     *
     * @param mv Move to check.
     * @param dir Direction of the move.
     *
     * @return TRUE iff all the squares between the start
     * and end are empty or all but the last are empty
     * and the last is the opposite color.
     * */
    private boolean checkAllSteps(Move mv, int dir) {
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
        return checkAllSteps(mv, dir);
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
        return checkAllSteps(mv, dir);
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
        return checkAllSteps(mv, dir);
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
            if (isPossible(mv(piece.getLocation(), sq))) {
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
     * Checks if a color has its possible moves
     * HashSet updated.
     *
     * @param color Color to check.
     * @return TRUE iff COLOR's possible moves
     * are up-to-date.
     */
    boolean possibleMovesUpdated(Color color) {
        return switch (color) {
            case WHITE -> _possibleWhiteMovesUpdated;
            case BLACK -> _possibleBlackMovesUpdated;
            default -> false;
        };
    }

    /**
     * Returns a HashSet of all possible
     * moves on the current board for a
     * given color.
     *
     * @param color Color to return moves
     *              for.
     * @return HashSet containing all possible
     * moves for COLOR.
     */
    HashSet<Move> possibleMoves(Color color) {
        if (color == WHITE) {
            return possibleWhiteMoves();
        } else if (color == BLACK) {
            return possibleBlackMoves();
        } else {
            return null;
        }
    }

    /**
     * Returns a HashSet of all possible white
     * moves on the current board. If the HashSet is
     * not up-to-date, it is created from scratch and
     * marked as being up-to-date. Otherwise, the
     * up-to-date version is returned.
     *
     * @return HashSet containing all possible white
     * moves.
     */
    HashSet<Move> possibleWhiteMoves() {
        if (!_possibleWhiteMovesUpdated) {
            _possibleWhiteMoves.clear();
            for (Piece piece : getPieces(WHITE)) {
                _possibleWhiteMoves.addAll(piecePossibleMoves(piece));
            }
            _possibleWhiteMovesUpdated = true;
        }
        return _possibleWhiteMoves;
    }

    /**
     * Returns a HashSet of all possible wlack
     * moves on the current board. If the HashSet is
     * not up-to-date, it is created from scratch and
     * marked as being up-to-date. Otherwise, the
     * up-to-date version is returned.
     *
     * @return HashSet containing all possible black
     * moves.
     */
    HashSet<Move> possibleBlackMoves() {
        if (!_possibleBlackMovesUpdated) {
            _possibleBlackMoves.clear();
            for (Piece piece : getPieces(BLACK)) {
                _possibleBlackMoves.addAll(piecePossibleMoves(piece));
            }
            _possibleBlackMovesUpdated = true;
        }
        return _possibleBlackMoves;
    }

    /**
     * Finds all possible moves of a piece.
     *
     * @param piece Piece to check.
     * @return HashSet containing all possible
     * moves of PIECE.
     */
    HashSet<Move> piecePossibleMoves(Piece piece) {
        if (piece == null) {
            return null;
        }
        return switch (piece.abbr()) {
            case Bishop.ABBR -> bishopPossibleMoves(piece);
            case King.ABBR -> kingPossibleMoves(piece);
            case Knight.ABBR -> knightPossibleMoves(piece);
            case Pawn.ABBR -> pawnPossibleMoves(piece);
            case Queen.ABBR -> queenPossibleMoves(piece);
            case Rook.ABBR -> rookPossibleMoves(piece);
            default -> null;
        };
    }

    /**
     * Finds all possible moves of a Bishop.
     *
     * @param bishop Bishop to check.
     * @return HashSet containing all possible
     * moves of BISHOP.
     */
    HashSet<Move> bishopPossibleMoves(Piece bishop) {
        HashSet<Move> moves = new HashSet<>();
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(bishop.getLocation().moveDest(1, 1));
        squares.add(bishop.getLocation().moveDest(3, 1));
        squares.add(bishop.getLocation().moveDest(5, 1));
        squares.add(bishop.getLocation().moveDest(7, 1));
        while (squaresNotNull(squares)) {
            for (int i = 0; i < squares.size(); i++) {
                if (squares.get(i) != null) {
                    if (isPossible(mv(bishop.getLocation(), squares.get(i)))) {
                        moves.add(mv(bishop.getLocation(), squares.get(i)));
                        squares.set(i, squares.get(i).moveDest(2 * i + 1, 1));
                    } else {
                        squares.set(i, null);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Finds all possible moves of a King.
     *
     * @param king King to check.
     * @return HashSet containing all possible
     * moves of KING.
     */
    HashSet<Move> kingPossibleMoves(Piece king) {
        HashSet<Move> moves = new HashSet<>();
        Square sq = king.getLocation();
        for (int dir = 0; dir < 8; dir++) {
            if (sq.moveDest(dir, 1) != null
                    && isPossible(mv(sq, sq.moveDest(dir, 1)))) {
                moves.add(mv(sq, sq.moveDest(dir, 1)));
            }
        }
        if (!king.hasMoved()) {
            if (sq == sq("e1")) {
                if (isPossible(mv("e1-c1"))) {
                    moves.add(mv("e1-c1"));
                }
                if (isPossible(mv("e1-g1"))) {
                    moves.add(mv("e1-g1"));
                }
            } else {
                if (isPossible(mv("e8-c8"))) {
                    moves.add(mv("e8-c8"));
                }
                if (isPossible(mv("e8-g8"))) {
                    moves.add(mv("e8-g8"));
                }
            }
        }
        return moves;
    }

    /**
     * Finds all possible moves of a Knight.
     *
     * @param knight Knight to check.
     * @return HashSet containing all possible
     * moves of KNIGHT.
     */
    HashSet<Move> knightPossibleMoves(Piece knight) {
        HashSet<Move> moves = new HashSet<>();
        Square sq = knight.getLocation();
        for (int first : new int[] {-2, -1, 1, 2}) {
            for (int second : new int[] {-2, -1, 1, 2}) {
                if (Math.abs(first) != Math.abs(second)) {
                    if (exists(sq.col() + first, sq.row() + second)
                            && isPossible(mv(sq, sq(sq.col() + first, sq.row() + second)))) {
                        moves.add(mv(sq, sq(sq.col() + first, sq.row() + second)));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Finds all possible moves of a Pawn.
     *
     * @param pawn Pawn to check.
     * @return HashSet containing all possible
     * moves of PAWN.
     */
    HashSet<Move> pawnPossibleMoves(Piece pawn) {
        HashSet<Move> moves = new HashSet<>();
        Square dest;
        int adjust = 4 * (pawn.getColor() == BLACK ? 1 : 0);
        for (int i = 7 + adjust; i <= 8 + 1 + adjust; i++) {
            int dir = i % 8;
            dest = pawn.getLocation().moveDest(dir, 1);
            if (dest != null && isPossible(mv(pawn.getLocation(), dest))) {
                moves.add(mv(pawn.getLocation(), dest));
                dest = pawn.getLocation().moveDest(dir, 2);
                if ((dir == 0 || dir == 4)
                        && isPossible(mv(pawn.getLocation(), dest))) {
                    moves.add(mv(pawn.getLocation(), dest));
                }
            }
        }
        return moves;
    }

    /**
     * Finds all possible moves of a Queen.
     *
     * @param queen Queen to check.
     * @return HashSet containing all possible
     * moves of QUEEN.
     */
    HashSet<Move> queenPossibleMoves(Piece queen) {
        HashSet<Move> moves = new HashSet<>();
        moves.addAll(bishopPossibleMoves(queen));
        moves.addAll(rookPossibleMoves(queen));
        return moves;
    }

    /**
     * Finds all possible moves of a Rook.
     *
     * @param rook Rook to check.
     * @return HashSet containing all possible
     * moves of ROOK.
     */
    HashSet<Move> rookPossibleMoves(Piece rook) {
        HashSet<Move> moves = new HashSet<>();
        ArrayList<Square> squares = new ArrayList<>();
        squares.add(rook.getLocation().moveDest(0, 1));
        squares.add(rook.getLocation().moveDest(2, 1));
        squares.add(rook.getLocation().moveDest(4, 1));
        squares.add(rook.getLocation().moveDest(6, 1));
        while (squaresNotNull(squares)) {
            for (int i = 0; i < squares.size(); i++) {
                if (squares.get(i) != null) {
                    if (isPossible(mv(rook.getLocation(), squares.get(i)))) {
                        moves.add(mv(rook.getLocation(), squares.get(i)));
                        squares.set(i, squares.get(i).moveDest(2 * i, 1));
                    } else {
                        squares.set(i, null);
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Checks whether any square in a List
     * of squares is NULL.
     *
     * @param squares List of squares to check.
     * @return TRUE if at least one SQUARE in
     * the List is not NULL.
     */
    boolean squaresNotNull(List<Square> squares) {
        for (Square sq : squares) {
            if (sq != null) {
                return true;
            }
        }
        return false;
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
     * Sets of all possible moves on the current
     * board.
     */
    final HashSet<Move> _possibleWhiteMoves = new HashSet<>(),
            _possibleBlackMoves = new HashSet<>();

    /**
     * TRUE iff possible moves sets are up-to-date.
     */
    boolean _possibleWhiteMovesUpdated = false,
            _possibleBlackMovesUpdated = false;

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
    private final List<MovePair> _movesMade = new ArrayList<>();
}

/**
 * Wrapper class for storing a move and
 * the corresponding piece that made the
 * move.
 *
 * @author Richard Hu
 */
class MovePair {

    /**
     * Default constructor.
     *
     * @param mv Move.
     * @param moving Moving piece.
     */
    MovePair(Move mv, char moving) {
        _mv = mv;
        _moving = moving;
    }

    /**
     * Returns move.
     *
     * @return _mv.
     */
    Move mv() {
        return _mv;
    }

    /**
     * Returns moving piece.
     *
     * @return _moving.
     */
    char moving() {
        return _moving;
    }

    /**
     * Move.
     */
    private Move _mv;

    /**
     * Moving piece.
     */
    private char _moving;
}