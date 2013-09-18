package parsers

import scala.util.parsing.combinator.Parsers
import com.itextpdf.text.pdf.{PdfName, PdfObject, PdfDictionary, PdfReader}
import models.Menu
import com.itextpdf.text.pdf.parser.{PdfContentStreamProcessor, ContentByteUtils}
import play.api.libs.json.JsValue
import org.joda.time.DateTime
import scala.util.parsing.input.{Position, Reader}
import java.io.InputStream


object PDFParsers extends Parsers {

  type Elem = String

  implicit def extractMenus(str: String, month: String) = new Parser[Menu] {
    def apply(in: PDFParsers.Input): PDFParsers.ParseResult[Menu] = {

      def parseDate(str: String): DateTime = {
        DateTime.now
      }

      val data = in.first.split("\\r?\\n")

      data.size match {
        // Nothing to parse ??
        case 0 => Failure(s"Impossible to parse lines in $data", in.rest)

        // Just the date, no content
        case 1 => Success(Menu(parseDate(data(0)), ""), in.rest)
          // Special day
        case 2 => Success(Menu(parseDate(data(0)), data(1)), in.rest)
        case n => {
          Success(Menu(parseDate(data(0)), data.drop(0).mkString("\n")), in.rest)
        }
      }
    }
  }
}

class PdfReader(is: InputStream) extends Reader[String] {
  def first: String = ???

  def pos: Position = ???

  def rest: Reader[String] = ???

  def atEnd: Boolean = ???
}