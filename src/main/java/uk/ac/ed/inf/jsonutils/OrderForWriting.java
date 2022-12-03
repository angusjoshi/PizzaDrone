package uk.ac.ed.inf.jsonutils;

import uk.ac.ed.inf.order.OrderOutcome;

/**
 * A record for structuring the Order data for serialization
 * @param orderNo The order number
 * @param outcome The order outcome
 * @param costInPence The cost in pence (as received through the rest client, not recalculated)
 */
public record OrderForWriting(String orderNo, OrderOutcome outcome, int costInPence) {
}
