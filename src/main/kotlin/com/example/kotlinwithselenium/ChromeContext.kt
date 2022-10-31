package com.example.kotlinwithselenium

import org.openqa.selenium.By
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebDriver.Navigation
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.stereotype.Component
import java.awt.SystemColor.text
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

@Component
class ChromeContext {

    fun start() {


        val webDriverID = "webdriver.chrome.driver"
        val webDriverPath = "./driver/chrome/chromedriver.exe"
        System.setProperty(webDriverID, webDriverPath)

        val chromeOptions = ChromeOptions()
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL)
        chromeOptions.addArguments("start-maximized")

        val driver = ChromeDriver(chromeOptions)

        driver.get("https://www.snlib.go.kr/bd/menu/10265/program/30004/plusSearchKdc.do")
        TimeUnit.SECONDS.sleep(1L)

        // #kdcDepth2List_0 a
        val categories = driver.findElements(By.cssSelector("#kdcDepth2List_0 a"))

        categories.forEach {

            driver.enhancedClickAndWait(it)

            IterateWebElements(
                driver = driver,
                cssSelector = "p.paging a:not([class]),p.paging span.current",
                consumer = { pageNode ->

                    println("페이지 이동 (index = ${pageNode.index})")

                    val iterOnPageResult = IterateWebElements(driver,
                        cssSelector = "dl.bookDataWrap dt a",
                        consumer = { pageResultNode ->
                            val title: String = driver.findElement(By.cssSelector(".resultViewDetail .tit h4")).text
                            println("INDEX = ${pageResultNode.index}, 제목 = ${title.trim()}")
                        }, callBack = {
                            // 네비게이션만 조작하면 된다
                            driver.navigateAndWait { navigation -> navigation.back() }
                        })

                    iterOnPageResult.iter()
                },
                callBack = { node -> if (node.next != null) driver.enhancedClickAndWait(node.next) }
            ).iter()

        }
    }

}

class IterateWebElements(
    val driver: RemoteWebDriver,
    val cssSelector: String,
    val consumer: Consumer<IterationWebElementNode>,
    val callBack: Consumer<IterationWebElementNode>
) {

    fun iter() {
        val size = driver.findElements(By.cssSelector(cssSelector)).size

        for (index in 0 until size) {
            val creatNode: () -> IterationWebElementNode = {
                IterationWebElementNode(index, findByIndex(index), if (index < size - 2) findByIndex(index + 1) else null)
            }

            driver.enhancedClick(creatNode.invoke().current)
            TimeUnit.SECONDS.sleep(1L)

            consumer.accept(creatNode.invoke())
            callBack.accept(creatNode.invoke())
        }
    }

    private fun findByIndex(index: Int): WebElement {
        val webElements = driver.findElements(By.cssSelector(cssSelector))
        return if (webElements.isEmpty()) EmptyRemoteWebElement() else webElements[index]
    }

}

class IterationWebElementNode(
    val index: Int = -1,
    val current: WebElement = EmptyRemoteWebElement(),
    val next: WebElement? = null,
)

class EmptyRemoteWebElement : RemoteWebElement() {}

val WebElement.innerHTML: String
    get() = this.getAttribute("innerHTML")
val WebElement.outerHTML: String
    get() = this.getAttribute("outerHTML")


fun RemoteWebDriver.navigateAndWait(cssSelector: String = "body", waitForSecond: Long = 10, navigate: Consumer<Navigation>) {
    navigate.accept(this.navigate())
    WebDriverWait(this, Duration.of(waitForSecond, ChronoUnit.SECONDS))
        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)))
}

fun RemoteWebDriver.enhancedClick(element: WebElement) {
    this.executeScript("arguments[0].click()", element)
}

fun RemoteWebDriver.enhancedClickAndWait(element: WebElement, cssSelector: String = "body", waitForSecond: Long = 10) {
    enhancedClick(element)
    WebDriverWait(this, Duration.of(waitForSecond, ChronoUnit.SECONDS))
        .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)))
}

fun WebElement.enhancedClick(driver: RemoteWebDriver) {
    driver.executeScript("arguments[0].click()", this)
}

data class Book(
    private val title: String,
    private val writer: String,

)