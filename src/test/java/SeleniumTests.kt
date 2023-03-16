import java.io.File
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
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.events.EventFiringDecorator
import org.openqa.selenium.support.events.WebDriverListener


class SeleniumTests {

	val options = ChromeOptions().addArguments("--remote-allow-origins=*")
	val listener = object : WebDriverListener {
		override fun beforeAccept(alert: Alert) {
			println("Text of accepted alert is: ${alert.text}")
		}
	}
	val driver = EventFiringDecorator<WebDriver>(listener).decorate(ChromeDriver(options))

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

	@After
	fun tearDown() {
		driver.quit()
	}
}
