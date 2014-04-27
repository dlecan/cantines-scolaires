package appconfig

import helpers.{LoggingComponentImpl, LoggingComponent}
import pdf.{MenusForPeriodParserComponentImpl, ITextPdfParserComponent, FichierMenusParserComponent, PdfParserComponent}
import repositories.MenuRepositoryComponent
import repositories.MenuRepositoryComponentImpl
import services.{ServicesComponentImpl, ServicesComponent}

trait Context
  extends LoggingComponent
  with PdfParserComponent
  with FichierMenusParserComponent
  with MenuRepositoryComponent
  with ServicesComponent

trait ProductionContext extends Context
  with LoggingComponentImpl
  with ITextPdfParserComponent
  with MenusForPeriodParserComponentImpl
  with MenuRepositoryComponentImpl
  with ServicesComponentImpl