package it.unibo.core

case class SAS[S, A](stateT : S, actionT : A, stateTPlus : S)
