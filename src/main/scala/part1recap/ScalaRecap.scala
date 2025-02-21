package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ScalaRecap extends App {

  // values and variables
  val aBoolean: Boolean = false

  // expressions
  val anIfExpression = if(2 > 3) "bigger" else "smaller"

  // instructions vs expressions
  val theUnit: Unit = println("Hello, Scala") // Unit = "no meaningful value" = void in other languages

  // functions
  def myFunction(x: Int) = 42

  // OOP
  class Animal
  class Cat extends Animal
  // interface known as trait
  // abstract methods can be defined in traits
  // classes can use traits with "with" keyword
  trait Carnivore {
	 // we're obligated to actually implement these abstract methods in the
	 // class that extends this trait
    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch!")
  }

  // singleton pattern
  // 1. both the type and the single instance that this type can have
  // 2. singleton can be expanded with curly brace and with additional
  // implementation with methods and members
  object MySingleton  

  // companions
  // 1. If you have class or trait and a singleton object with the same name
  //    in the same file, it is called companions.
  // 2. trait and object Carnivore are called "companions"
  // 3. classes or traits and singleton objects can see their private members
  object Carnivore

  // generics
  // 1. [A] is type argument 
  // 2. MyList can be applied to any type and this type is denoted generically
  //    with this name A.
  // 3. MyList[+A] => Covariant
  trait MyList[A]

  // method notation
  // 1. + is actually a method
  val x = 1 + 2
  // 2. any method with one argument can be infixed like below => .+(2)
  val y = 1.+(2)
  // prefix and postfix notations are discussed in the beginners course

  // Functional Programming
  // 1. Functions in Scala are actually instances of a trait called function
  //    1,2,3,..,22
  // 2. below is called anonymous function or lambda in Scala
  val incrementer: Int => Int = x => x + 1
  val incremented = incrementer(42) // will return 43
  // 3. functional programming is all about working with these functions as 
  //    values

  // - map, flatMap, filter => are called higher order functions
  // - higher order functions can receive functions as arguments
  // - higher order functions are used a lot when we process data sets and
  //   resilient distributed data sets
  val processedList = List(1,2,3).map(incrementer)

  // Pattern Matching
  val unknown: Any = 45
  val ordinal = unknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  // try-catch
  try {
    throw new NullPointerException
  } catch {
	 // catch is done via pattern matching
    case _: NullPointerException => "some returned value"
    case _: Throwable => "something else"
  }

  // Future
  import scala.concurrent.ExecutionContext.Implicits.global
  val aFuture = Future {
    // some expensive computation, runs on another thread
    42
  }

  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"I've found $meaningOfLife")
    case Failure(ex) => println(s"I have failed: $ex")
  }

  // Partial functions (discussed in advanced Scala course)
  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
    case _ => 999
  }

  // Implicits

  // 1) auto-injection by the compiler
  def methodWithImplicitArgument(implicit x: Int) = x + 43
  implicit val implicitInt = 67
  val implicitCall = methodWithImplicitArgument // 67 is automaticall is injected here

  // 2) implicit conversions - implicit defs
  case class Person(name: String) {
    def greet = println(s"Hi, my name is $name")
  }

  implicit def fromStringToPerson(name: String) = Person(name)
  "Bob".greet // fromStringToPerson("Bob").greet

  // 3) implicit conversion - implicit classes
  implicit class Dog(name: String) {
    def bark = println("Bark!")
  }
  // 3.1. compiler will automatically convert this string into a dog with that content as an 
  //      argument and it will call the "bark" method on that.
  "Lassie".bark 

	
  /*
       compiler figures out which implicit to inject here into te argument list is done by looking into
	 three areas:
    - local scope
    - imported scope => Future
    - companion objects of the types involved in the method call
	 
	 Just need to know about local scope and imported scope for this course because we are only going to
	 use just few implicits when we are converting data frames and data sets
   */

}
