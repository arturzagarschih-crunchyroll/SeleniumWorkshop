import java.time.Duration
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class DemoPage(private val driver: WebDriver) {

	private val regex = "(?<val1>\\d+)\\s+(?<sign>[+\\-*/]+)\\s+(?<val2>\\d+)".toRegex()

	val login by lazy { driver.findElement(By.cssSelector("[data-name='login']")) }

	val firstName by lazy { driver.findElement(By.name("first_name")) }

	val lastName = driver.findElement(By.name("last_name"))

	val companyName = driver.findElement(By.name("business_name"))

	val email = driver.findElement(By.name("email"))

	val submitButton = driver.findElement(By.id("demo"))

	private val CAPTCHA_BY = By.cssSelector("div[class*='row fcr'] h2")
	val captcha = driver.findElement(CAPTCHA_BY)

	val captchaInput = driver.findElement(By.id("number"))

	val completedCheckmark = driver.findElements(By.xpath("//div[@class='completed']"))

	fun calculateCaptcha() {
		val expression = captcha.text.replace("=", "")
		val result = (driver as JavascriptExecutor).executeScript("return eval(arguments[0])", expression)
		println(result)
		//or
		//(driver as JavascriptExecutor).executeScript("eval(document.querySelector('div[class*='row fcr'] h2').textContent.replace(\"=\", \"\"))")
	}

	fun submitForm() {
		val ac = Actions(driver)
		WebDriverWait(driver, Duration.ofSeconds(10))
			.until(ExpectedConditions.textMatches(CAPTCHA_BY, regex.toPattern()))
		val captcha = calculateCaptcha().toString()
		ac
			.moveToElement(firstName).sendKeys("MyTestName")
			.moveToElement(lastName).sendKeys("MyLastTestName")
			.moveToElement(companyName).sendKeys("Company")
			.moveToElement(email).sendKeys("testemail@email.com")
			.moveToElement(captchaInput).sendKeys(captcha)
			.moveToElement(submitButton).click().build().perform()
	}
}
