import java.io.File
import java.lang.Thread.sleep
import java.net.URL
import java.nio.file.Files.write
import java.util.concurrent.TimeUnit
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.OutputType.BYTES
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.events.EventFiringDecorator
import org.openqa.selenium.support.events.WebDriverListener


class SeleniumTests {

	val username = System.getenv("LT_USERNAME")
	val accessToken = System.getenv("LT_ACCESS_KEY")
	val host = URL("https://${username}:${accessToken}@hub.lambdatest.com/wd/hub")!!

	val options = labmdaTest()

	fun labmdaTest(): ChromeOptions{
		val option = ChromeOptions()
		option.setPlatformName("Windows 10")
		option.setCapability("browserVersion", "110.0")
		val ltOptions: HashMap<String, Any> = HashMap<String, Any>()
		ltOptions["username"] = username
		ltOptions["accessKey"] = accessToken
		ltOptions["project"] = "Untitled"
		ltOptions["selenium_version"] = "4.0.0"
		ltOptions["w3c"] = true
		option.setCapability("LT:Options", ltOptions)
		return option
	}


//	val options = optionSetUp()
//	fun optionsSetUp(): ChromeOptions {
//		val options = ChromeOptions().addArguments("--remote-allow-origins=*")
//		options.setCapability("browserName", "chrome")
//		//options.setCapability("platformName", "MAC")
//		options.setCapability("se:name", "My simple test")
//		options.setCapability("se:sampleMetadata", "Sample metadata value")
//		options.setPlatformName("MAC")
//		return options
//	}

	fun optionSetUp(): ChromeOptions {
		val options = ChromeOptions().addArguments("--remote-allow-origins=*")
		options.setCapability("browserName", "chrome")
		options.setCapability("browserVersion", "110.0")
		return options
	}


//	val chromeOptions = ChromeOptions().apply {
//		addArguments("--remote-allow-origins=*")
//		setCapability("pageLoadStrategy", "eager")
//	}

	val listener = object : WebDriverListener {
		override fun beforeAccept(alert: Alert) {
			println("Text of accepted alert is: ${alert.text}")
		}
	}

	val driver = EventFiringDecorator<WebDriver>(listener).decorate(RemoteWebDriver(host, options))
	//val driver = EventFiringDecorator<WebDriver>(listener).decorate(ChromeDriver(chromeOptions))

	@Before
	fun setUp() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
	}

	@Test
	fun test() {
		driver.navigate().to("https://phptravels.com/demo")
		val demoPage = DemoPage(driver)
		demoPage.firstName.sendKeys("MyTestName")
		demoPage.lastName.sendKeys("MyLastTestName")
		demoPage.email.sendKeys("testemail@email.com")
		demoPage.submitButton.click()
		var alert = driver.switchTo().alert()
		println(alert.text)
		alert.accept()
		demoPage.companyName.sendKeys("Company")
		demoPage.submitButton.click()
		alert = driver.switchTo().alert()
		println(alert.text)
		alert.accept()
		demoPage.captchaInput.sendKeys(demoPage.calculateCaptcha().toString())
		demoPage.submitButton.click()
		Assert.assertTrue("Completed sign is present", demoPage.completedCheckmark.size > 0)
		driver.findElement(By.cssSelector("body")).sendKeys(Keys.chord(Keys.COMMAND, "A"))
		val file = (driver as TakesScreenshot).getScreenshotAs(BYTES)
		val screenshotFile = File("screenshot.png")
		write(screenshotFile.toPath(), file)
	}

	@Test
	fun directFormSubmission() {
		driver.navigate().to("https://phptravels.com/demo")
		val demoPage = DemoPage(driver)
		demoPage.submitForm()
		Assert.assertTrue("Completed sign is present", demoPage.completedCheckmark.size > 0)
	}

	@Test
	fun tableInteraction() {
		driver.navigate().to("https://www.techlistic.com/p/demo-selenium-practice.html")
		val tablePage = TablePage(driver)
		Assert.assertTrue("Taipei" == tablePage.dynamicElement("Taipei 101", "City")?.text)
		sleep(1000)
	}

	@After
	fun tearDown() {
		driver.quit()
	}
}
