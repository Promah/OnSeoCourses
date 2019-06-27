package com.example.day9animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text
import kotlin.math.PI

class MainActivity : AppCompatActivity() {

    var leftOffset : Float = 0f
    lateinit var objectAnimator : ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val animatorFade = ValueAnimator.ofFloat(0f, 1f)
            .also {
                it.duration = 1000
                it.addUpdateListener( object : ValueAnimator.AnimatorUpdateListener{
                    override fun onAnimationUpdate(animation: ValueAnimator?) {
                        textView.alpha = animation?.animatedValue as Float
                    }
                })
            }

        this.tooast("sa")

//        wrong -> leftOffset is 0 in onCreate()
//        leftOffset = (allWrap.width /2 ).toFloat() + textView.width /2

        objectAnimator = ObjectAnimator()
        objectAnimator.target = textView
        objectAnimator.setProperty(TextView.TRANSLATION_X)
        objectAnimator.duration = 1000


        button1.setOnClickListener{
            animatorFade.start()
        }

        button2.setOnClickListener{
            leftOffset = (allWrap.width /2 ).toFloat() + textView.width /2
//            objectAnimator = ObjectAnimator.ofFloat(textView, "translationX",leftOffset, 0f)
            objectAnimator.setFloatValues(leftOffset, 0f)

            objectAnimator.start()
        }

        button3.setOnClickListener {
            leftOffset = (allWrap.width /2 ).toFloat() + textView.width /2
            objectAnimator.setFloatValues(leftOffset, 0f)

            val setOfAnims = AnimatorSet()
//            setOfAnims.play(objectAnimator).before(animatorFade)
            setOfAnims.playTogether(objectAnimator, animatorFade)
            setOfAnims.start()
        }

        button4.setOnClickListener {
            leftOffset = (allWrap.width /2 ).toFloat() + textView.width /2
            textView.translationX = leftOffset
            textView.alpha = 0f
            textView.animate()
                .setDuration(1000)
                .translationX(0f)
                .alpha(1f)
                .setInterpolator(MInterpolator())
//                .setInterpolator(AccelerateDecelerateInterpolator())


        }

    }

//    fun mInterpolator(input : Float): Float{
//        return Math.pow( input.toDouble(), 2.toDouble() ).toFloat()
//    }

//    wrong -> leftOffset is 0 in onStart()
//    override fun onStart() {
//        super.onStart()
//        leftOffset = (allWrap.width /2 ).toFloat() + textView.width /2
//        objectAnimator = ObjectAnimator.ofFloat(textView, "translationX",leftOffset, 0f)
//        objectAnimator.duration = 1000
//    }

    fun Context.toast(str:String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }

    fun MainActivity.tooast(str:String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }

    class ReverseInterpolator : TimeInterpolator {
        override fun getInterpolation(input: Float): Float {
            val rez = Math.cos(input.toDouble() * PI /2).toFloat()
            println("input = $input /t return = $rez")
            return rez
        }
    }

    class MInterpolator : TimeInterpolator {
        override fun getInterpolation(input: Float): Float {
            val rez = Math.cos((input.toDouble()-1) * PI /2).toFloat()
            println("\t input = $input \t\t return = $rez")
            return rez
        }
    }


}

