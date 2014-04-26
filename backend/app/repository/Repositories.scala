package repositories

import models.Menu


trait MenuRepository {

  def saveAll(menus: List[Menu])

}

trait MenuRepositoryComponent {

  val menuRepository: MenuRepository

}

trait MenuRepositoryComponentImpl extends MenuRepositoryComponent {

  override val menuRepository: MenuRepository = new InMemoryMenuRepositoryImpl

  class InMemoryMenuRepositoryImpl extends MenuRepository {
    override def saveAll(menus: List[Menu]): Unit = ???
  }
}