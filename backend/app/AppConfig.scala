package appconfig

import parsers.{MenusForPeriodParserComponentImpl, ITextPdfParserComponent, FichierMenusParserComponent, PdfParserComponent}
import repositories.MenuRepositoryComponent
import repositories.MenuRepositoryComponentImpl
import services.{ServicesComponentImpl, ServicesComponent}

trait Registry
  extends PdfParserComponent
  with FichierMenusParserComponent
  with MenuRepositoryComponent
  with ServicesComponent

trait ProdEnvironment extends Registry
  with ITextPdfParserComponent
  with MenusForPeriodParserComponentImpl
  with MenuRepositoryComponentImpl
  with ServicesComponentImpl