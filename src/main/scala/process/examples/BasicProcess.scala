package process
package examples

import cats.implicits._
import cats.effect._

import doodle.core._
import doodle.syntax._
import doodle.image._
import doodle.image.syntax._
import doodle.java2d._
import doodle.java2d.effect.Frame
import scala.util.Random

object Numbers {
  val randomDouble: IO[Double] = IO { Random.nextDouble() }
  val nextGaussian: IO[Double] = IO { Random.nextGaussian() }
  val randomColor: IO[Color] = (randomDouble, randomDouble, randomDouble).tupled.map({
    case (r1, r2, r3) =>
      Color.hsla(
        (r1 / 3.0 - 0.33).turns, // blues and reds
        r2 / 2.0 + 0.4, // fairly saturated
        r3 / 2.0 + 0.4, // fairly light
        0.7 // Somewhat transparent
      )
  })
}

/** Simple process with no interaction between the elements. */
object BasicProcess  extends IOApp {

  import Numbers._

  object State {
    /** How much we change the location by when we step forward. */
    val locationDelta = 5.0

    /** How much we change the heading by when we turn. */
    val headingDelta = 30.degrees
    val initial = State(Point.zero, Angle.zero, List.empty)
  }

  final case class State(location: Point, heading: Angle, path: List[Point]) {
    def forward: State = {
      val newLocation = location + Vec.polar(State.locationDelta, heading)
      this.copy(
        location = newLocation,
        path = newLocation +: path
      )
    }

    def clockwise: State =
      this.copy(heading = heading + State.headingDelta)

    def anticlockwise: State =
      this.copy(heading = heading - State.headingDelta)

    def toImage: Image =
      Image.interpolatingSpline(path)
  }

  type Event = Double

  override def run(args: List[String]): IO[ExitCode] = {
    /** The finite state machine defines how the state evolves over time. Tweaking
      * the probabilities will arrive at different results. */
    val fsm =
      Fsm[State,Event]{ (state, choice) =>
        if(choice < 0.5) state.forward
        else if (choice < 0.75) state.clockwise
        else state.anticlockwise
      }

    /** Execute one step of the FSM */
    def step(state: State): IO[State] = randomDouble.map(r => fsm(state, r))

    /** Execute count steps of the FSM */
    def iterate(count: Int, state: State): IO[State] =
      if(count == 0) IO.pure(state)
      else
        step(state).flatMap( s => iterate(count - 1, s))

    def squiggle(initialState: State): IO[Image] =
      randomColor.flatMap(c =>
        iterate(100, initialState)
          .map(s => s.toImage.strokeWidth(3.0).strokeColor(c))
      )

    val initialPosition: IO[Point] = (nextGaussian, nextGaussian).tupled.map({
      case (r1, r2) =>
        // Poisson disk sampling might be more attractive
        Point(r1 * 150, r2 * 150)
    })

    def initialDirection(position: Point): Angle =
      (position - Point.zero).angle

    val squiggles: IO[Image] =
      (0 to 500).toList.parTraverse {_ =>
        val a: IO[Image] = for {
          pt <- initialPosition
          angle = initialDirection(pt)
          state = State(pt, angle, List.empty)
          r <- squiggle(state)
        } yield r
        a
      }.map(_.allOn)

    val frame = Frame.fitToPicture().background(Color.black)
    val go: IO[Unit] = squiggles.map(_.draw(frame))

    go.map(_ => ExitCode.Success)
  }
}
