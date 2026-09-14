package testBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseClass {

	public WebDriver driver;
	public Logger logger;
	public Properties p;

	@BeforeClass(groups = { "Master", "Sanity", "Regression" })
	@Parameters({ "os", "browser" })
	public void setup(@Optional("linux") String os, @Optional("chrome") String br) throws IOException {
		p = new Properties();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("config.properties not found in classpath");
			}
			p.load(input);
		}

		logger = LogManager.getLogger(this.getClass());

		String executionEnv = firstNonBlank(
				System.getenv("EXECUTION_ENV"),
				System.getProperty("execution_env"),
				p.getProperty("execution_env"),
				"local");

		boolean headless = Boolean.parseBoolean(firstNonBlank(
				System.getenv("HEADLESS"),
				System.getProperty("headless"),
				p.getProperty("headless"),
				"false"));

		String gridUrl = firstNonBlank(
				System.getenv("SELENIUM_REMOTE_URL"),
				System.getProperty("selenium.grid.url"),
				p.getProperty("selenium.grid.url"),
				"http://localhost:4444/wd/hub");

		if ("remote".equalsIgnoreCase(executionEnv)) {
			if (br.equalsIgnoreCase("chrome")) {
				ChromeOptions options = new ChromeOptions();
				options.setPlatformName(normalizePlatform(os));
				applyChromeOptions(options, headless);
				driver = new RemoteWebDriver(new URL(gridUrl), options);
			} else if (br.equalsIgnoreCase("edge")) {
				EdgeOptions options = new EdgeOptions();
				options.setPlatformName(normalizePlatform(os));
				if (headless) {
					options.addArguments("--headless=new", "--window-size=1920,1080");
				}
				driver = new RemoteWebDriver(new URL(gridUrl), options);
			} else if (br.equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				options.setPlatformName(normalizePlatform(os));
				if (headless) {
					options.addArguments("-headless");
				}
				driver = new RemoteWebDriver(new URL(gridUrl), options);
			} else {
				logger.error("Invalid browser for grid: {}", br);
				throw new IllegalArgumentException("Unsupported browser for remote: " + br);
			}
		} else {
			switch (br.toLowerCase()) {
			case "chrome":
				ChromeOptions chromeOptions = new ChromeOptions();
				applyChromeOptions(chromeOptions, headless);
				driver = new ChromeDriver(chromeOptions);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				if (headless) {
					edgeOptions.addArguments("--headless=new", "--window-size=1920,1080");
				}
				driver = new EdgeDriver(edgeOptions);
				break;
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				if (headless) {
					firefoxOptions.addArguments("-headless");
				}
				driver = new FirefoxDriver(firefoxOptions);
				break;
			default:
				throw new IllegalArgumentException("Invalid browser name: " + br);
			}
		}

		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(p.getProperty("appURL"));
		if (!headless) {
			driver.manage().window().maximize();
		}
	}

	@AfterClass(groups = { "Master", "Sanity", "Regression" })
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	private static void applyChromeOptions(ChromeOptions options, boolean headless) {
		options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
		if (headless) {
			options.addArguments("--headless=new", "--window-size=1920,1080");
		}
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		options.setExperimentalOption("prefs", prefs);
	}

	private static String normalizePlatform(String os) {
		if (os == null) {
			return "linux";
		}
		String value = os.trim().toLowerCase();
		if (value.startsWith("win")) {
			return "windows";
		}
		if (value.startsWith("mac")) {
			return "mac";
		}
		return "linux";
	}

	private static String firstNonBlank(String... values) {
		for (String value : values) {
			if (value != null && !value.trim().isEmpty()) {
				return value.trim();
			}
		}
		return "";
	}

	public String randomeString() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('a', 'z').get();
		return generator.generate(10);
	}

	public String randomeNumber() {
		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', '9').get();
		return generator.generate(10);
	}

	public String randomAlphaNumeric() {
		RandomStringGenerator letters = new RandomStringGenerator.Builder().withinRange('a', 'z').get();
		RandomStringGenerator numbers = new RandomStringGenerator.Builder().withinRange('0', '9').get();
		return letters.generate(3) + "@" + numbers.generate(3);
	}

	public String captureScreen(String testName) throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String path = System.getProperty("user.dir") + File.separator + "screenshots" + File.separator + testName + "_"
				+ timeStamp + ".png";

		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		File target = new File(path);
		target.getParentFile().mkdirs();
		Files.copy(source.toPath(), target.toPath());
		return path;
	}
}
