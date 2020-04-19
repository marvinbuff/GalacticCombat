package galacticCombat.ui

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.Node
import javafx.scene.image.Image

class InfoPanel {
  private var infoSource: HasInfo? = null
  val titleProperty: StringProperty = SimpleStringProperty("")
  val textProperty: StringProperty = SimpleStringProperty("")
  val imageProperty: ObjectProperty<Image> = SimpleObjectProperty()
  val bottomChildrenProperty: SimpleListProperty<Node> = SimpleListProperty()

  fun set(source: HasInfo) {
    reset()
    infoSource = source
    titleProperty.set(source.getTitle())
    textProperty.bind(source.getInformation())
    imageProperty.set(source.getTexture())
    source.activate(this)
  }

  fun reset() {
    imageProperty.unbind()
    titleProperty.unbind()
    textProperty.unbind()
    imageProperty.set(null)
    titleProperty.set("")
    textProperty.set("")
    infoSource?.deactivate(this)
    infoSource = null
  }

  fun update(source: HasInfo) {
    if (source == infoSource)
      set(source)
  }
}