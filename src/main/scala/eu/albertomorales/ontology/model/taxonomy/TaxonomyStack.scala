package eu.albertomorales.ontology.model.taxonomy

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