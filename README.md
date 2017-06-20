JEB (Java Erlang Bridge)
========================

JEB is a tool that allows an Erlang node to issue commands to a thread in a Java Virtual Machine. It is composed of a Java library and a simple Erlang API. The library provides a method that will register an Erlang node and loop listening for commands from another Erlang node. When the Erlang node sends a close message, it stops looping.

Currently the API only allows executing methods in objects, `static` methods, constructor calling, the `null` primitive, and `string` constants. It stores the results of method calls in a dictionary and will send the keys back to the Erlang node.

## Example

An illustrative example can be seen in the combination of the class `Main` in Java, and the functions `client:example1/0`, `client:example2/0`, `client:example3/0`, and  `client:example4/0` in the Erlang module.

## Dependencies

The Java library depends on the library JInterface included in the Erlang distribution.

Both the Java library and the Erlang API relay on maps.
