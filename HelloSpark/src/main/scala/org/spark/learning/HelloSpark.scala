package org.spark.learning

import org.apache.spark.sql.SparkSession
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.DataFrame

import java.util.Properties
import scala.io.Source

object HelloSpark extends Serializable {
  @transient lazy val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {
    logger.info("Creating SparkSession...")

    val sparkConf = getSparkConf
    logger.info(s"SparkConf => \n${
      sparkConf.getAll.foldRight("\n")((pair, acc) => acc + s"${pair._1} -> ${pair._2}\n").trim
    }")

    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()

    logger.info("Leading Survey Data...")
    val surveyDf = loadSurveyDF(spark, args(0))
    logger.info("Performing Transformations on loaded Data...")
    val transformedDF = performTransformations(surveyDf)
    transformedDF.explain()
    transformedDF.show()

    logger.info("Stopping SparkSession...")
    spark.stop()
  }

  def performTransformations(df: DataFrame): DataFrame = {
    df.where("Age < 40")
      .select("Age", "Gender", "Country", "state")
      .groupBy("Country")
      .count()
  }

  def loadSurveyDF(spark: SparkSession, dataPath: String): DataFrame = {
    spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv(dataPath)
  }

  def getSparkConf: SparkConf = {
    val sparkConf = new SparkConf
    val props = new Properties
    props.load(Source.fromFile("spark.conf").bufferedReader())
    props.forEach((k, v) => sparkConf.set(k.toString, v.toString))
    sparkConf
  }

}

