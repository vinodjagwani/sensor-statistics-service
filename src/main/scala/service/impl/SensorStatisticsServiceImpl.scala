package com.luxoft.sensor.statistics
package service.impl

import dto.{HumidityData, SensorDataDto, SensorStatisticsData}
import service.SensorStatisticsService

import zio.blocking.Blocking
import zio.stream.ZStream
import zio.{ZIO, blocking}

class SensorStatisticsServiceImpl extends SensorStatisticsService {


  override def calcSensorStatistics(sensorDataDto: ZStream[blocking.Blocking, Throwable, SensorDataDto]): ZIO[Blocking, Throwable, SensorStatisticsData] = {
    sensorDataDto.broadcast(3, 10).use {
      case stream1 :: stream2 :: stream3 :: Nil => calSensorStatistics(stream1, stream2, stream3)
      case _ => ZIO.fail(new Exception("Fail to process sensor data"))
    }
  }

  private def calSensorStatistics(stream1: ZStream[Any, Throwable, SensorDataDto],
                                  stream2: ZStream[Any, Throwable, SensorDataDto],
                                  stream3: ZStream[Any, Throwable, SensorDataDto]) =
    for {
      successFullMeasurements <- stream1.runCount.fork
      failedMeasurements <- stream2.filter(_.humidity.isEmpty).runCount.fork
      measurementsBySensor <- stream3.runCollect
        .map { measurements =>
          measurements.groupBy(_.sensorId)
            .map { case (sensorId, measurementsForOneSensor) =>
              val humidityValues = measurementsForOneSensor.flatMap(_.humidity)
              val calHumidity =
                if (humidityValues.isEmpty) HumidityData(None, None, None)
                else
                  HumidityData(Some(humidityValues.min),
                    Some(humidityValues.foldLeft(0)(_ + _) / humidityValues.length), Some(humidityValues.max)
                  )
              sensorId -> calHumidity
            }.toList.sortBy(_._2.avg).reverse
        }.fork
      ((successFullMeasurements, failedMeasurements), measurementsBySensor) <- successFullMeasurements.join <&> failedMeasurements.join <&> measurementsBySensor.join
    } yield SensorStatisticsData(successFullMeasurements, failedMeasurements, measurementsBySensor)
}
