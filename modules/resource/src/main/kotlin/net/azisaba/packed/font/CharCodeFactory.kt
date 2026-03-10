package net.azisaba.packed.font

open class CharCodeFactory(private var char: Char = '\uE001') {
    protected fun nextChar(): Char = char++
}
