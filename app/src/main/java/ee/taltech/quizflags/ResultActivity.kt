package ee.taltech.quizflags

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ee.taltech.quizflags.model.Quiz
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        textViewName.text=intent.getStringExtra(Quiz.USER_NAME)
        val total=intent.getIntExtra(Quiz.TOTAL_QUESTIONS,0)
        val correctAnswers=intent.getIntExtra(Quiz.CORRECT_ANSWERS,0)
        textViewResult.text="Your score is $correctAnswers out of $total"

        buttonFinish.setOnClickListener(){
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}