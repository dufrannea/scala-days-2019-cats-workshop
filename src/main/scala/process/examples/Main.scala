import cats.effect._
import scala.util.Random
import cats.implicits._
import scala.concurrent.Await

object Main {

    // --------------
    // Part 1
    // --------------

    // Question 1 :
    // How to compute a random value inside an IO ?
    def random(): IO[Double] = IO(Random.nextDouble)

    // Beware of using pure in that case
    def randomPure(): IO[Double] = IO.pure(Random.nextDouble())

    def proveWrong = {
        val one = for {
            x <- randomPure()
            y <- randomPure()
        } yield (x, y)

        val a = randomPure()

        val two = for {
            x <- a
            y <- a
        } yield (x, y)

        (one, two).tupled
    }

    // also easy to break with Future
    def proveFutureBreaksSubstitution() = {
        import scala.concurrent.ExecutionContext.Implicits.global
        import scala.concurrent.Future
        import scala.concurrent.duration._

        val randomInFuture: Future[Double] = Future { Random.nextDouble() }

        val example = for {
                x <- randomInFuture
                y <- randomInFuture
            } yield (x + y)

        Await.result(example, 10.second)
    }

    // Question 2:
    // how can we perform a *pure* computation transforming
    // the result of type A into a result of type B
    // Answer: Just use map

    // Question 3:
    // same with *impure* and having some effects
    def example3(): IO[(Double, Double)] = {

        val start: IO[Double] = random()

        val program1: IO[Double] = start.flatMap(a => {
            val b = Random.nextDouble()
            IO(a+b)
        })

        val program2: IO[Double] = start.flatMap(a => IO {
            val b = Random.nextDouble()
            a + b
        })

        // takeaway: always prefer version2 over version1
        // because it does not rely on the implementation
        // on the IO. 
        // Consider you are basically side effecting outside
        // an IO
        (program1 -> program2).tupled
    }

    // Question 4:
    // How do we run it ?
    // Answer trivial


    // --------------
    // Part 2
    // --------------


}