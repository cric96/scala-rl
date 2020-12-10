package it.unibo.core

import it.unibo.core.Concepts.Policy
import it.unibo.core.typeclass.{AgentConditioning, AvailableActions, PolicyMarker}

case class ExploratoryPolicyMaker[S, A, T]()(implicit val actionsExtractor : AvailableActions[S, A, T],
                                             implicit val stochastic: Stochastic)
    extends PolicyMarker[S, A, T] {
  override def buildFrom(trajectory: T): Concepts.Policy[S, A] = {
    new Policy[S, A] {
      def actionsFrom(s : S) : Iterable[A] = actionsExtractor.enumerateActions(trajectory, s)
      override def probabilityOf(s: S, a: A): Double = 1.0 / actionsFrom(s).size
      override def isDefinedAt(x: S): Boolean = { true }
      override def apply(s: S): A = stochastic.pickRandom(actionsFrom(s))
    }
  }
}