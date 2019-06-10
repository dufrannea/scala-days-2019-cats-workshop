# Random cats tools
Convert `F[G[A]]` to `G[F[A]]` using `sequence`

```scala
import cats.implicits._
import cats.effect.IO

val example: IO[List[Int]] = List(IO(1), IO(2)).sequence
```

# Distributed systems via abstract algebra

*Interesting properties*
Associativity
Commutativity

*Use cases*
Hyperloglog
Bloom filters
Countminsketch
Eventually consistent systems -> CRDTS = commutative idempotent associative operations