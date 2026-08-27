package testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.apache.commons.text.RandomStringGenerator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.apache.logging.log4j.LogManager;//log4j
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import java.nio.file.Files;


public class BaseClass {

	public WebDriver driver;
	public Logger logger;
	public Properties p;
	
	@BeforeClass(groups = { "Master", "Sanity", "Regression" })
	@Parameters({"os", "browser"})
	//@Parameters("browser")
	//public void setup(String os,String br) throws IOException
	public void setup(@Optional("windows") String os,
            @Optional("chrome") String br) throws IOException
	//public void setup() throws IOException
	//public void setup(String br) throws IOException
	{
		 p = new Properties();
//		 p = new Properties();
//
//		    FileInputStream file = new FileInputStream(System.getProperty("user.dir") 
//		            + ".//src//test//resources//config.properties");
//
//		    p.load(file);

		try {
		    InputStream input = getClass()
		            .getClassLoader()
		            .getResourceAsStream("config.properties");

		    if (input == null) {
		        throw new RuntimeException("config.properties not found in classpath");
		    }

		    p.load(input);

		} catch (Exception e) {
		    e.printStackTrace();
		}
		 
		logger=LogManager.getLogger(this.getClass());
		
		if(p.getProperty("execution_env").equalsIgnoreCase("remote"))
		{
			 URL gridUrl = new URL("http://localhost:4444");

			    if(br.equalsIgnoreCase("chrome"))
			    {
			        ChromeOptions options = new ChromeOptions();
			        options.setPlatformName(os);
			        driver = new RemoteWebDriver(gridUrl, options);
			    }
			    else if(br.equalsIgnoreCase("edge"))
			    {
			        EdgeOptions options = new EdgeOptions();
			        options.setPlatformName(os);
			        driver = new RemoteWebDriver(gridUrl, options);
			    }
			    else
			    {
			        logger.error("Invalid browser for grid");
			        return;
			    }
			    
			}
			
			//browser
//			switch(br.toLowerCase())
//			{
//			case "chrome": capabilities.setBrowserName("chrome"); break;
//			case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
//			default: System.out.println("No matching browser"); return;
//			}
//			
//			driver=new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),capabilities);
		//}
		
				
		if(p.getProperty("execution_env").equalsIgnoreCase("local"))
		{

			switch(br.toLowerCase())
			{
			case "chrome" : driver=new ChromeDriver(); break;
			case "edge" : driver=new EdgeDriver(); break;
			case "firefox": driver=new FirefoxDriver(); break;
			default : System.out.println("Invalid browser name.."); return;
			}
		}
		
			
		
		 //for config file
//		String b = p.getProperty("Browser").toLowerCase();
//		switch(b)
//		{
//		case "chrome": 
//				driver=new ChromeDriver(); 
//				break;
//		case "edge": 
//				driver=new EdgeDriver(); 
//				break;
//		default: 
//				System.out.println("No matching browser..");
//				return;
//		}
		//for parallel execution 
//		if(br.equals("chrome"))
//		{
//			driver=new ChromeDriver();
//		}
//		else if(br.equals("edge"))
//		{
//			driver=new EdgeDriver(); 
//		}
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		driver.get(p.getProperty("appURL"));
		driver.manage().window().maximize();
	}
	
	@AfterClass (groups = { "Master", "Sanity", "Regression" })
	public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

	public String randomeString()
	{
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
		        .withinRange('a', 'z')
		        .get();
		String randomLetters = generator.generate(10);
		return randomLetters;
	}
	
	public String randomeNumber()
	{
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
			    .withinRange('0', '9')
			    .get();
			String numericString = generator.generate(10);
			return numericString;
	}
	
	public String randomAlphaNumeric()
	{
		RandomStringGenerator generator = new RandomStringGenerator.Builder()
		        .withinRange('a', 'z')
		        .get();
		String randomLetters = generator.generate(3);
		RandomStringGenerator generatorn = new RandomStringGenerator.Builder()
			    .withinRange('0', '9')
			    .get();
			String numericString = generatorn.generate(3);
		//String str=RandomStringUtils.randomAlphabetic(3);
		//String num=RandomStringUtils.randomNumeric(3);
		
		return (randomLetters+"@"+numericString);
	}
	public String captureScreen(String testName) throws IOException
	{
	    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

	    String path = System.getProperty("user.dir") + "\\screenshots\\" + testName + "_" + timeStamp + ".png";

	    TakesScreenshot ts = (TakesScreenshot) driver;
	    File source = ts.getScreenshotAs(OutputType.FILE);

	    File target = new File(path);
	    target.getParentFile().mkdirs(); // create folder

	    Files.copy(source.toPath(), target.toPath());

	    return path;
	}
}
