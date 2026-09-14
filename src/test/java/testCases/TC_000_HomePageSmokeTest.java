package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import testBase.BaseClass;

/**
 * Lightweight smoke checks for CI — proves browser + Grid + app reachability.
 */
public class TC_000_HomePageSmokeTest extends BaseClass {

	@Test(groups = { "Master", "Sanity", "Regression" })
	public void verifyHomePageLoads() {
		logger.info("**** Starting TC_000_HomePageSmokeTest ****");
		String title = driver.getTitle();
		String url = driver.getCurrentUrl();
		logger.info("Title={}, URL={}", title, url);
		Assert.assertNotNull(title, "Page title should not be null");
		Assert.assertFalse(title.isBlank(), "Page title should not be blank");
		Assert.assertTrue(
				url.contains("tutorialsninja") || title.toLowerCase().contains("store"),
				"Unexpected home page. title=" + title + " url=" + url);
		logger.info("**** Finished TC_000_HomePageSmokeTest ****");
	}
}
