package it.unibo.sarsa

import it.unibo.core.{Concepts, LearningProcess, LogicalClock, QTable, Wrap}
import it.unibo.sarsa.QLearning.HyperParameter

case class QLearning[S, A](q : QTable[S, A, Double], parameters : HyperParameter, clock : LogicalClock) extends LearningProcess[S, A, Double, QLearning[S, A]] {
  def sequence : Seq[Wrap[_]] = Seq.empty
  override def acceptState(s: S, clock : LogicalClock): QLearning[S, A] = sequence match {
    case (r : Reward) :: (oldAction : Action) :: (oldState : State) :: Nil =>
      createWithSequence(Seq(new State(s)), update(s, r.value, oldAction.value, oldState.value), clock)
    case _ => createWithSequence(Seq(new State(s)), q, clock)
  }
  override def acceptAction(a: A, clock : LogicalClock): QLearning[S, A] = createWithSequence(new Action(a) +: this.sequence, q, clock)
  override def acceptReward(r: Double, clock : LogicalClock): QLearning[S, A] = createWithSequence(new Reward(r) +: this.sequence, q, clock)
  override def policy: Concepts.Policy[S, A] = QTable.epsilonGreedyFromTable(q, parameters.epsilon(clock))
  def update(s : S, r : Double, aOld : A, sOld : S) : QTable[S, A, Double] = {
    val maxValue = q.actions.map(q(s, _)).max
    val alpha = parameters.alpha(clock)
    val update = q(sOld, aOld) + alpha * (r + parameters.discount * (maxValue - q(sOld, aOld)))
    q.update((sOld, aOld), update)
  }
  private def createWithSequence(updateSequence : Seq[Wrap[_]], q : QTable[S, A, Double], newClock : LogicalClock) : QLearning[S, A] = {
    new QLearning[S, A](q, parameters, newClock) {
      override def sequence: Seq[Wrap[_]] = updateSequence
    }
  }
}

object QLearning {
  case class HyperParameter(alpha : (LogicalClock => Double), epsilon : (LogicalClock => Double), discount : Double)
  def timeIndependent(alpha : Double, epsilon : Double, discount : Double) : HyperParameter = {
    HyperParameter(clock => alpha, clock => epsilon, discount : Double)
  }
}