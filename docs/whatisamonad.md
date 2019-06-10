# What is an effect 

Option => non existence
Try => failure
Future => asynchrony & failure
Either => divergence

typeclass ? type constructor ?

A class that has an inherent semantic/propery

# What is a side effect

- Breaks referential transparency
- "cannot do it twice"
- talking to outside world, IO, or changing global state
- ex: any action, not result, in flatMap (i.e. log) is a side-effect

( cannot replace by its value ? )

## Answers

*Substitution*: replace an expression with the value it evaluates to without changing the meaning of a program (1 + 1 vs println)
*Local reasoning*
*Composition*
Static vs dynamic reasoning in FP vs OO

? You can prove memory allocation of som programs

# What is a Monad

Offer a way of composing functions with an additional behaviour

## Answers

- Inherent notion of sequencing *in a context*, of particular interest when dealing with side effects.
- Examples of Contexts are: _Option_, _List_, _Try_, _Either_, _Future_
- Laziness
- Separate description from action

# Why do we care ?

- Convenient, composable reusable
- Expressive
- Allows to reason locally
- Free behaviour handled at type level non explicitely
