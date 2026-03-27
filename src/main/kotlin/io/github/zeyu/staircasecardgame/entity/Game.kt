package io.github.zeyu.staircasecardgame.entity

import java.util.Stack

/**
 * Root entity that holds the whole game state.
 *
 * @property players        the two players in turn order (size = 2)
 * @property stairs         the 5 stair stacks; each row is a [Stack] of [Card]
 * @property currentPlayer  index of the player whose turn it is (0 or 1)
 * @property gameLog        list of public log messages (anonymized)
 * @property stairsModified flag set to true once a stair was changed during the current turn
 */
data class Game(val players: List<Player>) {
    // 每个Stack是一列，而不是一行
    var stairs: List<Stack<Card>> = listOf(Stack(), Stack(), Stack(), Stack(), Stack())
    var currentPlayer: Int = 0
    var gameLog: MutableList<String> = mutableListOf()
    var stairsModified: Boolean = false

    // Game类实例必须能记录两个牌堆
    var drawStack: Stack<Card> = Stack()
    var discardStack: Stack<Card> = Stack()
}














//val = 只读引用（不能重新赋值）
//首次赋值后，这个变量名就不能指向别的对象了。
//但对象本身如果是可变的，还是可以改内容。
//
//var = 可读写引用（可以重新赋值）
//可以把变量名重新指向别的对象；当然对象本身如果是可变的，也能改内容。