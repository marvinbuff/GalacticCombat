package galacticCombat.ui

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.scene.image.Image

class InfoPanel {
  val titleProperty: StringProperty = SimpleStringProperty("")
  val textProperty: StringProperty = SimpleStringProperty("")
  val imageProperty: ObjectProperty<Image> = SimpleObjectProperty<Image>()

  fun reset() {
    imageProperty.unbind()
    titleProperty.unbind()
    textProperty.unbind()
    imageProperty.set(null)
    titleProperty.set("")
    textProperty.set("")
  }
}