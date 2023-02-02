package com.luxoft.sensor.statistics
package service

import service.impl.{CVSFileReadService, SensorStatisticsServiceImpl}

import zio.test.{DefaultRunnableSpec, _}

import java.io.File

object SensorStatisticsServiceImplTest extends DefaultRunnableSpec {

  val csvFilePath1 = s"${System.getProperty("user.dir")}/src/test/resources/csv/leader-1.csv"
  val csvFilePath2 = s"${System.getProperty("user.dir")}/src/test/resources/csv/leader-2.csv"

  override def spec = suite("SensorStatisticsServiceImplTestSpec")(

    testM("Test successMeasurements sensor measurements") {
      val fileReadService = new CVSFileReadService()
      val sensorData = fileReadService.readFile(new File(csvFilePath1))
      val sensorStatisticsService = new SensorStatisticsServiceImpl()
      sensorStatisticsService.calcSensorStatistics(sensorData).map(sd => {
        assertTrue(sd.successfulMeasurements == 2)
      })
    },

    testM("Test failedMeasurements sensor measurements") {
      val fileReadService = new CVSFileReadService()
      val sensorData = fileReadService.readFile(new File(csvFilePath2))
      val sensorStatisticsService = new SensorStatisticsServiceImpl()
      sensorStatisticsService.calcSensorStatistics(sensorData).map(sd => {
        assertTrue(sd.failedMeasurements == 1)
      })
    }
  )

}
