package uk.ac.ed.inf.order;

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
