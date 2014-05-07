package com.ruel.atm;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

/**
 * A basic implementation of an ATM
 * The atm is initialized with a fixed set of users
 * User pins are:  1111, 1234, 5678
 *
 * The supported use cases are:
 *
 * 1)  Get balance
 * 2)  Deposit Cash
 * 3)  Withdrawl Cash
 * 4)  Enter pin
 *
 * Discussion points for additional functionality:
 * 1) Receipts
 * 2) Different functionality by banks
 * 3) Account transactions
 * 4) Dealing with an actual card
 * 5) Deposit envelopes - verify that a deposit envelope was place in machine
 * 6) Deposit pending - don't credit the deposit immediately, put it in a pending state
 * 6) Timeouts - guava has a nice library for this
 *
 */
public abstract class ATM {

    //an atm has a reference to a list of accounts in  a datastore
    //in the real world, this would be in a peristent database store
    protected AccountStore accountStore;

    //the current user of the atm, we use this to handle any monetary changes and querying the balance
    //ideally it would be a service decoupled from the implementation of the atm itself
    protected Account currentAccount;

    //the amount of money the atm has to dispense
    protected int currencyInATM;

    protected String bankName = "Abstract ATM";    //TODO:   if the bank had many rules (e.g daily withdraw limits) we would pull it out into its own entity

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public int getCurrencyInATM() {
        return currencyInATM;
    }

    public void setCurrencyInATM(int currencyInATM) {
        this.currencyInATM = currencyInATM;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }


    /**
     * Get the account store that the atm uses
     * @return
     */
    public AccountStore getAccountStore() {
        return accountStore;
    }

    /**
     * Set the account store that the atm uses
     * @param accountStore
     */
    public void setAccountStore(AccountStore accountStore) {
        this.accountStore = accountStore;
    }


    /**
     * Initialize the atm by creating an account store and setting the amount of money that is in the atm
     * @param startingCashValue
     */
    public void init(int startingCashValue) {
        this.accountStore = new AccountStore();
        this.currencyInATM = startingCashValue;
    }

    /**
     * Display a welcome message to to the user
     */
    protected void displayWelcomeScreen() {
        System.out.println("");
        System.out.println("Welcome to "  + this.bankName);   //make generic, get a bank object and get its name
    }

    /**
     * Get a pin number for a user
     * @return
     */
    protected int getPinNumber() {
        Scanner keyboard = new Scanner(System.in);
        int pin = 0;

        System.out.println("Please type your pin number and press enter to proceed");

        try {
            pin = keyboard.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("The pin must be a number value.  Text is not allowed");
            pin = this.getPinNumber();
        }

        return pin;
    }

    /**
     * Get an account based on its pin
     * @param pin
     * @return
     */
    protected Account getAccountByPin(int pin) {

        return this.getAccountStore().getAccountByPin(pin);

    }

    /**
     * Greet the user with there name
     * @param account
     */
    protected void greetUser(Account account) {

        System.out.println("Welcome " + account.getFirstName() + " " + account.getLastName());

    }

    /**
     * Display a list of options to the user
     */
    protected void displayOptions() {

        System.out.println("1. Show Balance");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Make a deposit");
        System.out.println("4. Exit");

    }

    /**
     * Get user input from the console
     * Ensure that it is not text and has a positive integer value
     * @return
     */
    protected int getUserInput() {
        Scanner keyboard = new Scanner(System.in);
        int choice = 0;

        try {
            choice = keyboard.nextInt();

            if (choice < 0) {
                System.out.println("The value entered cannot be negative");
                choice = this.getUserInput();
            }


        } catch (InputMismatchException e) {
            System.out.println("The input must be a number value.  Text is not allowed");
            choice = this.getUserInput();
        }

        return choice;
    }

    /**
     * Show a users account balance
     * @param account
     */
    protected void showBalance(Account account) {
        NumberFormat usdCostFormat = NumberFormat.getCurrencyInstance(Locale.US);
        usdCostFormat.setMinimumFractionDigits(2);
        usdCostFormat.setMaximumFractionDigits(2);
        System.out.println("Your balance is " + usdCostFormat.format(account.getBalance().doubleValue()));

    }

