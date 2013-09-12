import com.itextpdf.text.Rectangle
import java.io.PrintWriter

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser._

object ExtractPageContent {

  val xOffset = 59
  val yOffset = 48

  val rowWidth = 159
  val colHeigth = 119

  def parsePdf(pdf: String) = {
    val reader = new PdfReader(pdf)

    for {
      row <- 0 to 3
      col <- 0 to 4
    } yield {
      val rect = getRectangle(row, col)
      println(s"($row, $col) =================================================================================")
      println(rect.betterToString)
      println()

      val filter = new RegionTextRenderFilter(rect)
      val strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter)

      println(PdfTextExtractor.getTextFromPage(reader, 1, strategy))
      println("=================================================================================")
      println()
    }
    reader.close()
  }

  def main(args: Array[String]) = {
    ExtractPageContent.parsePdf(getClass.getResource("/menu.pdf").toExternalForm)
  }

  def getRectangle(row: Int, col: Int) = {
    def xRow(x: Int) = xOffset + x * colHeigth
    def yCol(y: Int) = yOffset + y * rowWidth

    new Rectangle(xRow(row), yCol(col), xRow(row + 1), yCol(col + 1))
  }

  implicit class RectangleWithBetterToString(rect: Rectangle) {
    def betterToString() = {
      "(" + rect.getLeft + ", " + rect.getBottom + "), (" + rect.getRight + ", " + rect.getTop + ")"
    }
  }

}