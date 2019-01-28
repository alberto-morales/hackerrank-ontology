package eu.albertomorales.ontology

import eu.albertomorales.ontology.game.GameInputHelper
import eu.albertomorales.ontology.model.Brain

object Solution {

  def main(args: Array[String]) {
    val gameInput =  GameInputHelper.generateInputFromConsole()
    val resultado : List[String] = Brain.resolve(gameInput)
    resultado.foreach(x => {
      println(x)
    })
  }

}
