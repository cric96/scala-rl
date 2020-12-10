package it.unibo.core.typeclass

trait AgentConditioning[S, A] {
  def avoid(state : S) : Seq[A]
}

object AgentConditioning {
  object NoConditioning {
    implicit def noConditioning[S, A] : AgentConditioning[S, A] = (state: S) => Seq.empty
  }
}
