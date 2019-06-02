package com.example.applicationgameday2


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import com.example.applicationgameday2.logic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    val ID_TMP_CHECKBOXES_OFFSET = 300

    //try to set separate scaled drawables, but have problem with appeared random padding
//    var scaledCheckBoxDrawable = ScaleDrawable(resources.getDrawable(R.drawable.game_checkbox), Gravity.CENTER, 0.5F, 0.5F)
//    scaledCheckBoxDrawable.level = 1000

//    var bitImageHorizontal = BitmapFactory.decodeResource(resources,R.drawable.line_horizontal)
//    var bitImageScaledHorizontal : Bitmap = Bitmap.createScaledBitmap(bitImageHorizontal,50,50,true)
//    var bitImageVertical = BitmapFactory.decodeResource(resources,R.drawable.line_vertical)
//    var bitImageScaledVertical : Bitmap = Bitmap.createScaledBitmap(bitImageVertical,50,50,true)
//
//    var ImageScaledHorizontalDrawable = BitmapDrawable(resources, bitImageScaledHorizontal)
//    var ImageScaledVerticalDrawable = BitmapDrawable(resources, bitImageScaledVertical)
//    checkBoxTest.buttonDrawable = BitmapDrawable(resources, bitImageScaled)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonBuildGameField.setOnClickListener{
            onClickListenerBuildGameField()
        }

    }

    private fun onClickListenerBuildGameField(){

        val inputNumCols = editTextNumOfCols.text.toString().toIntOrNull()
        inputNumCols?.also {
            val inputNumRows = editTextNumOfRows.text.toString().toIntOrNull()
            inputNumRows?.also {
                if(inputNumCols < 2 || inputNumRows < 2)
                    Toast.makeText(this,"Minimum field is 2x2",Toast.LENGTH_LONG).show()
                else if (inputNumCols > 15 || inputNumRows > 15)
                    Toast.makeText(this,"Maximum field is 15x15",Toast.LENGTH_LONG).show()
                else
                    buildGameField(inputNumRows, inputNumCols )
            }
        }

    }

    private fun buildGameField(numRows : Int, numCols : Int){

        //calculating scales variables
        val gameFieldWith = wrapperGameField.width
        val gameFieldHeight = wrapperGameField.height

        val checkBoxIco = resources.getDrawable(R.drawable.game_checkbox)
        val checkBoxIcoWidth = checkBoxIco.intrinsicWidth.toFloat()
        val checkBoxIcoHeight = checkBoxIco.intrinsicHeight.toFloat()

        var checkBoxIcoScaleW = 1F
        var checkBoxIcoScaleH = 1F
        if (checkBoxIcoWidth*numCols > gameFieldWith){
            checkBoxIcoScaleW = gameFieldWith / checkBoxIcoWidth / numCols
        }
        if (checkBoxIcoHeight*numRows > gameFieldHeight){
            checkBoxIcoScaleH = gameFieldHeight / checkBoxIcoHeight / numRows
        }
        val checkBoxIcoScale :Float = min(checkBoxIcoScaleW,checkBoxIcoScaleH)

        //try to set separate scaled drawables, but have problem with appeared random padding
//        val checkBoxIcoScaledWidth = (checkBoxIcoWidth*checkBoxIcoScale).roundToInt()
//        val checkBoxIcoScaledHeight = (checkBoxIcoHeight*checkBoxIcoScale).roundToInt()


        changeGameFieldSize(numRows,numCols)
        wrapperGameField.removeAllViews()

        val tmpLayoutParamsLinearLayout = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val tmpLayoutParamsCheckBox = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        for (curRow in 0 until numRows){
            val tmpLinearLayout  = LinearLayout(this)
            tmpLinearLayout.orientation = LinearLayout.HORIZONTAL
            tmpLinearLayout.layoutParams = tmpLayoutParamsLinearLayout

            for (curCol in 0 until numCols){
                val tmpCheckBox = CheckBox(this)
                tmpCheckBox.layoutParams = tmpLayoutParamsCheckBox
                tmpCheckBox.id = ID_TMP_CHECKBOXES_OFFSET + (curRow * numCols + curCol)

                //CheckBox styles, but cant scale Drawables with propriety covering places
                // variant 1 (cant "center-crop"):
                tmpCheckBox.buttonDrawable = resources.getDrawable(R.drawable.game_checkbox)

                // variant 2 (also cant "center-crop"):
//                val scaledCheckBoxDrawable = ScaleDrawable(resources.getDrawable(R.drawable.game_checkbox), Gravity.FILL, 1-checkBoxIcoScale , 1-checkBoxIcoScale)
//                scaledCheckBoxDrawable.level = 1000
//                tmpCheckBox.buttonDrawable = scaledCheckBoxDrawable

                //propriety setting size
                tmpCheckBox.width = (checkBoxIcoWidth * checkBoxIcoScale).roundToInt()
                tmpCheckBox.height = (checkBoxIcoHeight * checkBoxIcoScale).roundToInt()

                //padding between checkBoxes
                tmpCheckBox.scaleX = 0.9F
                tmpCheckBox.scaleY = 0.9F


                tmpCheckBox.setOnClickListener {
                    onClickListenerGameFieldCheckbox(curRow * numCols + curCol)
                }
                tmpLinearLayout.addView(tmpCheckBox)
            }

            wrapperGameField.addView(tmpLinearLayout)
        }
        shuffle()
        updateGameField()
    }

    private fun onClickListenerGameFieldCheckbox( numOfElement : Int){
        gameOnCheckBoxClick(numOfElement)
        updateGameField()
        if(haveWon()) Toast.makeText(this,"Have won!",Toast.LENGTH_SHORT).show()
    }

    private fun updateGameField() {
        for (curRow in 0 until numOfRows){
            for (curCol in 0 until numOfCols){
                val tmp = findViewById<CheckBox>(ID_TMP_CHECKBOXES_OFFSET + (curRow * numOfCols + curCol))
                tmp.isChecked = gameFieldArray[curRow][curCol]
                //try to set separate scaled drawables, but have problem with appeared random padding
//                if (tmp.isChecked){
//                    tmp.buttonDrawable = ImageScaledVerticalDrawable
//                }else{
//                    tmp.buttonDrawable =  ImageScaledHorizontalDrawable
//                }

            }
        }
    }
}
