package it.unibo.core

trait TrajectoryTracker[S, A, R, M <: TrajectoryTracker[S, A, R, M]] {
  def track(sars : SARS[S, A, R]) : M
}

trait StateCount[S, A, R, M <: TrajectoryTracker[S, A, R, M]] extends TrajectoryTracker[S, A, R, M]{
  override def track(sars : SARS[S, A, R]) : M = super.track(sars)
}

