package com.example.applicationgameday2.logic

import java.util.LinkedList
import java.util.Random

var numOfRows : Int = 2
var numOfCols : Int = 2
var gameFieldArray  = arrayListOf<LinkedList<Boolean>>()

fun changeGameFieldSize(Rows:Int, Cols:Int ){
    numOfRows = Rows
    numOfCols = Cols
    gameFieldArray  = arrayListOf()
    for (curRow in 0 until Rows){
        val rowOfGameField = LinkedList<Boolean>()
        for (curCol in 0 until Cols){
            rowOfGameField.add(true)// not false
        }
        gameFieldArray.add(rowOfGameField)
    }
}

fun gameOnCheckBoxClick(numOfElement:Int){
    val x = numOfElement % numOfCols
    val y = numOfElement / numOfCols
    for (xx in 0 until numOfCols)
        toggle(y, xx)
    for (yy in 0 until numOfRows)
        toggle(yy, x)
    toggle(y, x)
}

private fun toggle(y:Int, x:Int) {
    gameFieldArray[y][x] = !gameFieldArray[y][x]
}

fun haveWon():Boolean {
//    for (xx in gameFieldArray) {
//        for (yy in xx) {
//            if (!yy) return false
//        }
//    }
//    return true
    return gameFieldArray.all { it.all{it} }
}
fun shuffle() {
    val r = Random()
    for (i in 0.. (2 + r.nextInt(10))) {
        val x = r.nextInt(numOfCols)
        val y = r.nextInt(numOfRows)
        gameOnCheckBoxClick(y * numOfCols + x)
    }
}