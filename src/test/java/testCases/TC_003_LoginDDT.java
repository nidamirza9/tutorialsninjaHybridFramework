package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;
import utilities.DataProviders;

public class TC_003_LoginDDT extends BaseClass {

    @Test(dataProvider = "LoginData", dataProviderClass = DataProviders.class)
    public void verify_loginDDT(String email, String password, String exp) {

        logger.info("**** Starting TC_003_LoginDDT *****");
        logger.info("Test Data -> Email: " + email + " | Password: " + password + " | Expected: " + exp);

        try {

     
            if ((email == null || email.trim().isEmpty()) ||
                (password == null || password.trim().isEmpty())) {

                logger.warn("Empty username or password detected");

                // Expected should be Invalid
                Assert.assertEquals(exp, "Invalid");
                return; // stop execution for this dataset
            }

            // Home page
            HomePage hp = new HomePage(driver);
            hp.clickMyAccount();
            hp.clickLogin();

            // Login page
            LoginPage lp = new LoginPage(driver);
            lp.setEmail(email);
            lp.setPassword(password);
            lp.clickLogin();

            // My Account Page
            MyAccountPage macc = new MyAccountPage(driver);
            boolean targetPage = macc.isMyAccountPageExists();

          
            if (exp.equalsIgnoreCase("Valid")) {

                if (targetPage) {
                    logger.info("Login successful (expected)");
                    macc.clickLogout();
                    Assert.assertTrue(true);
                } else {
                    logger.error("Login failed (unexpected)");
                    Assert.fail("Expected login success, but failed");
                }

            } else if (exp.equalsIgnoreCase("Invalid")) {

                if (targetPage) {
                    logger.error("Login successful (unexpected)");
                    macc.clickLogout();
                    Assert.fail("Expected login failure, but succeeded");
                } else {
                    logger.info("Login failed (expected)");
                    Assert.assertTrue(true);
                }
            }

        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage());
            Assert.fail("Test failed due to exception: " + e.getMessage());
        }

        logger.info("**** Finished TC_003_LoginDDT *****");
    }
}
