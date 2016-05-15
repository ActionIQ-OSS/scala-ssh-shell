package peak6.util
import java.util

import jline.console.completer.ArgumentCompleter.ArgumentList

import scala.tools.nsc.interpreter.Parsed

object Helpers {
  val TypeStrings = scala.tools.nsc.interpreter.TypeStrings
  class JLineDelimiter extends jline.console.completer.ArgumentCompleter.ArgumentDelimiter {
    val delegate = new scala.tools.nsc.interpreter.JLineDelimiter

    override def delimit(buffer: CharSequence, pos: Int): ArgumentList = {
      val p = Parsed(buffer.toString, pos)
      p.args match {
        case Nil    => new ArgumentList(new Array[String](0), 0, 0, pos)
        case xs     => new ArgumentList(xs.toArray, xs.size - 1, xs.last.length, pos)
      }
    }

    override def isDelimiter(buffer: CharSequence, pos: Int): Boolean = {
      delegate.isDelimiter(buffer, pos)
    }
  }

  val JLineHistory = scala.tools.nsc.interpreter.session.JLineHistory
  type ConsoleReaderHelper = scala.tools.nsc.interpreter.ConsoleReaderHelper
}
