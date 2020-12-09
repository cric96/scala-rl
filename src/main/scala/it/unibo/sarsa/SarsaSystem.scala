package it.unibo.sarsa

import it.unibo.core.Concepts.Environment
import it.unibo.core.{LearningProcess, LogicalClock, QTable}
import it.unibo.sarsa.SarsaLikeAgent.StateSarsa
object SarsaSystem {
  def learningStream[S, A, R, P <: LearningProcess[S, A, R, P]](initialState : StateSarsa[S, A, R, P], env : Environment[(S, A), S, R]) : LazyList[SarsaLikeAgent[S, A, R, P]] = {
    LazyList.iterate(initialState)(mind => {
      val policy = mind.process.policy
      val actionRecorded = mind.action(policy(mind.state))
      val (nextState, reward) = env((mind.state, actionRecorded.action))
      val rewardRecorded = actionRecorded.reward(reward)
      rewardRecorded.state(nextState)
    })
  }
}

object Main extends App {
  val x : Int = 10
  type State = Int
  type Action = String
  val states = Set(1, 2)
  val actions = Set("a", "b", "c")

  val env = new Environment[(State, Action), State, Double] {
    override def apply(v1: (State, Action)): (State, Double) = v1 match {
      case (1, "a") => (1, 0)
      case (1, "b") => (2, -1)
      case (1, "c") => (1, -1)
      case (2, "a") => (2, -1)
      case (2, "b") => (1, -2)
      case (2, "c") => (2, -2)
    }
  }

  val clock = LogicalClock()
  val qtable = QTable.fromFunction[State, Action, Double](v => 0.0, states, actions)
  val process = QLearning(qtable, QLearning.timeIndependent(0.1, 0.4, 0.90), clock)
  val agent = StateSarsa[State, Action, Double, QLearning[State, Action]](1, process, clock)
  val beforeUsedMem = Runtime.getRuntime.totalMemory - Runtime.getRuntime.freeMemory
  val evolution = SarsaSystem.learningStream(agent, env)
  val last = evolution.take(1000000)
  val afterUsedMem = Runtime.getRuntime.totalMemory - Runtime.getRuntime.freeMemory
  val iterator = last.iterator
  while(iterator.hasNext) {
    iterator.next
  }
  println(afterUsedMem)
}