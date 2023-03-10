import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class SeleniumTests {

	val options = ChromeOptions().addArguments("--remote-allow-origins=*")
	private val driver = ChromeDriver(options)

	@Test
	fun test() {
		driver.get("https://phptravels.com/demo")
		driver.findElement(By.name("first_name")).sendKeys("MyTestName")
		driver.findElement(By.name("last_name")).sendKeys("MyLastTestName")
		driver.findElement(By.name("email")).sendKeys("testemail@email.com")
		driver.findElement(By.id("demo")).click()
		var alert = driver.switchTo().alert()
		println(alert.text)
		alert.accept()
		driver.findElement(By.name("business_name")).sendKeys("Company")
		driver.findElement(By.id("demo")).click()
		alert = driver.switchTo().alert()
		println(alert.text)
		alert.accept()
		val capcha = driver.findElement(By.xpath("//div[contains(@class, 'row fcr')]//h2")).getText().trim() //text
		println(capcha)
		//val regex = "(\\d+)\\s+([+\\-*/]+)\\s+(\\d+)".toRegex()
		val regex = "(?<val1>\\d+)\\s+(?<sign>[+\\-*/]+)\\s+(?<val2>\\d+)".toRegex()
		val match = regex.find(capcha)!!
		val val1 = match.groups["val1"]!!.value.toInt()
		val val2 = match.groups["val2"]!!.value.toInt()
		val sign = match.groups["sign"]!!.value

		println(val1)
		println(val2)
		println(sign)
		val captcha = when (sign) {
			"+" -> val1 + val2
			"-" -> val1 - val2
			"*" -> val1 * val2
			"/" -> val1 / val2
			else -> {
				throw NumberFormatException()
			}
		}
		println(captcha)
		driver.findElement(By.id("number")).sendKeys(captcha.toString())
		driver.findElement(By.id("demo")).click()

		Assert.assertTrue("Completed sign is present", driver.findElements(By.xpath("//div[@class='completed']")).size > 0)
	}

	@After
	fun tearDown() {
		driver.quit()
	}
}
