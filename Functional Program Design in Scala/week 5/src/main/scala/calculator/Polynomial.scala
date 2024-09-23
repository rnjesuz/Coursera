package calculator

import scala.util.chaining.scalaUtilChainingOps

object Polynomial extends PolynomialInterface:
  def computeDelta(a: Signal[Double], b: Signal[Double], c: Signal[Double]): Signal[Double] =
    Signal(b() * b() - 4 * a() * c())

  def computeSolutions(
    a: Signal[Double],
    b: Signal[Double],
    c: Signal[Double],
    delta: Signal[Double],
  ): Signal[Set[Double]] = 
    computeDelta(a, b, c)
      .pipe { delta =>
        Signal({
          if delta() < 0 then Set.empty
          else Set(
            (Math.sqrt(delta()) - b()) / (2 * a()),
            (-Math.sqrt(delta()) - b()) / (2 * a()))
        })
      }
    