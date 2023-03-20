import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class TablePage(private val driver: WebDriver) {

	fun dynamicElement(rowName: String, columnName: String): WebElement? {
		return driver.findElement(By.xpath("//table[@summary='Sample Table']//tr[./th[contains(text(),'$rowName')]]/td[count(//table[@summary='Sample Table']//tr/th[.='$columnName'][1]/preceding-sibling::th)]"))
		//table[@summary='Sample Table']//tr[./th[contains(text(),'Taipei 101')]]/td[count(//table[@summary='Sample Table']//tr/th[.='City'][1]/preceding-sibling::th)]
		//table[@summary=‘Sample Table’]//tr[./th[contains(text(),‘Taipei 101’)]]/td[count(//table[@summary=‘Sample Table’]//tr/th[.=‘City’][1]/preceding-sibling::th)]
	}
}
