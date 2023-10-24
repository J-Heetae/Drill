package project.drill

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DrillApplication

fun main(args: Array<String>) {
	runApplication<DrillApplication>(*args)
}
