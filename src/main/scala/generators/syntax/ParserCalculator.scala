package generators.syntax

import scala.collection.mutable

object ParserCalculator {
  def getFirst(rule: List[Entry], curFirst: Map[NonTerminal, Set[FirstEntry]]): Set[FirstEntry] = {
    rule match {
      case Nil => Set(Epsilon)

      case Epsilon :: Nil => Set(Epsilon)

      case (term@Terminal(_)) :: _ => Set(term)

      case (nterm@NonTerminal(_)) :: tail =>
        val firstSet = curFirst.getOrElse(nterm, Set())
        if (firstSet.contains(Epsilon)) {
          // TODO: Remove epsilon?
          val tailFirst = getFirst(tail, curFirst)
          firstSet ++ tailFirst
        } else {
          firstSet
        }
    }
  }

  def calculateFirst(rules: List[(NonTerminal, List[Entry])]): Map[NonTerminal, Set[FirstEntry]] = {
    val first = mutable.Map[NonTerminal, Set[FirstEntry]]()

    var changed = true

    while (changed) {
      changed = false
      for {(nterm, rule) <- rules} {
        val prevSet = first.getOrElse(nterm, Set())
        val delta = getFirst(rule, first.toMap)
        if (!delta.subsetOf(prevSet)) {
          changed = true
          first.update(nterm, prevSet ++ delta)
        }
      }
    }
    first.toMap
  }

  def calculateFollow(rules: List[(NonTerminal, List[Entry])], start: NonTerminal,
                      first: Map[NonTerminal, Set[FirstEntry]]): Map[NonTerminal, Set[FollowEntry]] = {
    val answer = mutable.Map[NonTerminal, Set[FollowEntry]]()
    answer.update(start, Set(Dollar))

    var changed = true
    while (changed) {
      changed = false
      for {
        (nterm, rule) <- rules
        (entry, index) <- rule.zipWithIndex
        if entry.isInstanceOf[NonTerminal]
        curNterm = entry.asInstanceOf[NonTerminal]
        curFollow = answer.getOrElse(curNterm, Set())
        ruleReminder = rule.slice(index + 1, rule.size)
        curFirst = getFirst(ruleReminder, first)
      } {
        val prev = answer.getOrElse(curNterm, Set())
        for {firstEntry <- curFirst} {
          firstEntry match {
            case Epsilon =>
            case term@Terminal(name) =>
              val curFollow = answer.getOrElse(curNterm, Set())
              answer.update(curNterm, curFollow + term)
          }
        }
        if (curFirst.contains(Epsilon)) {
          val followForNterm = answer.getOrElse(nterm, Set())
          val followForCurNterm = answer.getOrElse(curNterm, Set())
          answer.update(curNterm, followForCurNterm ++ followForNterm)
        }
        if (answer.getOrElse(curNterm, Set()) != prev) {
          changed = true
        }
      }
    }
    answer.toMap
  }
}
