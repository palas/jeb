%%%-------------------------------------------------------------------
%%% @author Pablo Lamela Seijas <P.Lamela-Seijas@kent.ac.uk>
%%% @copyright (C) 2014, Pablo Lamela
%%% @doc
%%% Interface for communicating with Java
%%% @end
%%% Created :  3 Oct 2014 by Pablo Lamela
%%%-------------------------------------------------------------------
%%% All rights reserved.
%%%
%%% Redistribution and use in source and binary forms, with or without
%%% modification, are permitted provided that the following conditions
%%% are met:
%%%
%%% 1. Redistributions of source code must retain the above copyright
%%% notice, this list of conditions and the following disclaimer.
%%%
%%% 2. Redistributions in binary form must reproduce the above
%%% copyright notice, this list of conditions and the following
%%% disclaimer in the documentation and/or other materials provided
%%% with the distribution.
%%%
%%% 3. Neither the name of the copyright holder nor the names of its
%%% contributors may be used to endorse or promote products derived
%%% from this software without specific prior written permission.
%%%
%%% THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
%%% "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
%%% LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
%%% FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
%%% COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
%%% INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
%%% BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
%%% LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
%%% CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
%%% LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
%%% ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
%%% POSSIBILITY OF SUCH DAMAGE.
%%%-------------------------------------------------------------------
-module(client).

-export([syn/0, command/1, method_call/4, var/1, null_var/1, close/0,
	 send/1, recv/0, example1/0, example2/0]).

syn() ->
    client:send({msg, syn, syn}),
    case client:recv() of
	{msg, syn, syn} -> ok;
	error -> error
    end.

command(Command) ->
    client:send({msg, command, Command}),
    case recv() of
	{msg, result, Result} -> Result;
	error -> error
    end.

method_call(Class, Method, This, Params) ->
    {cmd, method_call, #{class => Class,
			 method => Method,
			 this => This,
			 params => Params}}.

var(Num) -> {var, var, Num}.

null_var(Class) -> {var, null, Class}.

close() ->
    client:send({msg, close, close}),
    ok.

send(Msg) ->
    Address = list_to_atom("java_server@" ++ net_adm:localhost()),
    {'mbox', Address} ! Msg.

recv() ->
    receive
	{msg, _, _} = Msg -> Msg
    after 100 -> error
    end.

example1() ->
    ok = client:syn(),
    {result, ok_method_call, Var1} =
	client:command(
	  client:method_call("java.lang.System", "console",
			     client:null_var("java.lang.System"), [])),
    client:close(),
    Var1.

example2() ->
    ok = client:syn(),
    {result, ok_method_call, Ten} =
	client:command(
	  client:method_call("eu.prowessproject.jeb.Main", "getTen",
			     client:null_var("eu.prowessproject.jeb.Main"), [])),
    io:format("Ten = ~p~n", [Ten]),
    {result, ok_method_call, One} =
	client:command(
	  client:method_call("eu.prowessproject.jeb.Main", "getOne",
			     client:null_var("eu.prowessproject.jeb.Main"), [])),
    io:format("One = ~p~n", [One]),
    {result, ok_method_call, Eleven} =
	client:command(
	  client:method_call("java.math.BigInteger", "add",
			     Ten, [One])),
    io:format("Eleven = ~p~n", [Eleven]),
    {result, ok_method_call, Null} =
	client:command(
	  client:method_call("eu.prowessproject.jeb.Main", "print",
			     client:null_var("eu.prowessproject.jeb.Main"),
			     [Eleven])),
    io:format("Null = ~p~n", [Null]),
    client:close().
