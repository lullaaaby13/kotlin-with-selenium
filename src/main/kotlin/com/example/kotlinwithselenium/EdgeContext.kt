package com.example.kotlinwithselenium

import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.springframework.stereotype.Component

@Component
class EdgeContext {

    fun start() {
        val webDriverID = "webdriver.edge.driver"
        val webDriverPath = "./edgedriver_win64/msedgedriver.exe";

        System.setProperty(webDriverID, webDriverPath)

        //엣지드라이버 옵션 설정하기
        val options: EdgeOptions = EdgeOptions()
        options.addArguments("--start-maximized")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-default-apps")
        // options.addArguments("headless")
        options.addArguments("log-level=0")

        //옵션이 적용된 엣지드라이버 불러오기
        val driver: EdgeDriver = EdgeDriver(options)

        try {
            // driver.get("https://www.snlib.go.kr/bd/menu/10265/program/30004/plusSearchKdc.do")
            driver.get("https://www.google.co.kr")



        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // driver.close()
        }
    }

}