package utils

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

abstract class baseJob {

  def executeJob(spark: SparkSession, config: Config)

  def main(args: Array[String]): Unit = {

    val mandatoryConfigurations =
      args
        .filter(conf => conf.startsWith("-D"))
        .map(conf =>
          conf
            .replace("-D", "")
            .split("=")
        )
        .map(v => (v(0), v(1)))
        .toMap
    val config: Config = ConfigFactory.empty()
    val newConfig: Config = mandatoryConfigurations.keys.foldLeft(config) {
      (configuration, current) =>
        if (mandatoryConfigurations(current).contains(".conf"))
          ConfigFactory
            .parseResources(mandatoryConfigurations(current))
            .withFallback(configuration)
        else
          configuration
            .withValue(
              current,
              ConfigValueFactory.fromAnyRef(mandatoryConfigurations(current))
            )
    }
    val sparkConfig = newConfig
      .getConfig("spark")
      .entrySet()
      .toList
      .map(values =>
        (values.getKey, values.getValue.unwrapped().asInstanceOf[String])
      )
      .toMap
    val sConfig = new SparkConf()
    sConfig.setAll(sparkConfig)
    val spark = SparkSession.builder().config(sConfig).getOrCreate()

    executeJob(spark, newConfig)

  }

}
