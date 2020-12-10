package it.unibo.core.typeclass

import it.unibo.core.Concepts.QFunction

abstract class AvailableActions[S, A, T](implicit agentConditioning: AgentConditioning[S, A]) {
  def enumerateActions(target : T, state : S) : Iterable[A]
}

trait AvailableStates[S, T] {
  def enumerateStates(target : T) : Iterable[S]
}

abstract class AvailableStateActions[S, A, T](implicit agentConditioning: AgentConditioning[S, A]) extends AvailableActions[S, A, T] with AvailableStates[S, T] {
  def enumerateStateAction(target : T) : Iterable[(S, A)] = for {
    s <- enumerateStates(target)
    a <- enumerateActions(target, s)
  } yield (s, a)
}

trait VisitedStates[S] {
  def howManyTime(s : S) : Int
}

trait StateActionFn[S, A, R, T] {
  def extractFn(target : T) : QFunction[S, A, R]
}



