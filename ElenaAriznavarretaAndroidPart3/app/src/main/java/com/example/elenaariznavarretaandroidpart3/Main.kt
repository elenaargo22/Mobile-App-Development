package com.example.elenaariznavarretaandroidpart3

import kotlin.random.Random

fun main() {
    println("Variables")
    variablesTask()

    println("\nFibonacci")
    fibonacci(7)

    println("\nPyramid")
    pyramid(5)

    println("\nReversed")
    reverseManual("hello")

    println("\nArrays")
    arraysTask()

    println("\nClasses")
    classesTask()

    println("\nCalculator (Type 'exit' to quit)")
    calculator()
}

// 1. VARIABLES AND BASIC OPERATIONS

fun variablesTask() {
    var a: Int = 5
    var b: Int = 6
    var c = a + b
    println("The result of " + a + " + " + b + " = " + c)
    println("The result of $a + $b = $c")

    c++
    println("The value of incremented c is $c")

    val d: Int = 5
    val e: Int = 2
    val f: Int = d + e
    println("The result of " + d + " + " + e + " = " + f)
    println("The result of $d + $e = $f")

}

// 2. CONTROL FLOW & CALCULATOR
fun calculator() {
    while (true) {
        println("\n Calculator ")
        println("Enter first number:")
        val input = readln()
        if (input == "exit") {
            break
        }

        val n1 = input.toDoubleOrNull() ?: 0.0

        println("Enter second number:")
        val n2 = readln().toDoubleOrNull() ?: 0.0

        println("Enter operator (+, -, *, /, ^, !):")
        val op = readln()

        if (op == "+") {
            println("Result: " + (n1 + n2))
        } else if (op == "-") {
            println("Result: " + (n1 - n2))
        } else if (op == "*") {
            println("Result: " + (n1 * n2))
        } else if (op == "/") {
            if (n2 == 0.0) {
                println("Error: Division by zero")
            } else {
                println("Result: " + (n1 / n2))
            }
        } else if (op == "^") {
            println("Result: " + powerFor(n1.toInt(), n2.toInt()))
        } else if (op == "!") {
            println("Result: " + factorial(n1.toInt()))
        } else {
            println("Invalid operator")
        }
    }
}

fun powerFor(a: Int, b: Int): Int {
    var res = 1
    for (i in 1..b) {
        res = res * a
    }
    return res
}

fun powerWhile(a: Int, b: Int): Int {
    var res = 1
    var i = 0
    while (i < b) {
        res = res * a
        i++
    }
    return res
}

fun powerRepeat(a: Int, b: Int): Int {
    var res = 1
    repeat(b) {
        res = res * a
    }
    return res
}

fun factorial(n: Int): Int {
    var res = 1
    for (i in 1..n) {
        res = res * i
    }
    return res
}

fun fibonacci(n: Int) {
    var a = 1
    var b = 1
    print("Output: ")
    for (i in 1..n) {
        print("$a ")
        val next = a + b
        a = b
        b = next
    }
    println()
}

fun pyramid(n: Int) {
    var i = n
    while (i >= 1) {
        for (j in 1..i) {
            print("*")
        }
        println()
        i--
    }
}

fun reverseManual(s: String) {
    var reversed = ""
    var i = s.length - 1
    while (i >= 0) {
        reversed = reversed + s[i]
        i--
    }
    println("Output: $reversed")
}

// 3. ARRAYS
fun arraysTask() {
    val names = arrayOf("Anna", "John", "Mark")

    for (i in 0..2) {
        print(names[i] + " ")
    }
    println()

    for (i in 0..2) {
        println("$i: " + names[i])
    }

    var longest = names[0]
    for (i in 0..2) {
        if (names[i].length > longest.length) {
            longest = names[i]
        }
    }
    println("The longest name is: " + longest)

    val arr1 = IntArray(100)
    val arr2 = IntArray(100)
    for (i in 0..99) {
        val randomNumber = Random.nextInt(1, 1000)
        arr1[i] = randomNumber
        arr2[i] = randomNumber
    }

    println("Insertion - Before:")
    for (i in 0..99) { print("${arr1[i]} ") }
    println()

    for (i in 1..99) {
        val key = arr1[i]
        var j = i - 1
        while (j >= 0 && arr1[j] > key) {
            arr1[j + 1] = arr1[j]
            j--
        }
        arr1[j + 1] = key
    }

    println("Insertion - After:")
    for (i in 0..99) { print("${arr1[i]} ") }
    println()


    println("Bubble - Before:")
    for (i in 0..99) { print("${arr2[i]} ") }
    println()

    // Bubble sort algorithm
    for (i in 0..98) {
        for (j in 0..(98 - i)) {
            if (arr2[j] > arr2[j + 1]) {
                val temp = arr2[j]
                arr2[j] = arr2[j + 1]
                arr2[j + 1] = temp
            }
        }
    }

    println("Bubble - After:")
    for (i in 0..99) { print("${arr2[i]} ") }
    println()
}

// 4. CLASSES AND OBJECTS
open class Animal(val name: String, val age: Int, val species: String) {

    open fun move() {
        println("moving")
    }

    // 3. Overrie toString
    override fun toString(): String {
        return "Hi. I'm a " + species + ". I'm " + age + " years old and my name is " + name + "."
    }
}

class Fish(name: String, age: Int) : Animal(name, age, "fish") {
    override fun move() {
        println("swimming")
    }
}

class Dog(name: String, age: Int) : Animal(name, age, "dog") {
    override fun move() {
        println("running")
    }
}

fun classesTask() {
    // Basic test and toString output
    val spider = Animal("Puffy", 6, "spider")
    println(spider.toString())

    // 5. Polymorphism test
    val animal: Animal = Fish("Nemo", 2)
    animal.move()
}