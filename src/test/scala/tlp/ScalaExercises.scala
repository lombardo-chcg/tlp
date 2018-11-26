package tlp

import org.scalatest.FunSuite


//All of these tests come from going through this https://www.scala-exercises.org/shapeless/polymorphic_function_values
class ScalaExercises extends FunSuite {
  //Polymorphic Function Values
  test("Polymorphic FunctionValues") {
    import shapeless.poly._
    import shapeless.Poly1
    // This is a KIND not a TYPE since now Seq[T] is specified, rather just Seq->Option
    // ~> is the magic here.
    object choose extends (Seq ~> Option) {
      def apply[T](s: Seq[T]) = s.headOption
    }

    assert(choose(Seq(1,2,3,4)) == Some(1))
    assert(choose(Seq('a','b')) == Some('a'))

    def pairApply(f: Seq ~> Option) = (f(Seq(1, 2, 3)), f(Seq('a', 'b', 'c')))
    assert(pairApply(choose) == (Some(1),Some('a')))

    //Interestingly intellij is not happy with the type inference here, but it compiles correctly and runs
    assert(List(Seq(1, 3, 5), Seq(2, 4, 6)).map(choose) == List(Some(1),Some(2)))



    // Attemptimg to extend the implicits to new case classes
    abstract trait Animal{ def weight: Double}
    case class Cat(meow: String, weight: Double) extends Animal
    case class Dog(woof: String, weight: Double) extends Animal

    object size extends Poly1 {
      implicit def caseInt = at[Int](x ⇒ 1)
      implicit def caseString = at[String](_.length)
      implicit def caseAnimal = at[Animal](_.weight)
      implicit def caseTuple[T, U](implicit st: Case.Aux[T, Int], su: Case.Aux[U, Int]) =
        at[(T, U)](t ⇒ size(t._1) + size(t._2))
    }


    assert(size(23) == 1 )
    assert(size("foo") == 3)
    assert(size((23, "foo")) == 4)
    assert(size(((23, "foo"), 13)) == 5)


    // TODO: Why isn't this working?
    //assert(size(((Dog("woof",10), "foo"), Cat("meow",100))) ==113)

  }


}
