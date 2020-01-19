package galacticCombat.ui

import javafx.beans.binding.StringBinding

interface HasInfo {
  fun getInformation(): StringBinding
}