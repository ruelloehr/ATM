package com.ruel.atm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rloehr on 5/7/14.
 */
public class AccountStore {

    private List<Account> accounts;

    public AccountStore() {

        Account account1 = new Account();
        account1.setBalance(new BigDecimal(512.13));
        account1.setPin(1234);
        account1.setFirstName("Elvis");
        account1.setLastName("Presley");

        Account account2 = new Account();
        account2.setBalance(new BigDecimal(125.93));
        account2.setPin(5678);
        account2.setFirstName("James");
        account2.setLastName("Dean");

        Account account3 = new Account();
        account3.setBalance(new BigDecimal(4));
        account3.setPin(1111);
        account3.setFirstName("Huck");
        account3.setLastName("Finn");


        accounts = new ArrayList<Account>();

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    protected Account getAccountByPin(int pin) {

        //TODO:  this would be replaced with a db lookup, O(n) isn't the way to go
        for (Account curAccount : accounts)
        {

            if (curAccount.getPin() == pin)
            {
                return curAccount;
            }
        }

       return null;

    }


}
