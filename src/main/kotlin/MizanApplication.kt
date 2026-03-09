package uz.mizanai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration::class])
class MizanApplication

fun main(args: Array<String>) {
    runApplication<MizanApplication>(*args)
}