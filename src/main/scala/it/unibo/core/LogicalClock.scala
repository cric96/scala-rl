package it.unibo.core

case class LogicalClock(time : Int = 0) {
  def tick : LogicalClock = LogicalClock(time + 1)
}
