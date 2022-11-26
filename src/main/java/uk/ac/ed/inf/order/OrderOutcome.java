package uk.ac.ed.inf.order;

/**
 * enum to store all the possible order outcomes.
 */
public enum OrderOutcome {
    Delivered ,
    ValidButNotDelivered ,
    InvalidCardNumber ,
    InvalidExpiryDate ,
    InvalidCvv ,
    InvalidTotal ,
    InvalidPizzaNotDefined ,
    InvalidPizzaCount ,
    InvalidPizzaCombinationMultipleSuppliers ,
    Invalid

}
