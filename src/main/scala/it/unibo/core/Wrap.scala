package it.unibo.core

trait Wrap[E] {
  def value : E
}
object Wrap {
  case class State[S](value : S) extends Wrap[S]
  case class Action[A](value : A) extends Wrap[A]
  case class Reward[R](value : R) extends Wrap[R]
}
