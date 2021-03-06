package o.lartifa.jam.engine.parser

import o.lartifa.jam.common.exception.ParseFailException
import o.lartifa.jam.model.patterns.{ContentMatcher, ParseResult}

import scala.util.Try
import scala.util.matching.Regex

/**
 * SSDL 基本模式语法解析器
 * Author: sinar
 * 2020/1/2 23:17
 */
object PatternParser extends Parser {

  /**
   * 解析基础模式
   *
   * @param string 待解析字符串
   * @return 解析结果
   */
  @throws[ParseFailException]
  def parseBasePattern(string: String): ParseResult = {
    val result = Patterns.basePattern.findFirstMatchIn(string).getOrElse(throw ParseFailException("书写内容没有以标准格式开头"))
    val stepId = Try(result.group("id").toLong).getOrElse(throw ParseFailException("步骤编号过大，过小或不合法"))
    val content = result.group("content")
    parseMatcher(content, stepId) match {
      case Some((matcher, command)) =>
        val executable = LogicStructureParser.parseLogic(command).getOrElse(throw ParseFailException("没有指令内容，或指令内容不正确！"))
        ParseResult(stepId, executable, Some(matcher))
      case None =>
        val executable = LogicStructureParser.parseLogic(content).getOrElse(throw ParseFailException("没有指令内容，或指令内容不正确！"))
        ParseResult(stepId, executable)
    }
  }

  /**
   * 解析消息内容捕获器
   *
   * @param string 待解析字符串
   * @param stepId 步骤 ID
   * @return 解析结果
   */
  private def parseMatcher(string: String, stepId: Long): Option[(ContentMatcher, String)] = {
    import ContentMatcher.Constant
    Patterns.matcherPattern.findFirstMatchIn(string).map(result => {
      val `type`: ContentMatcher.Type = result.group("type") match {
        case Constant.EQUALS => ContentMatcher.EQUALS
        case Constant.CONTAINS => ContentMatcher.CONTAINS
        case Constant.ENDS_WITH => ContentMatcher.ENDS_WITH
        case Constant.STARTS_WITH => ContentMatcher.STARTS_WITH
        case s if s.startsWith(Constant.REGEX) => ContentMatcher.REGEX
      }
      val regex: Option[Regex] = if (`type` == ContentMatcher.REGEX) {
        Some(Try(result.group("keyword").r).getOrElse(throw ParseFailException("提供的正则表达式不正确")))
      } else None
      (ContentMatcher(stepId, `type`, result.group("keyword"), regex), result.group("command"))
    })
  }
}
