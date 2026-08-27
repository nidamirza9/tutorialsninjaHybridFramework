package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//input[@id='input-email']")
	WebElement txtEmailAddress;

	@FindBy(xpath = "//input[@id='input-password']")
	WebElement txtPassword;

	@FindBy(xpath = "//input[@value='Login']")
	WebElement btnLogin;


//	public void setEmail() {
//		txtEmailAddress.sendKeys("patildaksha7@gmail.com");
//	}
	public void setEmail(String email) {
		txtEmailAddress.sendKeys(email);
	}

//	public void setPassword() {
//		txtPassword.sendKeys("Dakshu@20");
//	}
	public void setPassword(String password) {
		txtPassword.sendKeys(password);
		
	}
	public void clickLogin() {
		btnLogin.click();
	}

	

}
