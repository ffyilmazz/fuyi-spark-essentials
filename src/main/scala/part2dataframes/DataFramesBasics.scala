package part2dataframes

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types._

object DataFramesBasics extends App {

  // creating a SparkSession
  val spark_session = SparkSession.builder()
    .appName("DataFrames Basics")
    .config("spark.master", "local")
    .getOrCreate()

  // reading a DF
  val firstDF = spark_session.read
    .format("json")
    .option("inferSchema", "true") // all column of dataframe will be figured out from json structure
    .load("src/main/resources/data/cars.json")
  // showing a DF
  println("> firstDF.show()")
  firstDF.show()
  println("> firstDF.printSchema()")
  firstDF.printSchema() // description of every column

  // get rows => array of rows
  println("> firstDF.take(10).foreach(println)")
  firstDF.take(10).foreach(println)

  // spark types
  val longType = LongType //case objects? O ile gosteriliyormus IntelliJ

  // schema
  // - spark yanlis schema kullanabilir eger ".option("inferSchema", "true")" kullanilirsa
  // - bu datatype'lar runtime'da kullaniliyormus. type safe data set'ler ile compile time sonra gosterecekmis
  val carsSchema = StructType(Array(
    StructField("Name", StringType), // StructField("Name", StringType, nullable = true) => eger null degerler varsa (default true)
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", StringType),
    StructField("Origin", StringType)
  ))

  // obtain a schema of existing dataset
  val carsDFSchema = firstDF.schema
  println(carsDFSchema) // carSchema'ya benzer birsey yazdiracak

  // read a DF with your schema
  val carsDFWithSchema = spark_session.read
    .format("json")
    .schema(carsDFSchema)
    .load("src/main/resources/data/cars.json")

  // create rows by hand
  // - Row : apply factory method?
  // - Row can contain anythig at all
  val myRow = Row("chevrolet chevelle malibu",18,8,307,130,3504,12.0,"1970-01-01","USA")

  // create DF from tuples
  val cars = Seq(
    ("chevrolet chevelle malibu",18,8,307,130,3504,12.0,"1970-01-01","USA"),
    ("buick skylark 320",15,8,350,165,3693,11.5,"1970-01-01","USA"),
    ("plymouth satellite",18,8,318,150,3436,11.0,"1970-01-01","USA"),
    ("amc rebel sst",16,8,304,150,3433,12.0,"1970-01-01","USA"),
    ("ford torino",17,8,302,140,3449,10.5,"1970-01-01","USA"),
    ("ford galaxie 500",15,8,429,198,4341,10.0,"1970-01-01","USA"),
    ("chevrolet impala",14,8,454,220,4354,9.0,"1970-01-01","USA"),
    ("plymouth fury iii",14,8,440,215,4312,8.5,"1970-01-01","USA"),
    ("pontiac catalina",14,8,455,225,4425,10.0,"1970-01-01","USA"),
    ("amc ambassador dpl",15,8,390,190,3850,8.5,"1970-01-01","USA")
  )
  // createDataFrame is heavily overloaded => createDataFrame[A <: Product](data: Seq[A](implicit ...) olani kullandi
  // kendi schema'ni kullanmak icin opsiyon yok. Cunku tuple type'lari compile time'da => spark otomatik olarak schema olusturuyormus
  val manualCarsDF = spark_session.createDataFrame(cars) // schema auto-inferred

  // note: DFs have schemas, rows do not

  // create DFs with implicits
  import spark_session.implicits._
  val manualCarsDFWithImplicits = cars.toDF("Name", "MPG", "Cylinders", "Displacement", "HP", "Weight", "Acceleration", "Year", "CountryOrigin")

  //
  println(">>> manualCarsDF.printSchema()")
  manualCarsDF.printSchema()
  println("-----")
  manualCarsDFWithImplicits.printSchema()
  /**
    * Exercise:
    * 1) Create a manual DF describing smartphones
    *   - make
    *   - model
    *   - screen dimension
    *   - camera megapixels
    *
    * 2) Read another file from the data/ folder, e.g. movies.json
    *   - print its schema
    *   - count the number of rows, call count()
    */

  // 1
  val smartphones = Seq(
    ("Samsung", "Galaxy S10", "Android", 12),
    ("Apple", "iPhone X", "iOS", 13),
    ("Nokia", "3310", "THE BEST", 0)
  )

  val smartphonesDF = smartphones.toDF("Make", "Model", "Platform", "CameraMegapixels")
  smartphonesDF.show()

  // 2
  val moviesDF = spark_session.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/movies.json")
  moviesDF.printSchema()
  println(s"The Movies DF has ${moviesDF.count()} rows")
}
