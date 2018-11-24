package tlp

import org.scalatest.FunSuite

class VanillaScalaTest extends FunSuite {

  test("generate simple methods using macros") {
    import MacroExample._
    hello("world")

    val sample = List("i", "ii", "iii", "ii")
    assert(longestStrings(sample) == List("iii"))
    assert(longestStrings(sample :+ "nnn") == List("iii", "nnn"))
  }
}
