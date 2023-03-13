import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class DemoPage(driver: WebDriver) {

	val firstName = driver.findElement(By.name("first_name"))

	val lastName = driver.findElement(By.name("last_name"))

	val companyName = driver.findElement(By.name("business_name"))

	val email = driver.findElement(By.name("email"))

	val submitButton = driver.findElement(By.id("demo"))

	val captcha = driver.findElement(By.xpath("//div[contains(@class, 'row fcr')]//h2"))

	val captchaInput = driver.findElement(By.id("number"))

	val completedCheckmark = driver.findElements(By.xpath("//div[@class='completed']"))

	fun calculateCaptcha(): Int {
		val captchaValue = captchaInput.text
		//val regex = "(\\d+)\\s+([+\\-*/]+)\\s+(\\d+)".toRegex()
		val regex = "(?<val1>\\d+)\\s+(?<sign>[+\\-*/]+)\\s+(?<val2>\\d+)".toRegex()
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
		firstName.sendKeys("MyTestName")
		lastName.sendKeys("MyLastTestName")
		companyName.sendKeys("Company")
		email.sendKeys("testemail@email.com")
		captchaInput.sendKeys(calculateCaptcha().toString())
		submitButton.click()
	}
}
