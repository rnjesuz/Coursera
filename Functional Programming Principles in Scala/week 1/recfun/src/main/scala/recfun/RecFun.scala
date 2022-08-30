package recfun

import scala.collection.mutable
import scala.collection.mutable.Stack

object RecFun extends RecFunInterface:

  def main(args: Array[String]): Unit =
    println("Pascal's Triangle")
    for row <- 0 to 10 do
      for col <- 0 to row do
        print(s"${pascal(col, row)} ")
      println()

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int =
    if r < 0 || c < 0 || c > r then throw IllegalArgumentException("Invariant broken 0 <= c <= r")
    if c == 0 || c == r then 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean =
    val parenthesis = Stack[Char]()
    chars.foreach( char =>
      char match
        case '(' => parenthesis.push(char)
        case ')' => if parenthesis.isEmpty then return false else parenthesis.pop()
        case _ =>
    )
    if parenthesis.isEmpty then true else false

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int =
    if (money < 0 || coins.isEmpty ) then 0
    else if (money == 0) then 1
    else countChange(money, coins.tail) + countChange(money - coins.head, coins)