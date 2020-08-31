package ee.taltech.quizflags

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import ee.taltech.quizflags.model.Question
import ee.taltech.quizflags.model.Quiz
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var listQuestions: List<Question>? = null
    private var currentPosition = 1
    private var selectedPosition = 0
    private lateinit var selectedAnswerView: TextView
    private var correctAnswers: Int = 0
    private var username: String? = null

    companion object {
        private const val COUNT_QUESTION = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        Toast.makeText(this, "What country does this flag belong to?", Toast.LENGTH_LONG).show()

        username = intent.getStringExtra(Quiz.USER_NAME)

        listQuestions = Quiz.getQuestions(this).shuffled()
        setQuestion()
        progressBar.max = COUNT_QUESTION
        textViewProgress.text = "$currentPosition/${progressBar.max}"

        textViewAnswer1.setOnClickListener(this)
        textViewAnswer2.setOnClickListener(this)
        textViewAnswer3.setOnClickListener(this)
        textViewAnswer4.setOnClickListener(this)
        buttonAnswer.setOnClickListener(this)
    }

    fun setQuestion() {

        val currentQuestion: Question = listQuestions!!.get(currentPosition - 1)

        progressBar.progress = currentPosition
        textViewProgress.text = "$currentPosition/${progressBar.max}"

        imageFlag.setImageResource(currentQuestion.image)
        buttonAnswer.text = "ANSWER"

        defaultAnswerView()
        val answersSet: HashSet<String> = hashSetOf()
        answersSet.add(currentQuestion.correctAnswer)
        while (answersSet.size != 4) {
            val number = (0 until listQuestions!!.size - 1).random()
            answersSet.add(listQuestions!![number].correctAnswer)
        }
        val answers = answersSet.shuffled().toTypedArray()
        textViewAnswer1.text = answers[0]
        textViewAnswer2.text = answers[1]
        textViewAnswer3.text = answers[2]
        textViewAnswer4.text = answers[3]
    }

    fun defaultAnswerView() {
        val options = ArrayList<TextView>()
        options.add(0, textViewAnswer1)
        options.add(1, textViewAnswer2)
        options.add(2, textViewAnswer3)
        options.add(3, textViewAnswer4)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_answer_bg_color)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.textViewAnswer1 -> {
                selectAnswerView(textViewAnswer1, 1)
            }
            R.id.textViewAnswer2 -> {
                selectAnswerView(textViewAnswer2, 2)
            }
            R.id.textViewAnswer3 -> {
                selectAnswerView(textViewAnswer3, 3)
            }
            R.id.textViewAnswer4 -> {
                selectAnswerView(textViewAnswer4, 4)
            }
            R.id.buttonAnswer -> {
                if (selectedPosition == 0) {
                    currentPosition++
                    when {
                        currentPosition <= COUNT_QUESTION -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Quiz.USER_NAME, username)
                            intent.putExtra(Quiz.CORRECT_ANSWERS, correctAnswers)
                            intent.putExtra(Quiz.TOTAL_QUESTIONS, COUNT_QUESTION)
                            startActivity(intent)
                            finish()
                        }
                    }
                } else {
                    when {
                        buttonAnswer.text.equals("ANSWER") -> {
                            val question = listQuestions?.get(currentPosition - 1)
                            if (selectedAnswerView.text.toString()
                                    .equals(question?.correctAnswer)
                            ) {
                                selectedAnswerView.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.correct_answer_bg_color
                                    )
                                correctAnswers++
                            } else {
                                selectedAnswerView.background =
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.wrong_answer_bg_color
                                    )
                                Toast.makeText(
                                    this,
                                    "Correct answer: " + question?.correctAnswer,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                    if (currentPosition == COUNT_QUESTION) {
                        buttonAnswer.text = "FINISH"
                    } else {
                        buttonAnswer.text = "NEXT QUESTION"
                    }
                    selectedPosition = 0
                }
            }
        }
    }


    private fun selectAnswerView(tv: TextView, selected: Int) {
        defaultAnswerView()
        selectedPosition = selected
        selectedAnswerView = tv
        tv.setTextColor(Color.parseColor("#363A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_answer_bg_color)
    }
}