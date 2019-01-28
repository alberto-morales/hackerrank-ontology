class Query(val topic: String, val text: String) {

}

object Query {

  def parseLine(line: String): Query = {
    val queryLinePattern = "^(\\S+)\\s([\\S||\\s]+)".r
    val queryLinePattern(_topic, _text) = line
    new Query(_topic, _text)
  }

  def parseLines(lines: List[String]) : List[Query] = {
    lines.map(x => parseLine(x))
  }

}

class Question(val topic: String, val text: String) {

}

object Question {

  def parseLine(line: String): Question = {
    val questionLinePattern = "^(\\S+):\\s([\\S||\\s]+)".r
    val questionLinePattern(_topic, _text) = line
    new Question(_topic, _text)
  }

  def parseLines(lines: List[String]) : List[Question] = {
    lines.map(x => parseLine(x))
  }

}

class FamilyTree (private val tree: Map[String, List[String]]) {

  def this(rootElement: String) {
    this(Map[String, List[String]](rootElement -> List[String]()))
  }

  def children(parent: String): List[String] = {
    tree.getOrElse(parent, List[String]())
  }

  def addElement(nuevoElemento: String, taxonomy: List[String]): FamilyTree = {
    // esto es meter al nuevo elemento como hijo de su padre
    val lista: List[(String, List[String])] = tree.toList
    val losQueTengoQueAlterar: List[(String, List[String])] = lista.filter(tupla => {
      val key: String = tupla._1
      taxonomy.contains(key)
    })
    var losRestantes: List[(String, List[String])] = lista.filter(tupla => {
      val key: String = tupla._1
      !taxonomy.contains(key)
    })
    var parteAlterada: List[(String, List[String])] = losQueTengoQueAlterar.map(tupla => {
      val key: String = tupla._1
      val valores: List[String] = tupla._2
      val nuevosValores = valores ++ List[String] (nuevoElemento)
      (key, nuevosValores)
    })
    val listaDelArbolTransformada :  List[(String, List[String])] = parteAlterada ++ losRestantes
    // aki le sumamos el nuevo elemento como presunto padre
    val listaNuevoElementoPresuntoPadre : List[(String, List[String])] = List[(String, List[String])]((nuevoElemento, List[String]()))
    // y aqui va to'padentro del arbol otra vez
    val nuevoArbol = new FamilyTree((listaDelArbolTransformada ++ listaNuevoElementoPresuntoPadre).toMap)

    nuevoArbol
  }

}

class FamilyTreeBuilder {

  def load(wholeString: String): FamilyTree = {
    val tokens: Array[String] = wholeString.split(" ");
    load(tokens.toList)
  }

  def load(listToLoad: List[String]): FamilyTree = {
    val head: String = listToLoad.head
    val tail: List[String] = listToLoad.tail
    load(tail, new TaxonomyStack(head))
  }

  @scala.annotation.tailrec
  private def load(pending: List[String], current: TaxonomyStack): FamilyTree = {

    pending match {
      case Nil => current.getTree()
      case element :: rest =>
        if (element == "(") {
          load(rest, current.down)
        } else if (element == ")") {
          load(rest, current.up)
        } else {
          load(rest, current.add(element))
        }
    }

  }

}

object FamilyTreeBuilder {

  def apply(): FamilyTreeBuilder = {
    new FamilyTreeBuilder()
  }

}

class TaxonomyStack (private val  level: Int,
                     private val taxonomy: List[String],
                     private val tree: FamilyTree) {

  def this(elementoRaiz: String) {
    this(1,
      List[String](elementoRaiz),
      new FamilyTree(elementoRaiz)
    )
  }

  def down: TaxonomyStack = {
    new TaxonomyStack(level + 1, taxonomy, tree)
  }

  def up: TaxonomyStack = {
    new TaxonomyStack(level - 1, taxonomy, tree)
  }

  def add(element: String): TaxonomyStack = {
    val newTaxonomy: List[String] = taxonomy.slice(0,level - 1) ++ List[String] (element)
    val newTree = tree.addElement(element, newTaxonomy)
    new TaxonomyStack(level, newTaxonomy, newTree)
  }

  def getTree(): FamilyTree = {
    tree
  }

}

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

class GenericGameInput(private val _tree: FamilyTree, private val _questions: List[Question], private val _queries: List[Query]) extends GameInput {

  override def tree: FamilyTree = {
    _tree
  }

  override def questions: List[Question] = {
    _questions
  }

  override def queries: List[Query] = {
    _queries
  }

}

object StdinGameInput {

  def parse(): GameInput = {

    @inline def defined(line: String) = {
      line != null && line.nonEmpty
    }

    val lines: List[String] = Iterator.continually(Console.readLine).takeWhile(defined(_)).toList

    val gameInput = GameInputHelper.generateInputFromLineList(lines)

    gameInput
  }

}

trait GameInput {

  def tree: FamilyTree
  def questions: List[Question]
  def queries: List[Query]

}

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

object Solution {

  def main(args: Array[String]) {
    val gameInput = StdinGameInput.parse()
    val resultado : List[String] = Brain.resolve(gameInput)
    resultado.foreach(x => {
      println(x)
    })
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

  GameInputHelper.generateInputFromLineList(generateTestData)
  println("ready")
}

object BrainTest extends App {

  val gameInput = GameInputHelper
    .generateInputFromLineList(GameInputHelperTest.generateTestData)

  val resultado : List[String] = Brain.resolve(gameInput)
  resultado.foreach(x => {
    println(x)
  })
}