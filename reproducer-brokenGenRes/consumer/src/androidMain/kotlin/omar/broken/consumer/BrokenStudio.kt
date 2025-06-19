package omar.broken.consumer

import omar.broken.producer.Produce


class BrokenStudio {

    val produce = Produce("omar")

    fun hello() {
        println(produce.name)
    }
    
}