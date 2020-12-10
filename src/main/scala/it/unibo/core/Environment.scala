package it.unibo.core

trait Environment[C, S, A, R] extends PartialFunction[C, Environment[C, S, A, R]] {

}

object Environment {
  case class EnvironmentUpdate[C, S, A, R](update : Environment[C, S, A, R], agentState : S, payoff : R)
}