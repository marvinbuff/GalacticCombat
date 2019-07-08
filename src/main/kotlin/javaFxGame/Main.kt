package javaFxGame

import javaFxGame.gui.WorldView
import javafx.application.Application

fun main(args: Array<String>) {

  Application.launch(WorldView::class.java, *args)

}