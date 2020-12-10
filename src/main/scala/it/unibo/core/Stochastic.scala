package it.unibo.core

class Stochastic(randomGenerator: RandomGenerator) {
  def pickRandom[A](iterable : Iterable[A]) : A = iterable.toSeq.apply(randomGenerator.nextInt(iterable.size))
  case class Drawer[A](p : Double)(ifTrue : A) {
    def orElse(other : A) : A = if(p < randomGenerator.nextDouble()) { ifTrue } else { other }
  }
  def draw[A](p : Double)(ifTrue : => A) : Drawer[A] = Drawer(p)(ifTrue)
  def draw[A](p : Parameter[Double])(ifTrue : => A) = Drawer(p.data)(ifTrue)
}