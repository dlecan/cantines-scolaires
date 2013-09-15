import com.itextpdf.text.Rectangle
import java.io.PrintWriter

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser._

object ExtractPageContent {

  val xOffset = 80
  val yOffset = 16

  val rowWidth = 160
  val colHeigth = 121

  def parsePdf(pdf: String): List[String] = {
    val reader = new PdfReader(pdf)

    val results = for {
      row <- 0 to 3
      col <- 0 to 4
    } yield {
      val rect = getRectangle(row, col)
      val filter = new RegionTextRenderFilter(rect)
      val strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter)

      PdfTextExtractor.getTextFromPage(reader, 1, strategy)
    }
    reader.close()
    results.toList
  }

  def main(args: Array[String]) = {
    val results = ExtractPageContent.parsePdf(getClass.getResource("/menu.pdf").toExternalForm)

    results.foreach {
      r =>
        println("=================================================================================")
        println(r)
        println("=================================================================================")
        println()
    }

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