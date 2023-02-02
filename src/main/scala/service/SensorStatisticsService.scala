package com.luxoft.sensor.statistics
package service

import dto.{SensorDataDto, SensorStatisticsData}

import zio.blocking.Blocking
import zio.stream.ZStream
import zio.{ZIO, blocking}

trait SensorStatisticsService {

  def calcSensorStatistics(sensorData: ZStream[blocking.Blocking, Throwable, SensorDataDto]): ZIO[Blocking, Throwable, SensorStatisticsData]

}
