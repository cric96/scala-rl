package it.unibo.core.typeclass

import it.unibo.core.Concepts.Policy
import it.unibo.core.TrajectoryTracker

trait PolicyMarker[S, A, T] {
  def buildFrom(target: T): Policy[S, A]
}
