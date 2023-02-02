package com.luxoft.sensor.statistics

import service.impl.{CVSFileReadService, SensorStatisticsPrintService, SensorStatisticsServiceImpl}

import zio._

object Application extends App {

  def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val fileReadService = new CVSFileReadService()
    val sensorStatisticsService = new SensorStatisticsServiceImpl()
    val sensorStatisticsPrintService = new SensorStatisticsPrintService(fileReadService, sensorStatisticsService)
    sensorStatisticsPrintService.printReport(args)
  }
}
