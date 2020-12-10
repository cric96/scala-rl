package it.unibo.core

trait Parameter[V] {
  def data : V
  def updateWith(time : Time) : Parameter[V]
}

object Parameter {
  sealed case class TimeInvariant[V](data : V) extends Parameter[V] {
    override def updateWith(time: Time): Parameter[V] = this
  }
  sealed case class TimeDependent[V](data : V, evolution : Time => V) extends Parameter[V] {
    override def updateWith(time: Time): Parameter[V] = TimeDependent(evolution(time), evolution)
  }
}