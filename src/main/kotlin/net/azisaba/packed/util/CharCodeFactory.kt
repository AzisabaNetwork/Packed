package net.azisaba.packed.util

open class CharCodeFactory {
    private var currentChar: Char = '\uE001'

    protected fun nextChar(): Char = currentChar++
}
