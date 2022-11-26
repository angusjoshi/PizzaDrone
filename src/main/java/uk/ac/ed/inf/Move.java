package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record Move(LngLat from, LngLat to, CompassDirection direction) {
    public static Move hover(LngLat from) {
        return new Move(from, from, null);
    }
    public Move reverseMove() {
        return new Move(to, from, direction.getOppositeDirection());
    }
    public static List<Move> reverseMoveList(List<Move> moves) {
        List<Move> reversedMoves = new ArrayList<>(moves);

        Collections.reverse(reversedMoves);
        return reversedMoves.stream().map(Move::reverseMove).toList();
    }
}
