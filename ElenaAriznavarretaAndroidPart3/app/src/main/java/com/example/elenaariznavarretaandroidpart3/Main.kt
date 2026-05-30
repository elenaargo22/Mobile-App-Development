package com.example.elenaariznavarretaandroidpart3

import kotlin.random.Random

fun main(){
    calculator()
}

fun variablesTask() {
    println("Task 1")
    var a: Int = 5
    var b: Int = 6
    var c = a + b
    println("The result of " + a + " + " + b + " = " + c)
    println("The result of $a + $b = $c")

    c++
    println("The value of incremented c is $c")

    val d: Int = 5
    val e: Int = 2
    val f: Int = d+e
    println("The result of "+d+" + "+e+" = "+f)
    println("The result of $d + $e = $f")

    val g = readLine()
    println(g)

}

// Task 2
//println("a = ")
//val a = readln().toInt()
//println ("b = ")
//val b = readln().toInt()
//var c: Int =1
//var i: Int =1
//println("op = ")
//val op = readln()
//
//if (op == "+"){
//    c=a+b
//} else if (op == "-"){
//    c=a-b
//} else if (op == "*"){
//    c=a*b
//} else if (op == "/"){
//    c=a/b
//} else if (op == "^"){
//    for (i in 1..b){
//        c=c*a
//    }
//} else if (op == "!"){
//    for (i in 1..a){
//        c=c*i
//    }
//}
//println("The result is $c"
fun calculator() {

    while (true) {
        println("\n Calculator ")
        println("Enter first number:")
        val input = readln()
        if (input == "exit") break

        val n1 = input.toDoubleOrNull() ?: 0.0

        println("Enter second number:")
        val n2 = readln().toDoubleOrNull() ?: 0.0

        println("Enter operator (+, -, *, /, ^, !):")
        val op = readln()

        when (op) {
            "+" -> println("Result: ${n1 + n2}")
            "-" -> println("Result: ${n1 - n2}")
            "*" -> println("Result: ${n1 * n2}")
            "/" -> println("Result: ${n1 / n2}")
            "^" -> println("Result: ${powerFor(n1.toInt(), n2.toInt())}")
            "!" -> println("Result: ${factorial(n1.toInt())}")
            else -> println("Invalid operator")
        }
    }
}

// 3.3
fun powerFor(a: Int, b: Int): Int {
    var res = 1
    for (i in 1..b) res *= a
    return res
}

fun powerWhile(a: Int, b: Int): Int {
    var res = 1
    var i = 0
    while (i < b) { res *= a; i++ }
    return res
}

fun powerRepeat(a: Int, b: Int): Int {
    var res = 1
    repeat(b) { res *= a }
    return res
}

fun factorial(n: Int): Int {
    var res = 1
    for (i in 1..n) res *= i
    return res
}

// 3.5
fun fibonacci(n: Int) {
    var a = 1
    var b = 1
    print("Fibonacci $n: ")
    repeat(n) {
        print("$a ")
        val next = a + b
        a = b
        b = next
    }
    println()
}

// 3.6
fun pyramid(n: Int) {
    for (i in n downTo 1) {
        println("*".repeat(i))
    }
}

// 3.7
fun reverseManual(s: String) {
    var reversed = ""
    for (i in s.length - 1 downTo 0) {
        reversed += s[i]
    }
    println("Reversed: $reversed")
}
