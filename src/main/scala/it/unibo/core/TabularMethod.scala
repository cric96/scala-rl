package it.unibo.core

import it.unibo.core.Concepts.QFunction
import it.unibo.core.typeclass.{AgentConditioning, AvailableStateActions, StateActionFn}

trait TabularMethod[S, A, R]{
  def qTable : QTable[S, A, R]
  //def vTable : VTable[S, R] TODO!
}
object TabularMethod {
  abstract class TabularContext[S, A, R, T <: TabularMethod[S, A, R]](implicit conditioning: AgentConditioning[S, A]) extends TabularAvailableStateActions[S, A, R, T] with TabularExtractFn[S, A, R, T]

  trait TabularExtractFn[S, A, R, T <: TabularMethod[S, A, R]] extends StateActionFn[S, A, R, T] {
    override def extractFn(target: T): QFunction[S, A, R] = target.qTable
  }

  abstract class TabularAvailableStateActions[S, A, R, T <: TabularMethod[S, A, R]](implicit conditioning: AgentConditioning[S, A]) extends AvailableStateActions[S, A, T] {
    override def enumerateStates(target: T): Iterable[S] = target.qTable.states
    override def enumerateActions(target: T, state: S): Iterable[A] = target.qTable.actions.filter(conditioning.avoid(state).contains(_))
  }
}