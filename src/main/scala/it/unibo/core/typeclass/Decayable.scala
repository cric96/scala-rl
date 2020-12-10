package it.unibo.core.typeclass

import it.unibo.core.Time

trait Decayable[T] {
  def decay(t : T, time : Time) : T
}
