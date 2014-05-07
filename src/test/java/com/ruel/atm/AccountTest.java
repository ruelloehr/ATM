package com.ruel.atm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by rloehr on 5/7/14.
 */
public class AccountTest {

    Account account;

    @Before
    public void setup() {
        account = new Account();
        account.setBalance(new BigDecimal(124.56));
    }


    @Test
    public void testSufficientFundsForWithdraw() {

        Assert.assertFalse(account.hasSufficientFundsForWithDraw(new BigDecimal(150)));
        Assert.assertTrue(account.hasSufficientFundsForWithDraw(new BigDecimal(124.55)));
        Assert.assertTrue(account.hasSufficientFundsForWithDraw(new BigDecimal(12)));
    }

    @Test
    public void testDebitAccount() {
        account.debitAccount(new BigDecimal(110.12));
        Assert.assertEquals(new BigDecimal(14.44).round(new MathContext(2, RoundingMode.HALF_UP)), account.getBalance().round(new MathContext(2, RoundingMode.HALF_UP)));
    }

    @Test
    public void testDepositAccount() {
        account.depositAccount(new BigDecimal(110.12));
        Assert.assertEquals(new BigDecimal(234.68).round(new MathContext(2, RoundingMode.HALF_UP)), account.getBalance().round(new MathContext(2, RoundingMode.HALF_UP)));

    }

}
