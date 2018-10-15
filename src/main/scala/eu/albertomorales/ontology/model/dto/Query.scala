package eu.albertomorales.ontology.model.dto

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

object PruebaQuery {

  def main (args: Array[String]): Unit = {

    def probar(_cadena: String) : Unit = {
      val _query: Query = Query.parseLine(_cadena)
      println("'"++_query.topic++"'")
      println("'"++_query.text++"'")
    }

    val cadena1: String = "Eagles How en"
    probar(cadena1)

    val cadena2: String = "Birds Where"
    probar(cadena2)

    val cadena3: String = "Reptiles Why do"
    probar(cadena3)

    val cadena4: String = "Animals Wh"
    probar(cadena4)

  }

}