    /**
     * Logout a user
     * @param account
     */
    protected void exitUser(Account account) {

        this.currentAccount = null;

    }

    /**
     * Deposit cash operation for the atm
     * @param account
     */
    protected void depositCash(Account account) {

        System.out.println("Please enter the amount you wish to deposit.   We only accept full dollar amounts and do not accept change");
        int depositAmount = getUserInput();
        account.depositAccount(new BigDecimal(depositAmount));


    }

    /**
     * Withdraw cash operation for the atm
     * @param account
     */
    protected void withdrawCash(Account account) {
        System.out.println("\n Please select a withdraw amount");
        System.out.println("1. $20.00");
        System.out.println("2. $40.00");
        System.out.println("3. $60.00");
        System.out.println("4. $80.00");

        int choice = this.getUserInput();
        int withDrawAmount = 0;

        switch (choice) {
            case 1:
                withDrawAmount=20;
                break;
            case 2:
                withDrawAmount=40;
                break;
            case 3:
                withDrawAmount=60;
                break;
            case 4:
                withDrawAmount=80;
                break;
            //TODO: allow arbritary whole dollar values up to the users daily withdraw limit
        }

        //in order to do a withdraw the customer must have enough funds in their account and the atm must have enough currency

        if (!this.hasSufficientFundsForWithDraw(account, withDrawAmount))
        {
            System.out.println("There are insufficient funds in the account to withdraw from");
        }
        else if (!this.ATMHasSufficientFundsForWithdraw(withDrawAmount)) {
            System.out.println("Unable to service your request at this time");
        }
        else
        {
            this.withDrawFromAccount(account, withDrawAmount);
            this.currencyInATM = this.currencyInATM - withDrawAmount;
            //TODO:  at this point we would dispense cash, we would want to ensure the user physically took the money
            //TODO:  we would then print or email a receipt
        }

    }

    /**
     * Check if the atm has enough money to do a withdraw
     * @param withDrawAmount
     * @return
     */
    protected boolean ATMHasSufficientFundsForWithdraw(int withDrawAmount) {
        if (this.currencyInATM - withDrawAmount > 0)
            return true;
        else
            return false;
    }

    /**
     * Withdraw money from an account
     * @param account
     * @param withDrawAmount
     */
    protected void withDrawFromAccount(Account account, int withDrawAmount) {

        account.debitAccount(new BigDecimal(withDrawAmount));

    }

    /**
     * Check if an account has sufficient funds for a withdraw
     * @param account
     * @param withDrawAmount
     * @return
     */
    protected boolean hasSufficientFundsForWithDraw(Account account, int withDrawAmount) {

        return account.hasSufficientFundsForWithDraw(new BigDecimal(withDrawAmount));
    }


    /**
     * Run the atm
     */
    public void run() {
        int pin = 0;
        this.init(5000);  //this atm has 5000 dollars


        //when no user is logged in, we should prompt to enter pin
        while (this.currentAccount == null) {

            int choice = 0;
            this.displayWelcomeScreen();
            pin = this.getPinNumber();

            //while there is not a valid account, we prompt and get the pin
            while (this.getAccountByPin(pin) == null)
            {
                System.out.println("An account could not be retrieved with the given pin number.");
                pin = this.getPinNumber();
            }

            //get the account associated with the given pin number
            this.currentAccount = this.getAccountByPin(pin);

            while (choice != 4) {
                this.greetUser(this.currentAccount);
                this.displayOptions();
                choice = this.getUserInput();

                switch (choice) {
                    case 1: this.showBalance(this.currentAccount);
                        break;
                    case 2: this.withdrawCash(this.currentAccount);
                        break;
                    case 3: this.depositCash(this.currentAccount);
                        break;
                    case 4:
                        this.exitUser(this.currentAccount);
                        break;


                }
            }

        }

        System.exit(0);


    }

}
