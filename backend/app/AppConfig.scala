package appconfig

import parsers.{MenusForPeriodParserComponentImpl, ITextPdfParserComponent, FichierMenusParserComponent, PdfParserComponent}
import repositories.MenuRepositoryComponent
import repositories.MenuRepositoryComponentImpl
import services.{ServicesComponentImpl, ServicesComponent}

trait Context
  extends PdfParserComponent
  with FichierMenusParserComponent
  with MenuRepositoryComponent
  with ServicesComponent

trait ProductionContext extends Context
  with ITextPdfParserComponent
  with MenusForPeriodParserComponentImpl
  with MenuRepositoryComponentImpl
  with ServicesComponentImpl