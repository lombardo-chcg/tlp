package tlp

import org.scalatest.FunSuite
import shapeless.{Generic, HList, LabelledGeneric}
import shapeless.ops.record.ToMap

case class Cat(catName: String, weight: Double)
case class Rabbit(rabbitName: String, length: Double)
case class Bat(name: String, wingSpan: Double, weight: Double)
case class Eagle(name: String, wingSpan: Double)

class ShapelessTest extends FunSuite {
  val genCat = Generic[Cat]
  val genRabbit = Generic[Rabbit]

  test("shapeless example 1 - convert between case classes using Generic") {
    // create my cat
    val myCatsName = "Louie"
    val myCat: Cat = Cat(myCatsName, 17.9)
    // lift my cat into a generic representation
    val myGenericCat: genCat.Repr = genCat.to(myCat)
    // turn my cat into a rabbit
    val myRabbit: Rabbit = genRabbit.from(myGenericCat)
    assert(myRabbit.rabbitName == myCatsName)
  }

  test("shapeless example 2 - materialize a labelled generic Record and convert it to another case class") {
    import shapeless.record._

    /*
       a LabelledGeneric is a Generic but with each field "Tagged"
       "records" are how the shapeless library represents LabelledGenerics
       read more: https://github.com/milessabin/shapeless/blob/cb973aa10f066782e8543fdce8d44b2d83996801/core/src/main/scala/shapeless/records.scala#L35

      @tparam T the type of the case class being converted to a record
      @tparam R the dependant type - the labelled generic.
      @param  myCaseClassInstance the instance to be converted
      @iparam evidence a bit of magic.  instruct the compiler to perform an implicit search for the LabelledGeneric[T],
                                        that has retained the type member R.
                                        shapeless appears to generate a macro class to meet this implicit need, see https://github.com/milessabin/shapeless/blob/cb973aa10f066782e8543fdce8d44b2d83996801/core/src/main/scala/shapeless/generic.scala#L157
                                        LabelledGeneric.Aux[T, R] == LabelledGeneric[T]{ type Repr = R }
     */
    def toRecord[T, R <: HList](myCaseClassInstance: T)(
      implicit evidence: LabelledGeneric.Aux[T, R],
    ) = {
      LabelledGeneric[T].to(myCaseClassInstance)
    }

    val myBatsName = "Martha"
    val myBat: Bat = Bat(myBatsName, 17.9, 15.5)
    val myBatRecord = toRecord(myBat)
    // use the "getter" syntax on our new type
    assert(myBatRecord.get('name) == myBatsName)

    // "Remove the field associated with the singleton typed key k, returning both the corresponding value and the updated record"
    // source https://github.com/milessabin/shapeless/blob/cb973aa10f066782e8543fdce8d44b2d83996801/core/src/main/scala/shapeless/syntax/records.scala#L75
    val slimBat = myBatRecord.remove('weight)
    // the generic is _2 of the tuple
    println(myBatRecord, slimBat._2)
    val lGenEagle = LabelledGeneric[Eagle]
    // convert the bat into an eagle
    val batReborn: Eagle = lGenEagle.from(slimBat._2)
    assert(batReborn.name == myBat.name)
  }

  test("shapeless example 3 - convert case class to Record, then a Map") {
    // this example shows how to use the dependent type, R, as a type param within the same argument group
    // this usage pattern is the reason for `Aux` to exist
    def makeMap[T, R <: HList](myCaseClassInstance: T)(
      implicit evidence: LabelledGeneric.Aux[T, R],
               toMap: ToMap[R]
    ) = {
      val lGen = LabelledGeneric[T].to(myCaseClassInstance)
      toMap(lGen)
    }
    // create my bat
    val myBatsName = "Martha"
    val myBat: Bat = Bat(myBatsName, 17.9, 15.5)
    // convert to standard scala map with shapeless flavored keys
    val myBatMap = makeMap(myBat)
    println(s"bat map created: $myBatMap, key type = ${myBatMap.keys.head.getClass}")
    // make keys vanilla
    val vanillaScalaMap = myBatMap.map {
      case (k,v) => k.toString.tail -> v
    }
    println(s"vanilla bat map created: $vanillaScalaMap, key type = ${vanillaScalaMap.keys.head.getClass}")
  }
}
