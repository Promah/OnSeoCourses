package com.example.day5recyclernorrisjokes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.jokes_list_item.view.*

class JokeRecycleAdapter private constructor( val context : Context, val dataFromBackEnd : BackEnd) : RecyclerView.Adapter<JokeHolder>() {

    //адаптер зделав синглтоном шоб не создавати нови при переворачивании екрана
    companion object {
        private var instance : JokeRecycleAdapter? = null
        fun  getInstance(contextI: Context, dataFromBackEndI: BackEnd): JokeRecycleAdapter {
            if (instance == null)  // NOT thread safe!
                instance = JokeRecycleAdapter(contextI, dataFromBackEndI)
            return instance!!
        }
    }


    private var countOfDownloadedJokes = 10
    private var  arrJokes = mutableListOf<String>()
    @Volatile var arrSize : Int = 0// це нужно для костиля

    init {
        //перви 10 текстив пусти, шоб не було вихода за интекс при onBindViewHolder
        for (i in 0 until countOfDownloadedJokes)
            arrJokes.add(" ")

        dataFromBackEnd.getLiveJokeText().observeForever {
            it?.let {
                arrJokes.add(it)
                // начало куска кода для написания якого ушло 3 часа танцив з бубнами и Thread(Runnable), для того шоб начальні 10 текстив при загрузци приложения не були пусті
                // бо була просто ОГРОМНА проблема в тому шо BackEnd.updateJokeText onResponse CALLBACK не пускає на виполнение, поки не виполняться всі onBindViewHolder які влазять на екран
                // і ше не меньше в тому шо не давало зделати з иншого потока notifyDataSetChanged()
                arrSize = arrSize + 1
                if (arrSize == countOfDownloadedJokes){
                    for (i in 0 until countOfDownloadedJokes)
                        arrJokes.removeAt(0)
                    notifyDataSetChanged()
                }
                // кінець куска кода для написания якого ушло 3 часа танцив з бубнами и Thread(Runnable), для того шоб начальні 10 текстив при загрузци приложения не були пусті
            }
        }

        //в началі виводим в 2 раза більше чим потом загружаем за запрос, бо якшо не то ловим рандомні throwIndexOutOfBoundsException
        downloadNextStackOfJokes()
        downloadNextStackOfJokes()

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeHolder {
        //тут були Thread.sleep'и шоб подождани калбекив з текстами шуток но це так и не помогло
        return JokeHolder(LayoutInflater.from(context).inflate(R.layout.jokes_list_item, parent, false))
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: JokeHolder, position: Int) {
        //тут були і Thread(Runnable) і Thread.sleep но не помогли, так як не пускали на виполнения "onResponse CALLBACK"
        // а notifyDataSetChanged() ваще ругався "Cannot call this method while RecyclerView is computing a layout or scrolling"

        //проверка если доскролили близь кінця шо грузити следущи
        if (position > arrJokes.size -4)
            downloadNextStackOfJokes()

        if (position>arrJokes.size -1) arrJokes.add("Error: пропав иннет, перезапусти иприложение") // костиль для обработки пропажи интернета
        holder?.textHolder.text = arrJokes[position]

        //а вот цього хватало шоб все було норм, кроми того шо при загрузци вси елементи на екрани пусти
//        dataFromBackEnd.updateJokeText()
//        holder?.textHolder.text = dataFromBackEnd.getLiveJokeText().value
    }

    private fun downloadNextStackOfJokes(){
        Thread(Runnable {
            for (i in 0..countOfDownloadedJokes){
                dataFromBackEnd.updateJokeText()
            }
        }).start()
    }

}
class JokeHolder (view: View) : RecyclerView.ViewHolder(view) {
    val textHolder = view.textViewJoke
}