package com.example.kotlinwithselenium

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.io.File

@SpringBootApplication
class KotlinWithSeleniumApplication

@Component
class AppStarted(
    private val chromeContext: ChromeContext
) : ApplicationListener<ApplicationStartedEvent> {

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
         chromeContext.start()

//        val students = listOf(Student("Apple", 10), Student("Banana", 20))
//        val gson = GsonBuilder().setPrettyPrinting().create()
//        File("test.json").writeText(gson.toJson(students))

    }

}

fun main(args: Array<String>) {

    runApplication<KotlinWithSeleniumApplication>(*args)

}


data class Student(private val name: String, private val age: Int)