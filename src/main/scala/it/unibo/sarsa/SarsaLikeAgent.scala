package it.unibo.sarsa

import it.unibo.core.{Agent, LearningProcess, LogicalClock}

sealed trait SarsaLikeAgent[S, A, R, P <: LearningProcess[S, A, R, P]] extends Agent [S, A, R, P]

object SarsaLikeAgent {
  case class StateSarsa[S, A, R, P <: LearningProcess[S, A, R, P]](state : S, process : P, clock : LogicalClock) extends SarsaLikeAgent[S, A, R, P] {
    final def action(a : A) : ActionSarsa[S, A, R, P] = ActionSarsa(a, conclude, clock)
    override final def conclude: P = process.acceptState(state, clock)
  }
  case class ActionSarsa[S, A, R, P <: LearningProcess[S, A, R, P]](action : A, process : P, clock : LogicalClock) extends SarsaLikeAgent[S, A, R, P] {
    final def reward(r : R) : RewardSarsa[S, A, R, P] = RewardSarsa(r, conclude, clock.tick)
    override final def conclude: P = process.acceptAction(action, clock)
  }
  case class RewardSarsa[S, A, R, P <: LearningProcess[S, A, R, P]](reward : R, process : P, clock : LogicalClock) extends SarsaLikeAgent[S, A, R, P] {
    final def state(s : S) : StateSarsa[S, A, R, P] = StateSarsa(s, conclude, clock)
    override final def conclude: P = process.acceptReward(reward, clock)
  }
}
