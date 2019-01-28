package eu.albertomorales.ontology.model

import eu.albertomorales.ontology.model.dto.{Query, Question}
import eu.albertomorales.ontology.model.taxonomy.FamilyTree
import eu.albertomorales.ontology.game.{GameInputHelper, GameInputHelperTest}

class Brain {

  def resolve(input: GameInput):  List[String] = {
    val tree: FamilyTree = input.tree
    val questions: List[Question] = input.questions
    val queries:  List[Query] = input.queries

    def resolve(query: Query, _tree: FamilyTree, _questions: List[Question]): String = {
      val queryTopic = query.topic
      val queryText  = query.text.trim()
      val children = _tree.children(queryTopic)
      val resultado = _questions.filter(question => {
          val questionTopic = question.topic.trim()
          val questionText = question.text
          (questionTopic == queryTopic || children.contains(questionTopic.trim)) && questionText.startsWith(queryText)
      }).size
      resultado.toString
    }

    val resultado: List[String] = queries.map(q => {
      resolve(q, tree, questions)
    })

    resultado
  }

}

object Brain {

  def resolve(input: GameInput):  List[String] = {
    new Brain().resolve(input)
  }

}

object BrainFileTest {

  def main(args: Array[String]): Unit = {
    val inputFilePath = args(0)
    println(s"From: ${inputFilePath}")
    val outputFilePath = args(1)
    println(s"To: ${outputFilePath}")

    val t0 = System.nanoTime()
    val gameInput = GameInputHelper
      .generateInputFromLineList(GameInputHelperTest.generateTestDataFromFile(inputFilePath))
    val t1 = System.nanoTime()
    val resultado : List[String] = Brain.resolve(gameInput)
    val t2 = System.nanoTime()

    println("Input time  : " + (t1 - t0) + "ns")
    println("Resolve time: " + (t2 - t1) + "ns")
    println("TOTAL time  : " + (t2 - t0) + "ns")

    import java.io._


    val file = new File(outputFilePath)
    val bw = new BufferedWriter(new FileWriter(file))

    resultado.foreach(x => {
      bw.write(x)
      bw.newLine()
    })

    bw.close()
    println("ya esta")
  }

}

object BrainFirstTest {

  val gameInput = GameInputHelper
      .generateInputFromLineList(GameInputHelperTest.generateTestData)

  val resultado : List[String] = Brain.resolve(gameInput)
  resultado.foreach(x => {
    println(x)
  })
}