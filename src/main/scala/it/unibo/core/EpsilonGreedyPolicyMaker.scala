package it.unibo.core

import it.unibo.core.typeclass.{AvailableStateActions, Decayable, PolicyMarker, StateActionFn}

case class EpsilonGreedyPolicyMaker[S, A, R : Ordering,  T](epsilon : Parameter[Double])
    (implicit val avail: AvailableStateActions[S, A, T],implicit val fn : StateActionFn[S, A, R, T], implicit val stochastic: Stochastic) extends PolicyMarker[S, A, T] {
  val greedy = new GreedyPolicyMaker[S, A, R, T]()
  val exploratory = new ExploratoryPolicyMaker[S, A, T]()

  override def buildFrom(trajectory: T): Concepts.Policy[S, A] = {
    val greedyPolicy = greedy.buildFrom(trajectory)
    val exploratoryPolicy = greedy.buildFrom(trajectory)
    stochastic.draw(epsilon)(exploratoryPolicy) orElse (greedyPolicy)
  }
}
object EpsilonGreedyPolicyMaker {
  def decayPolicy[S, A, R : Ordering, T]() : Decayable[EpsilonGreedyPolicyMaker[S, A, R, T]] = {
    (t: EpsilonGreedyPolicyMaker[S, A, R, T], time: Time) => new EpsilonGreedyPolicyMaker[S, A, R, T](t.epsilon.updateWith(time))(implicitly[Ordering[R]], t.avail, t.fn, t.stochastic)
  }
}
