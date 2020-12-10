package it.unibo.core

sealed trait SARSA[S, A] extends TrajectoryTracker[S, A, Double, SARSA[S, A]] with TabularMethod[S, A, Double]

object SARSA {
  case class EmptySARSA[S, A](qTable : QTable[S, A, Double]) extends SARSA[S, A]{
    override def track(sars: SARS[S, A, Double]): SARSA[S, A] = OnTrackSARSA(qTable, sars)
  }
  case class OnTrackSARSA[S, A, R](qTable : QTable[S, A, Double], sarsT : SARS[S, A, R]) extends SARSA[S, A] {
    override def track(sarsTPlus: SARS[S, A, Double]): SARSA[S, A] = {
      OnTrackSARSA(qTable, sarsTPlus)
    }
  }
}