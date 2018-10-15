package eu.albertomorales.ontology.model.taxonomy

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

    def load(pending: List[String], current: TaxonomyStack): FamilyTree = {

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

object Animalitos {

  def main (args: Array[String]): Unit = {
    val builder: FamilyTreeBuilder = FamilyTreeBuilder()

    val cadena1: String = "Animals ( Reptiles Birds ( Eagles Pigeons Crows ) )"
    val cadena2: String = "Animals ( Reptiles Birds ( Eagles Pigeons Crows ) Dogs ( Retriever Mastiff ) )"

    val arbol = builder.load(cadena2)

    println(arbol)
  }

}
