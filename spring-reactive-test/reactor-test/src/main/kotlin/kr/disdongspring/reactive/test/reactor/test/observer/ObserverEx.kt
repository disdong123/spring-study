package kr.disdongspring.reactive.test.reactor.test.observer

interface Observer <T> {
    fun update(data: T)
}

abstract class Observable2 <T> {
    protected val observers: MutableList<Observer<T>> = mutableListOf()
    protected val data: T? = null

    fun register(o: Observer<T>) {
        observers.add(o)
    }
    fun remove(o: Observer<T>) {
        observers.remove(o)
    }
    abstract fun notify(data: T)
}

data class WeatherData(
    private val temperature: Int = 0,
    private val humidity: Int = 0,
)

class Weather : Observable2<WeatherData>() {

    override fun notify(data: WeatherData) {
        observers.forEach {
            it.update(data)
        }
    }
}

class OutsideThermometer : Observer<WeatherData> {
    var data: WeatherData? = null

    override fun update(data: WeatherData) {
        this.data = data
    }
}

class InsideThermometer : Observer<WeatherData> {
    var data: WeatherData? = null

    override fun update(data: WeatherData) {
        this.data = data
    }
}

fun test() {
    println("${Thread.currentThread().name} start...")
    val weather = Weather()
    val outside = OutsideThermometer()
    val inside = InsideThermometer()

    weather.register(outside)
    weather.register(inside)

    weather.notify(WeatherData(1, 2))

    println("${Thread.currentThread().name} ${outside.data}")
    println("${Thread.currentThread().name} ${inside.data}")

    println("${Thread.currentThread().name} end...")
}
