package it.unibo.core

import it.unibo.core.Concepts.{DeterministicPolicy, Policy, QFunction, VFunction}

import scala.util.Random

trait QTable[S, A, R] extends QFunction[S, A, R] {
  implicit val orderingR : Ordering[R]
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
  def greedyPolicyFromTable[S, A, R: Ordering](table: QTable[S, A, R]) : DeterministicPolicy[S, A] = new DeterministicPolicy[S, A] {
    override def isDefinedAt(x: S): Boolean = table.states.contains(x)
    override def apply(v1: S): A = table.actions.maxBy(a => table(v1, a))
  }
  //todo add random type classes
  def exploratoryFromTable[S, A, R](table: QTable[S, A, R]) : Policy[S, A] = new Policy[S, A] {
    val random = new Random()
    override def probabilityOf(s: S, a: A): Double = 1.0 / table.actions.size
    override def isDefinedAt(x: S): Boolean = table.states.contains(x)
    override def apply(v1: S): A = table.actions.toSeq.apply(random.nextInt(table.actions.size ))
  }
  def epsilonGreedyFromTable[S, A, R : Ordering](table: QTable[S, A, R], epsilon : Double) : Policy[S, A] = new Policy[S, A] {
    val greedy = greedyPolicyFromTable(table)
    val exploratory = exploratoryFromTable(table)
    override def probabilityOf(s: S, a: A): Double = if(greedy(s) == a) { 1.0 - epsilon } else { epsilon }
    override def isDefinedAt(x: S): Boolean = table.states.contains(x)
    override def apply(v1: S): A = if(math.random() < epsilon) {
      exploratory(v1)
    } else {
      greedy(v1)
    }
  }
  def fromFunction[S, A, R : Ordering](qF : QFunction[S, A, R], states : Set[S], actions : Set[A]) : QTable[S, A, R] = {
    val map = for {
      s <- states
      a <- actions
    } yield ((s, a) -> qF(s, a))
    QTableImpl(map.toMap, states, actions)
  }
  private case class QTableImpl[S, A, R: Ordering](map : Map[(S, A), R], states : Set[S], actions : Set[A]) extends QTable[S, A, R] {
    override implicit val orderingR: Ordering[R] = orderingR
    override def update(key: (S, A), value: R): QTable[S, A, R] = QTableImpl(map + (key -> value), states, actions)(orderingR)
    override def isDefinedAt(x: (S, A)): Boolean = map.contains(x)
    override def apply(v1: (S, A)): R = map(v1)
  }
}
