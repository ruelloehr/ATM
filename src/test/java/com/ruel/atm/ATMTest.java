package com.ruel.atm;

import com.ruel.atm.WellsFargoATM;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by rloehr on 5/7/14.
 */
public class ATMTest {

    WellsFargoATM wfatm = null;


    @Before
    public void setup() {
        wfatm = new WellsFargoATM();
        wfatm.init(10000);
    }


    @Test
    public void testInitializiation() {

        Assert.assertEquals(10000, wfatm.getCurrencyInATM());

    }

    @Test
    public void testGetValidAccount() {
        Account userAccount = wfatm.getAccountByPin(1111);
        Assert.assertEquals("Huck", userAccount.getFirstName());
        Assert.assertEquals("Finn", userAccount.getLastName());
    }

    @Test
    public void testSufficientFundsInATM() {
        Assert.assertFalse(wfatm.ATMHasSufficientFundsForWithdraw(20000));
        Assert.assertTrue(wfatm.ATMHasSufficientFundsForWithdraw(8000));
    }
}
