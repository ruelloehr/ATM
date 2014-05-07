package com.ruel.atm;

import java.math.BigDecimal;

/**
 * A bank account
 */
public class Account {

    private int pin;     //this is acting as the unique identifier for a user
    private BigDecimal balance;
    private String firstName;
    private String lastName;

    //other things that we would need for further implementation
    //a list of transactions
    //email address
    //daily limit

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Check if a account has sufficient funds for withdraw
     * @param withDrawAmount
     * @return
     */
    protected boolean hasSufficientFundsForWithDraw(BigDecimal withDrawAmount) {
        if ((this.getBalance().subtract(withDrawAmount)).compareTo(BigDecimal.ZERO) > 0)
            return true;
        else
            return false;
    }

    /**
     * Debit from the account
     * @param withDrawAmount
     */
    protected void debitAccount(BigDecimal withDrawAmount) {
        this.setBalance(this.getBalance().subtract(withDrawAmount));
    }

    /**
     * Deposit money into the account
     * @param depositAmount
     */
    protected void depositAccount(BigDecimal depositAmount) {
        this.setBalance(this.getBalance().add(depositAmount));
    }

}
