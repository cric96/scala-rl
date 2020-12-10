package it.unibo.core

import scala.util.Random

trait RandomGenerator {
  def nextInt(bound : Int) : Int
  def nextDouble() : Double
}

object RandomGenerator {
  def wrapRandom(random : Random) = new RandomGenerator {
    override def nextInt(bound : Int): Int = random.nextInt()
    override def nextDouble(): Double = random.nextDouble()
  }
}