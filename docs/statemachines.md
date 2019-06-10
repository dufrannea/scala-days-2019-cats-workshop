# What is a finite state machine

- A finite number of states, initial is mandatory, optionally final state
- Rules to move from one state to another

examples: 
- Light switch: Two states On & Off, two transitions On -> Off, Off -> On

## Answer
A directed graph with a finite number of state (nodes in the graph) and transitions between states (edges in the graph). Transitions are determined by events (which are finite). Possibly initial and terminal (absorbing) states.

Set `S` of states
Set `E` of events
Transition function `(S, E) => S`

Examples:
- Traffic lights
- Coffee machines
- Regular expressions
- TPC/IP
- Akka messages
- UI

Reasoning. Understandability. Comprensibility.

```(scala)
final case class Fsm[S,E](f: (S, E) => S) {
}

vs

type Fsm[S,E] = (S, E) => S
```