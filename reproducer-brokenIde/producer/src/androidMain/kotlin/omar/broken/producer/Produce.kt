package omar.broken.producer


class Produce(val name: String) {

    fun build(): String {
        return "I am here $name"
    }
}

