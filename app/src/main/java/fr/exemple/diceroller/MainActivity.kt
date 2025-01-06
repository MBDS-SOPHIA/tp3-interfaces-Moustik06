package fr.exemple.diceroller

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.animation.BounceInterpolator
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val numberInput: EditText = findViewById(R.id.input)
        val rollButton = findViewById<View>(R.id.button)
        rollButton.visibility = View.GONE
        // Gérer la validation du clavier
        numberInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

                val number = numberInput.text.toString().toIntOrNull()
                if (number != null && number in 2..12) {
                    rollDice()
                } else {
                    clearResults()
                    val resultTextView: TextView = findViewById(R.id.result)
                    resultTextView.text = getString(R.string.entrez_un_nombre_entre_2_et_12)
                }
                true
            } else {
                false
            }
        }
    }

    private fun clearResults() {
        val resultTextView1: TextView = findViewById(R.id.dice1)
        val resultTextView2: TextView = findViewById(R.id.dice2)
        val resultTextView: TextView = findViewById(R.id.result)

        resultTextView1.text = ""
        resultTextView2.text = ""
        resultTextView.text = ""
    }

    private fun rollDice() {
        val dice1 = Dice(6)
        val dice2 = Dice(6)
        val diceRoll1 = dice1.roll()
        val diceRoll2 = dice2.roll()

        val resultTextView1: TextView = findViewById(R.id.dice1)
        val resultTextView2: TextView = findViewById(R.id.dice2)
        resultTextView1.text = diceRoll1.toString()
        resultTextView2.text = diceRoll2.toString()

        val diceSum = diceRoll1 + diceRoll2
        checkWin(diceSum)
    }

    private fun checkWin(diceSum: Int) {
        val resultTextView: TextView = findViewById(R.id.result)
        val numberInput: EditText = findViewById(R.id.input)
        val dice1View: TextView = findViewById(R.id.dice1)
        val dice2View: TextView = findViewById(R.id.dice2)

        val targetNumber = numberInput.text.toString().toIntOrNull() ?: return

        if (diceSum == targetNumber) {
            resultTextView.text = getString(R.string.you_win_la_somme_est, diceSum.toString())
            animateDice(dice1View, dice2View)
        } else {
            resultTextView.text = getString(R.string.you_lose_la_somme_est_r_essayez, diceSum.toString())
        }
    }

    private fun animateDice(dice1View: View, dice2View: View) {
        val bounceAnimator1 = ObjectAnimator.ofFloat(dice1View, "translationY", 0f, -100f, 0f).apply {
            duration = 2000
            interpolator = BounceInterpolator()
        }

        val bounceAnimator2 = ObjectAnimator.ofFloat(dice2View, "translationY", 0f, -100f, 0f).apply {
            duration = 2000
            interpolator = BounceInterpolator()
        }

        AnimatorSet().apply {
            playTogether(bounceAnimator1, bounceAnimator2)
            start()
        }
    }
}

class Dice(private val numSides: Int) {
    fun roll(): Int {
        return (1..numSides).random()
    }
}