package eu.albertomorales.ontology.game

import scala.io.Source

import eu.albertomorales.ontology.model.dto.GenericGameInput;
import eu.albertomorales.ontology.game.GameInputHelper.generateInputFromSource
import eu.albertomorales.ontology.model.GameInput
import eu.albertomorales.ontology.model.taxonomy.{FamilyTree, FamilyTreeBuilder}
import eu.albertomorales.ontology.model.dto._

object GameInputHelper {

  def generateInputFromFile(filePath: String) : GameInput = {
    return generateInputFromSource(Source.fromString(filePath))
  }

  def generateInputFromConsole() : GameInput = {
    return generateInputFromSource(Source.stdin)
  }

  def generateInputFromSource(source: Source) : GameInput = {
    val lines = source.getLines
    var taxonomyString : String = ""
    var numOfQuestions : Int = 0
    var numLineaNumeroQueries : Int = 0
    val questionsBuffer : scala.collection.mutable.ListBuffer[String]
        = scala.collection.mutable.ListBuffer()
    val queriesBuffer : scala.collection.mutable.ListBuffer[String]
        = scala.collection.mutable.ListBuffer()
    var nLinea : Int = 0
    for (linea <- lines) {
      if ("" != linea.trim) {
        nLinea = nLinea + 1
        if (nLinea == 1) {

        } else if (nLinea == 2 ) {
          taxonomyString = linea
        } else if (nLinea == 3) {
          numOfQuestions = linea.trim.toInt
          numLineaNumeroQueries = 3 + numOfQuestions + 1
        } else if (nLinea < numLineaNumeroQueries) { // esto son Questions
          questionsBuffer += linea
        } else if (nLinea == numLineaNumeroQueries) {

        } else if (nLinea > numLineaNumeroQueries) { // esto son Queries
          queriesBuffer += linea
        }
      }
    }
    val tree = new FamilyTreeBuilder().load(taxonomyString)
    val questions = Question.parseLines(questionsBuffer.toList)
    val queries = Query.parseLines(queriesBuffer.toList)
    new GenericGameInput(tree, questions, queries)
  }

}



object GameInputHelperTest extends App {

  def generateTestData(): Source = {
    val cadena =
      """6
        |Animals ( Reptiles Birds ( Eagles Pigeons Crows ) )
        |5
        |Reptiles: Why are many reptiles green?
        |Birds: How do birds fly?
        |Eagles: How endangered are eagles?
        |Pigeons: Where in the world are pigeons most densely populated?
        |Eagles: Where do most eagles live?
        |4
        |Eagles How en
        |Birds Where
        |Reptiles Why do
        |Animals Wh
      """.stripMargin
    Source.fromString(cadena)
  }

  def generateTestDataFromFile(filePath : String) : Source = {
    Source.fromFile(filePath)
  }

  generateInputFromSource(generateTestData)
  println("ready")
}
