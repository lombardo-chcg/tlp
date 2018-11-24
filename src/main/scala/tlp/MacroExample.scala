package tlp

import scala.reflect.macros.blackbox

object MacroExample {
  def hello(name: String): Unit = macro helloImpl

  def longestStrings(a: List[String]): List[String] = macro longestStringsMacro

  def helloImpl(c: blackbox.Context)(name: c.Expr[String]): c.Expr[Unit] = {
    import c.universe._
    c.Expr(q"""println("hello, " + ${name.tree} + ", this is macro-gen'ed!")""")
  }

  def longestStringsMacro(c: blackbox.Context)(a: c.Tree): c.Tree = {
    import c.universe._
    println(s"inside longestStringsMacro with argument '$a'...")
    q"""
       val maxLen = $a.map(_.size).max
       $a.filter(_.size == maxLen)
     """
  }
}
