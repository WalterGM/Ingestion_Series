package utils

import base.baseJobSpec
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RemoveSpacesSpec extends AnyFlatSpec with Matchers with baseJobSpec {

  val data =
    Seq(Row("1", "Test ", null), Row(" 2 ", " Test2 ", "  two"))
  val schema = StructType(
    Seq(
      StructField("number", StringType),
      StructField("value", StringType),
      StructField("description", StringType)
    )
  )
  val testDf = spark.createDataFrame(sc.parallelize(data), schema)

  "Given a dataframe with leading and trailing spaces in each field" should "Remove the extra spaces" in {

    val removeSpacesDf = testDf.transform(RemoveSpaces())
    removeSpacesDf.collect() should contain allOf (Row("1", "Test", null), Row("2", "Test2", "two"))

  }
}
