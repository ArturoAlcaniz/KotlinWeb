package com.webKotlin.dao

class Broker private constructor() {
    private val pool: Pool = Pool(10)

    private object BrokerHolder {
        var singleton = Broker()
    }

    val bd: WrapperConnection
        get() = pool.connection

    companion object {

        @JvmStatic
        fun get(): Broker {
            return BrokerHolder.singleton
        }
    }

}