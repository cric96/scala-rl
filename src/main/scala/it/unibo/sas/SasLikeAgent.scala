package it.unibo.sas

import it.unibo.core.{Agent, LearningProcess, LogicalClock}

sealed trait SasLikeAgent[S, A, P <: LearningProcess[S, A, Unit, P]] <: Agent[S, A, Unit, P]

object SasLikeAgent {
  case class StateSas[S, A, P <: LearningProcess[S, A, Unit, P]](state : S, process : P, clock : LogicalClock) extends SasLikeAgent[S, A, P] {
    final def action(a : A) : ActionSas[S, A, P] = ActionSas(a, conclude, clock.tick)
    override final def conclude: P = process.acceptState(state, clock)
  }
  case class ActionSas[S, A, P <: LearningProcess[S, A, Unit, P]](action : A, process : P, clock : LogicalClock) extends SasLikeAgent[S, A, P] {
    final def state(s : S) : StateSas[S, A, P] = StateSas(s, conclude, clock)
    override final def conclude: P = process.acceptAction(action, clock)
  }
}

