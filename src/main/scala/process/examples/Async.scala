package process
package examples

import scala.concurrent.ExecutionContext
import java.util.concurrent.{Executors, ThreadFactory}
import cats.effect.IO
import cats.implicits._

object ExecutionContexts {
    val tf = new ThreadFactory {
        override def newThread(toRun: Runnable): Thread = {
            val thread = new Thread(toRun)
            thread.setName("lol")
            thread.setDaemon(true)
            thread
        }
    }
    val executor = Executors.newFixedThreadPool(2, tf)
    val fixed = ExecutionContext.fromExecutor(executor)
}
object AsyncTest {
   // implicit lazy val cs = IO.contextShift(ExecutionContexts.fixed)
    implicit lazy val cs = IO.contextShift(ExecutionContexts.fixed)

    def log(msg: => String) = IO(println(s"[${Thread.currentThread.getName}] - $msg"))

    def run(): IO[Unit] = {
        (IO.shift >> log("Hello"))
    }

    run.unsafeRunAsync(_ => ())

    List(IO(1)).par
}