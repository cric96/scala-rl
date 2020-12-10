package it.unibo.core
case class SARS[S, A, R](stateT : S, actionT : A, rewardTPlus : R, stateTPlus : S, temporalMark : Time)
