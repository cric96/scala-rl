package it.unibo.core

import it.unibo.core.Concepts.VFunction

trait VTable[S, R] extends VFunction[S, R] {
  implicit val orderingR : Ordering[R]
  def states : Set[S]
  def update(s : S, r : R) : VTable[S, R]
}
