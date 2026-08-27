package testCases;

import java.io.InputStream;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;


public class TC_002_LoginTest extends BaseClass
{

//	@Test(groups= {"Sanity","Master"})
//	public void verify_login() throws InterruptedException
//	{
//	    logger.info("**** Starting TC_002_LoginTest  ****");
//
//	    HomePage hp=new HomePage(driver);
//	    hp.clickMyAccount();
//	    hp.clickLogin();
//
//	    Thread.sleep(5000);
//
//	    LoginPage lp=new LoginPage(driver);
//	    lp.setEmail(p.getProperty("email"));
//	    lp.setPassword(p.getProperty("password"));
//	    lp.clickLogin();
//
//	    Thread.sleep(5000);
//
//	    MyAccountPage macc=new MyAccountPage(driver);
//	    boolean targetPage=macc.isMyAccountPageExists();
//
//	    // This will FAIL if false
//	    Assert.assertTrue(targetPage, "Login failed");
//
//	    logger.info("**** Finished TC_002_LoginTest  ****");
//	}
	@Test(groups= {"Sanity","Master"})
	public void verify_login()
	{
		
		logger.info("**** Starting TC_002_LoginTest  ****");
		logger.debug("capturing application debug logs....");
		try
		{
		//Home page
		HomePage hp=new HomePage(driver);
		hp.clickMyAccount();
		logger.info("clicked on myaccount link on the home page..");
		hp.clickLogin(); //Login link under MyAccount
		Thread.sleep(5000);
		logger.info("clicked on login link under myaccount..");
		
		//Login page
		LoginPage lp=new LoginPage(driver);
		logger.info("Entering valid email and password..");
		//System.out.println("Vishal");
		System.out.println("test vishal "+p.getProperty("email"));
		//lp.setEmail();
		lp.setEmail(p.getProperty("email"));
		Thread.sleep(5000);
		//lp.setPassword();
		System.out.println("test vishal "+p.getProperty("password"));
		lp.setPassword(p.getProperty("password"));
		lp.clickLogin(); //Login button
		Thread.sleep(5000);
		logger.info("clicked on ligin button..");
		
		//My Account Page
		MyAccountPage macc=new MyAccountPage(driver);
				
		boolean targetPage=macc.isMyAccountPageExists();
		
		Assert.assertEquals(targetPage, true,"Login failed");
		}
		catch(Exception e)
		{
			Assert.fail();
		}
		
		logger.info("**** Finished TC_002_LoginTest  ****");
	}
}
