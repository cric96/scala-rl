package it.unibo.core

import it.unibo.core.TabularMethod.TabularContext
import it.unibo.core.typeclass.{AgentConditioning, Decayable}

case class QLearningTracker[S, A](qTable: QTable[S, A, Double], alpha : Parameter[Double], discount : Parameter[Double])
  extends TrajectoryTracker[S, A, Double, QLearningTracker[S, A]] with TabularMethod[S, A, Double] {
  override def track(sars: SARS[S, A, scala.Double]): QLearningTracker[S, A] = {
    QLearningTracker(qTable, alpha.updateWith(sars.temporalMark), discount.updateWith(sars.temporalMark))
  }
}

object QLearningTracker {
  object Implicits {
    implicit def qLearningContext[S, A](implicit agent : AgentConditioning[S, A]) : TabularContext[S, A, Double, QLearningTracker[S, A]] = {
      new TabularContext[S, A, Double, QLearningTracker[S, A]] {}
    }
    implicit def greedyDecay[S, A] : Decayable[EpsilonGreedyPolicyMaker[S, A, Double, QLearningTracker[S, A]]] = {
      EpsilonGreedyPolicyMaker.decayPolicy()
    }
  }
}