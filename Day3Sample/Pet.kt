package com.example.applicationday3pet

open class Pet{
    var type : PetTypes? = null
    var age : Int? = null

    public enum class PetTypes{
        CAT, DOG, ANACONDA
    }
    constructor(){
        println("empty constructor is executed")
    }
    constructor(petType : PetTypes){
        this.type = petType
        println("petType constructor is executed")
    }
    constructor(petAge : Int){
        this.age = petAge
        println("petAge constructor is executed")
    }
    constructor(petType : PetTypes, petAge : Int){
        this.type = petType
        this.age = petAge
        println("petType+petAge constructors is executed")
    }
}