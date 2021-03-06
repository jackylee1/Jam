package o.lartifa.jam

import cc.moecraft.logger.format.AnsiColor
import o.lartifa.jam.common.config.JamConfig._
import o.lartifa.jam.common.config.SystemConfig
import o.lartifa.jam.cool.qq.CoolQQLoader
import o.lartifa.jam.engine.JamLoader
import o.lartifa.jam.pool.JamContext

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * 唤醒果酱
 *
 * Author: sinar
 * 2020/1/2 21:54 
 */
object Bootloader {
  def main(args: Array[String]): Unit = {
    val client = CoolQQLoader.createCoolQQClient()
    JamContext.logger.set(client.getLoggerInstanceManager.getLoggerInstance(name, SystemConfig.debugMode))
    Await.result(JamLoader.init(), Duration.Inf)
    JamContext.clientConfig.getAndSet(client.getConfig)
    JamContext.httpApi.getAndSet(() => client.getAccountManager.getNonAccountSpecifiedApi)
    client.startBot()
    JamContext.logger.get().log(s"${AnsiColor.GREEN}${name}已苏醒")
  }
}
