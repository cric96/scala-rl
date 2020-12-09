package it.unibo.core

trait Agent [S, A, R, P <: LearningProcess[S, A, R, P]] {
  def conclude : P
  def process : P
  def clock : LogicalClock
}
