package it.unibo.core

case class Time(stamp : Long = 0) {
  def tick() : Time = Time(stamp + 1)
}
