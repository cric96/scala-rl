package it.unibo.core

import it.unibo.core.typeclass.Decayable

case class QLearning[S, A](initialQ : QTable[S, A, Double],
                           environment: Environment[(S, A), S, A, Double],
                           epsilon : Parameter[Double],
                           alpha : Parameter[Double],
                           discount : Parameter[Double],
                           initialTime : Time)(implicit stochastic: Stochastic) {
  import QLearningTracker.Implicits._
  import it.unibo.core.typeclass.AgentConditioning.NoConditioning._
  type PolicyMaker = EpsilonGreedyPolicyMaker[S, A, Double, QLearningTracker[S, A]]
  type Tracker = QLearningTracker[S, A]
  type Episode = (SARS[S, A, Double], Tracker, PolicyMaker, Time)

  def episode(state : S, tracker : Tracker, policy : PolicyMaker, time : Time) : Episode = {
    val action = policy.buildFrom(tracker)(state)
    val (newState, reward) = environment((state, action))
    val sars = SARS[S, A, Double](state, action, reward, newState, time)
    (sars, tracker.track(sars), implicitly[Decayable[PolicyMaker]].decay(policy, time), time.tick())
  }

  def episode(sars : SARS[S, A, Double], tracker: Tracker, policyMaker: PolicyMaker, time: Time) : Episode = episode(sars.stateT, tracker, policyMaker, time)

  private val tracker = new QLearningTracker[S, A](initialQ, alpha, discount)
  private val policyMaker = new EpsilonGreedyPolicyMaker[S, A, Double, Tracker](epsilon)

  def learn(initialState : S) : LazyList[Episode] = {
    LazyList.iterate(episode(initialState, tracker, policyMaker, initialTime)) {
      case (state, tracker, policyMaker, time) => episode(state, tracker, policyMaker, time)
    }
  }
}