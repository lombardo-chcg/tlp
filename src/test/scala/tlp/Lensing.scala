package tlp

import org.scalatest.FunSuite
import shapeless._
import shapeless.ops.record.{Selector, Updater}
import shapeless.labelled.FieldType



//This test is used to explore how far lensing can be pushed
class Lensing extends FunSuite {
  abstract trait Animal
  case class Cat(catName: String, weight: Double) extends Animal
  case class Rabbit(rabbitName: String, length: Double) extends Animal
  case class Bat(name: String, wingSpan: Double, weight: Double) extends Animal
  case class Eagle(name: String, wingSpan: Double) extends Animal
  case class Farm(name: String, animals: Seq[Animal])

  test("Basic Lens") {
    val catScale = lens[Cat] >> 'weight
    val fatCat = Cat("monopolyman",20000)
    assert(catScale.get(fatCat) == 20000)
  }

  test("Lens into a list") {
    //Lenses Figure out how to do the below
//    val catScale = lens[Cat] >> 'weight
//    val farmLens = lens[Farm] >> 'animals
//    def getCatWeights(f: Farm): Seq[Option[Int]] = farmLens.get(f).flatMap(catScale)
//
//    val fatCat = Cat("monopolyman",20000)
//    val skinnyCat = Cat("skinny",100)
//    val batCat = Bat("cat",10.0,100)
//    val farm = Farm("oldmcdonald", Seq(fatCat,batCat,skinnyCat))
//
//    assert(getCatWeights(farm) == Seq(Some(20000),None,Some(100)))
  }

}
