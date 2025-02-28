package jp.inaba.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InabaApplication

fun main(args: Array<String>) {
    runApplication<InabaApplication>(*args)
}
