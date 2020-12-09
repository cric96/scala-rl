package it.unibo.core

object Concepts {
  type VFunction[S, R] = PartialFunction[S, R]
  type QFunction[S, A, R] = PartialFunction[(S, A), R]
  type Environment[C, S, R] = (C) => (S, R)
  trait Policy[S, A] extends PartialFunction[S, A] {
    def probabilityOf(s : S, a : A) : Double
  }
  trait DeterministicPolicy[S, A] extends Policy[S, A] {
    override def probabilityOf(s : S, a : A) : Double = this.lift(s) match {
      case Some(`a`) => 1.0
      case _ => 0.0
    }
  }
}
