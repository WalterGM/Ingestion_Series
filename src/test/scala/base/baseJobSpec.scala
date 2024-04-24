package base

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

trait baseJobSpec {
  private val master = "local[*]"
  private val conf: SparkConf = new SparkConf()
    .setMaster(master)

  val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
  val sc: SparkContext = spark.sparkContext
}
