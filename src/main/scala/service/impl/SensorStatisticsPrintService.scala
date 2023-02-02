package com.luxoft.sensor.statistics
package service.impl

import dto.SensorStatisticsData
import service.{FileReadService, ReportPrintService, SensorStatisticsService}

import zio.console.putStrLn
import zio.stream.ZStream
import zio.{ExitCode, Task, URIO, ZIO}

import java.io.File

class SensorStatisticsPrintService(fileReadService: FileReadService, sensorStatisticsService: SensorStatisticsService)
  extends ReportPrintService {


  override def printReport(files: List[String]): URIO[zio.ZEnv, ExitCode] = {
    ZIO.foreach(files) { path =>
      var processedFiles = 0
      val sensorDataFromFile = listFiles(path).flatMap(file => {
        processedFiles += 1
        fileReadService.readFile(file)
      })
      sensorStatisticsService.calcSensorStatistics(sensorDataFromFile).tap(sensorData => {
        putStrLn(formatSensorData(processedFiles, sensorData))
      })
    }.exitCode
  }

  private def listFiles(path: String): ZStream[Any, Throwable, File] =
    ZStream.fromEffect(Task {
      val directory = new File(path)
      if (directory.exists && directory.isDirectory)
        directory.listFiles.filter(_.isFile).toList
      else
        List.empty
    }).flatMap(files => ZStream.fromIterable(files))


  private def formatSensorData(processedFiles: Long, sensorStatisticsData: SensorStatisticsData): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append("Num of processed files: ")
    stringBuilder.append(processedFiles)
    stringBuilder.append("\n")
    stringBuilder.append("Num of processed measurements: ")
    stringBuilder.append(sensorStatisticsData.successfulMeasurements)
    stringBuilder.append("\n")
    stringBuilder.append("Num of failed measurements: ")
    stringBuilder.append(sensorStatisticsData.failedMeasurements)
    stringBuilder.append("\n\n")
    stringBuilder.append("Sensors with highest avg humidity:")
    stringBuilder.append("\n\n")
    stringBuilder.append("sensor-id,min,avg,max")
    stringBuilder.append("\n")
    stringBuilder.append(sensorStatisticsData.humidityData.map {
      x => s"${x._1},${x._2.min.getOrElse("NaN")},${x._2.avg.getOrElse("NaN")},${x._2.max.getOrElse("NaN")}"
    }.mkString("\n"))
    stringBuilder.toString()
  }
}
