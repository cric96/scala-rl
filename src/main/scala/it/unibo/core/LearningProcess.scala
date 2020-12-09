package it.unibo.core

import it.unibo.core.Concepts.Policy

trait LearningProcess[S, A, R, P <: LearningProcess[S, A, R, P]] {
  type State = it.unibo.core.Wrap.State[S]
  type Action = it.unibo.core.Wrap.Action[A]
  type Reward = it.unibo.core.Wrap.Reward[R]
  def acceptState(s : S, clock : LogicalClock) : P
  def acceptAction(a : A, clock : LogicalClock) : P
  def acceptReward(r : R, clock : LogicalClock) : P
  def clock : LogicalClock
  def policy : Policy[S, A]
}

object LearningProcess {
  case class NoLearning[S, A, R](policy : Policy[S, A], clock : LogicalClock) extends LearningProcess[S, A, R, NoLearning[S, A, R]] {
    override def acceptState(s: S, clock : LogicalClock): NoLearning[S, A, R] = this
    override def acceptAction(a: A, clock : LogicalClock): NoLearning[S, A, R] = this
    override def acceptReward(r: R, clock : LogicalClock): NoLearning[S, A, R] = this
  }
}