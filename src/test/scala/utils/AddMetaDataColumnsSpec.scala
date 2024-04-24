package utils

import base.baseJobSpec
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{
  DateType,
  StringType,
  StructField,
  StructType
}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AddMetaDataColumnsSpec
    extends AnyFlatSpec
    with Matchers
    with baseJobSpec {
  val data =
    Seq(Row("1", "Test", null), Row(null, null, null), Row("2", "Test2", "two"))
  val schema = StructType(
    Seq(
      StructField("number", StringType),
      StructField("value", StringType),
      StructField("description", StringType)
    )
  )
  val testDf = spark.createDataFrame(sc.parallelize(data), schema)
  val targetSchema = StructType(
    Seq(
      StructField("id", StringType, true),
      StructField("loadDate", DateType, false),
      StructField("batchID", StringType, false)
    )
  )

  "Given a dataframe" should "Add metadata columns id, loadDate and batchID" in {
    val metadataDF = testDf.transform(AddMetaDataColumns())
    metadataDF.select("id", "loadDate", "batchID").schema shouldBe targetSchema
  }

}
