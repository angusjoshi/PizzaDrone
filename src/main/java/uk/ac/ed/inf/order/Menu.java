package uk.ac.ed.inf.order;


/**
 * record for storing a menu entry for a restaurant
 * @param name name of the item on the menu
 * @param priceInPence price in pence of the item
 */
public record Menu(String name, int priceInPence) {
}
