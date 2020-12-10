package it.unibo.core

object Learning {
  type ModelBased[S, A, R] = Environment[(S, A), S, A, R]
  def episode[S, A, R](s : S, env : ModelBased[S, A, R], )
}