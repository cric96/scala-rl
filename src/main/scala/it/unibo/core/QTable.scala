package it.unibo.core

import it.unibo.core.Concepts.{QFunction, VFunction}

abstract class QTable[S, A, R : Ordering] extends QFunction[S, A, R] {
  def states : Set[S]
  def actions : Set[A]
  def update(key : (S, A), value : R) : QTable[S, A, R]
  def vFunction : VFunction[S, R] = (s : S) => actions.map(apply(s, _)).max

  override def toString(): String = ( for {
    s <- states
    a <- actions
  } yield (s"s($s), a($a) -> ${this(s,a)}") ) mkString "\n"
}
object QTable {
  def fromFunction[S, A, R : Ordering](qF : QFunction[S, A, R], states : Set[S], actions : Set[A]) : QTable[S, A, R] = {
    val map = for {
      s <- states
      a <- actions
    } yield ((s, a) -> qF(s, a))
    QTableImpl(map.toMap, states, actions)
  }
  private case class QTableImpl[S, A, R: Ordering](map : Map[(S, A), R], states : Set[S], actions : Set[A]) extends QTable[S, A, R] {
    override def update(key: (S, A), value: R): QTable[S, A, R] = QTableImpl(map + (key -> value), states, actions)
    override def isDefinedAt(x: (S, A)): Boolean = map.contains(x)
    override def apply(v1: (S, A)): R = map(v1)
  }
}
