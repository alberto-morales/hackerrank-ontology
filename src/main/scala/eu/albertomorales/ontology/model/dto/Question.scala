package eu.albertomorales.ontology.model.dto

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

object PruebaQuestion {

  def main (args: Array[String]): Unit = {

    def probar(_cadena: String) : Unit = {
      val _question: Question = Question.parseLine(_cadena)
      println("'"++_question.topic++"'")
      println("'"++_question.text++"'")
    }
    val cadena1: String = "Reptiles: Why are many reptiles green?"
    probar(cadena1)

    val cadena2: String = "Birds: How do birds fly?"
    probar(cadena2)

    val cadena3: String = "Eagles: How endangered are eagles?"
    probar(cadena3)

    val cadena4: String = "Pigeons: Where in the world are pigeons most densely populated?"
    probar(cadena4)

    val cadena5: String = "Eagles: Where do most eagles live?"
    probar(cadena5)

  }

}