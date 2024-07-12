package akash.spark.examples

import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties
import scala.io.Source

object HelloSpark extends Serializable {

  @transient lazy val logger: Logger =Logger.getLogger(getClass.getName)
  def main(args: Array[String]): Unit = {

    if(args.length==0){
      logger.error("Usage: HelloSpark filename")
      System.exit(1)
    }

    /*Creating a spark session, spark session is a singleton class. so each program can have only one active session. spark session is a driver*/

    logger.info("Starting Hello Spark111")
    logger.info("Starting Hello Spark application started.....")
    val spark=SparkSession.builder()
      //.appName("Hello Spark")
     // .master("local[3]")
      .config(getSparkAppConf)
      .getOrCreate()

    //logger.info("spark.conf="+spark.conf.getAll.toString())
    //process your Data

    val surveyRawDF=loadSurveyDF(spark,args(0))
    val partitionedSurveyDF=surveyRawDF.repartition(2)
    val countDF = countByCountry(partitionedSurveyDF)


    //countDF.show()

    logger.info(countDF.collect().mkString("->"))

    //Stopping the driver
    logger.info("Finished Hello Spark")
    logger.info("Finished Hello Spark ended........")

    scala.io.StdIn.readLine()
    spark.stop()
  }

  def countByCountry(surveyDF: DataFrame): DataFrame ={
    val filterSurveyDF=surveyDF.where("Age < 40")
    val groupedSurveyDF = filterSurveyDF.select("Age", "Gender", "Country", "state")
      .groupBy("Country")
      .count()
    groupedSurveyDF
  }

  def loadSurveyDF(spark: SparkSession,dataFile: String): DataFrame ={
    spark.read
      .option("header","true")
      .option("inferSchema","true")
      .csv(dataFile)
  }

  def getSparkAppConf: SparkConf ={
    val sparkAppConf= new SparkConf

    //Set all Spark Configs
    val props = new Properties
    props.load(Source.fromFile("spark.conf").bufferedReader())
    props.forEach((k,v)=>sparkAppConf.set(k.toString,v.toString))

    //This is a fix for Scala 2.11
    //import scala.collection.JavaConverters._
    //props.asScala.foreach(kv => sparkAppConf.set(kv._1, kv._2))
    sparkAppConf
  }
}