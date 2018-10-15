package eu.albertomorales.ontology.model.taxonomy

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
