package generators.syntax

object GrammarChecker {
  def checkLL1(grammar: Map[NonTerminal, List[List[Entry]]], first: Map[NonTerminal, Set[FirstEntry]],
               follow: Map[NonTerminal, Set[FollowEntry]]): Boolean = {
    var result = true
    for {
      (nterm, rules) <- grammar
      (firstRule, firstIndex) <- rules.zipWithIndex
      secondRule <- rules.slice(firstIndex + 1, rules.size)
      if secondRule.nonEmpty
    } {
      val firstFirst = ParserCalculator.getFirst(firstRule, first)
      val secondFirst = ParserCalculator.getFirst(secondRule, first)
      if (firstFirst.intersect(secondFirst).nonEmpty) {
        result = false
      }
      if (firstFirst.contains(Epsilon)) {
        val curFollow = follow(nterm)
        val withoutEps = firstFirst.filter(_ != Epsilon).map(_.asInstanceOf[FollowEntry])
        if (curFollow.intersect(withoutEps).nonEmpty) {
          result = false
        }
      }
      if (secondFirst.contains(Epsilon)) {
        val curFollow = follow(nterm)
        val withoutEps = secondFirst.filter(_ != Epsilon).map(_.asInstanceOf[FollowEntry])
        if (curFollow.intersect(withoutEps).nonEmpty) {
          result = false
        }
      }
    }
    result
  }
}
