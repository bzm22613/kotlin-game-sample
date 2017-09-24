package work.katashin.kotlingamebackend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinGameBackendApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinGameBackendApplication::class.java, *args)
}
