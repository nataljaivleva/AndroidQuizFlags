package ee.taltech.quizflags.model

import android.content.Context
import android.util.Log


object Quiz {

    const val USER_NAME: String = "user_name"
    const val TOTAL_QUESTIONS: String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions(context: Context): List<Question> {
        val list = mutableListOf<Question>()
        val filename = "flags_quiz.csv"
        val reader = (context.assets.open(filename)
            ?: throw RuntimeException("Cannot open file: $filename"))
            .bufferedReader()
        val lineList = mutableListOf<String>()
        reader.useLines { line ->
            line.map { it.split(";") }.forEach {
                if (it[2].toString().isNotEmpty()) list.add(
                    Question(
                        it[0].toInt(),
                        context.getResources()
                            .getIdentifier(it[1], "drawable", context.getPackageName()),
                        it[2]
                    )
                )
            }
        }
        Log.d("MY_APP", list.toString())

        return list
    }


}