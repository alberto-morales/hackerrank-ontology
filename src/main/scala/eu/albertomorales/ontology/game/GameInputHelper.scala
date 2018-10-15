package eu.albertomorales.ontology.game

import eu.albertomorales.ontology.game.GameInputHelper.generateInputFromLineList
import eu.albertomorales.ontology.model.GameInput
import eu.albertomorales.ontology.model.taxonomy.{FamilyTree, FamilyTreeBuilder}
import eu.albertomorales.ontology.model.dto._

object GameInputHelper {

  def generateInputFromLineList(lineList: List[String]): GameInput = {
    val taxonomyString: String = lineList(1)
    val numOfQuestionsString: String = lineList(2)
    val numOfQuestions: Int = numOfQuestionsString.trim.toInt
    val questionStrings: List[String] = lineList.slice(3,(numOfQuestions+3))
    val queryStrings: List[String] = lineList.slice((numOfQuestions+4), lineList.size)
    val tree = new FamilyTreeBuilder().load(taxonomyString)
    val questions = Question.parseLines(questionStrings)
    val queries = Query.parseLines(queryStrings)
    new GenericGameInput(tree, questions, queries)
  }

}



object GameInputHelperTest extends App {

  def generateTestData(): List[String] = {
    val lineSet = List(
      "6",
      "Animals ( Reptiles Birds ( Eagles Pigeons Crows ) )",
      "5",
      "Reptiles: Why are many reptiles green?",
      "Birds: How do birds fly?",
      "Eagles: How endangered are eagles?",
      "Pigeons: Where in the world are pigeons most densely populated?",
      "Eagles: Where do most eagles live?",
      "4",
      "Eagles How en",
      "Birds Where",
      "Reptiles Why do",
      "Animals Wh"
    )
    lineSet
  }

  def generateTestDataFromFile(filePath: String): List[String] = {
    import scala.io.Source._
    val lines = fromFile(filePath).getLines()
    lines.toList
  }

  generateInputFromLineList(generateTestData)
  println("ready")
}
