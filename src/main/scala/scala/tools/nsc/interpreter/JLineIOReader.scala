/* NSC -- new Scala compiler
 * Copyright 2005-2011 LAMP/EPFL
 * @author Stepan Koltsov
 */

package scala.tools.nsc.interpreter

import peak6.util.Helpers
import _root_.jline.console.completer.{ArgumentCompleter,Completer}
import scala.tools.jline.console.ConsoleReader
import scala.tools.jline.SshTerminal
import scala.tools.nsc.interpreter.Completion.{Candidates, ScalaCompleter}
import scala.tools.nsc.interpreter.session.{History, NoHistory}

/**
 *  Reads from the console using JLine.
 */
class JLineIOReader(in: java.io.InputStream,
                    out: java.io.OutputStream,
                    _completion: => Completion) extends InteractiveReader {
  val interactive = true
  val consoleReader = new JLineConsoleReader()

  lazy val completion = _completion
  lazy val history: History = Helpers.JLineHistory()

  private def term = consoleReader.getTerminal
  def reset() = term.reset()
  def init()  = term.init()

  def scalaToJline(tc: ScalaCompleter): Completer = new Completer {
    def complete(_buf: String, cursor: Int, candidates: JList[CharSequence]): Int = {
      val buf   = if (_buf == null) "" else _buf
      val Candidates(newCursor, newCandidates) = tc.complete(buf, cursor)
      newCandidates foreach (candidates add _)
      newCursor
    }
  }

  class JLineConsoleReader
  extends ConsoleReader(in, out, new SshTerminal)
  with Helpers.ConsoleReaderHelper {
    // working around protected/trait/java insufficiencies.
    def goBack(num: Int): Unit = back(num)
    def readOneKey(prompt: String) = {
      this.print(prompt)
      this.flush()
      this.readCharacter()
    }
    def eraseLine() = consoleReader.resetPromptLine("", "", 0)
    def redrawLineAndFlush(): Unit = { flush() ; drawLine() ; flush() }
    // override def readLine(prompt: String): String

    // A hook for running code after the repl is done initializing.
    //lazy val postInit: Unit = {
      this setBellEnabled false
      if (history.isInstanceOf[scala.tools.jline.console.history.History])
        this setHistory history.asInstanceOf[scala.tools.jline.console.history.History]

      if (completion ne NoCompletion) {
        val delimiter = new Helpers.JLineDelimiter
        val completer: Completer = scalaToJline(completion.completer())
        val argCompletor = new ArgumentCompleter(delimiter, completer) with scala.tools.jline.console.completer.Completer
        argCompletor setStrict false

        this addCompleter argCompletor
        this setAutoprintThreshold 400 // max completion candidates without warning
      }
    //}
  }

  def currentLine = consoleReader.getCursorBuffer.buffer.toString
  def redrawLine() = consoleReader.redrawLineAndFlush()
  def eraseLine() = consoleReader.eraseLine()
  // Alternate implementation, not sure if/when I need this.
  // def eraseLine() = while (consoleReader.delete()) { }
  def readOneLine(prompt: String) = consoleReader readLine prompt
  def readOneKey(prompt: String)  = consoleReader readOneKey prompt
}

object JLineIOReader {
  def apply(intp: IMain,
            in: java.io.InputStream,
            out: java.io.OutputStream): JLineIOReader =
              apply(new JLineCompletion(intp), in, out)
  def apply(comp: Completion,
            in: java.io.InputStream,
            out: java.io.OutputStream): JLineIOReader =
              new JLineIOReader(in, out, comp)
}
