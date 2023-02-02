package com.luxoft.sensor.statistics
package service

import dto.SensorDataDto

import zio._
import zio.stream.ZStream

import java.io.File

trait FileReadService {

  def readFile(file: File): ZStream[blocking.Blocking, Throwable, SensorDataDto]

}
