package it.unibo.core

import it.unibo.core.Concepts.DeterministicPolicy
import it.unibo.core.typeclass.{AvailableStateActions, StateActionFn, PolicyMarker}

case class GreedyPolicyMaker[S, A, R : Ordering, T]()(implicit val actionStatesEnumerator: AvailableStateActions[S, A, T], implicit val stateActionFun: StateActionFn[S, A, R, T]) extends PolicyMarker[S, A,  T] {
  override def buildFrom(trajectory: T): Concepts.Policy[S, A] = {
    val states = actionStatesEnumerator.enumerateStates(trajectory)
    val valueFunction = stateActionFun.extractFn(trajectory)
    new DeterministicPolicy[S, A] {
      override def isDefinedAt(x: S): Boolean = states.toSet.contains(x)
      override def apply(s: S): A = actionStatesEnumerator.enumerateActions(trajectory, s).maxBy(valueFunction(s, _))
    }
  }
}
