import java.time.Duration
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

class DemoPage(private val driver: WebDriver) {

	private val regex = "(?<val1>\\d+)\\s+(?<sign>[+\\-*/]+)\\s+(?<val2>\\d+)".toRegex()

	val firstName by lazy { driver.findElement(By.name("first_name")) }

	val lastName = driver.findElement(By.name("last_name"))

	val companyName = driver.findElement(By.name("business_name"))

	val email = driver.findElement(By.name("email"))

	val submitButton = driver.findElement(By.id("demo"))

	private val CAPTCHA_PATH = By.xpath("//div[contains(@class, 'row fcr')]//h2")
	val captcha = driver.findElement(CAPTCHA_PATH)

	val captchaInput = driver.findElement(By.id("number"))

	val completedCheckmark = driver.findElements(By.xpath("//div[@class='completed']"))

	fun calculateCaptcha(): Int {
		val captchaValue = captcha.text

		println(captcha.tagName)
		val match = regex.find(captcha.text)!!
		val val1 = match.groups["val1"]!!.value.toInt()
		val val2 = match.groups["val2"]!!.value.toInt()
		val sign = match.groups["sign"]!!.value

		println(val1)
		println(val2)
		println(sign)
		val captchaResult = when (sign) {
			"+" -> val1 + val2
			"-" -> val1 - val2
			"*" -> val1 * val2
			"/" -> val1 / val2
			else -> {
				throw NumberFormatException()
			}
		}
		println(captchaResult)
		return captchaResult
	}

	fun submitForm() {
		val ac = Actions(driver)
		WebDriverWait(driver, Duration.ofSeconds(10))
			.until(ExpectedConditions.textMatches(CAPTCHA_PATH, regex.toPattern()))
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
