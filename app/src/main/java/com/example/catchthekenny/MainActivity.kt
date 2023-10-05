package com.example.catchthekenny

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.catchthekenny.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var score = 0
    var highScore = 0
    var imageArray = ArrayList<ImageView>()
    var runnable = Runnable {}
    var handler = Handler(Looper.getMainLooper())
    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        sharedPref = getSharedPreferences("com.example.storingdata", MODE_PRIVATE) //Yüksek skoru kaydetmek için shared preferences oluştur

        highScore = sharedPref.getInt("score",0) //Kaydettiğim skoru yüksek skor değişkenine atıyorum.
        binding.highText.text = "High Score: ${highScore}" //Yüksek skoru ekrana yazdır


        //ImageArray
        imageArray.add(binding.imageView1)
        imageArray.add(binding.imageView2)
        imageArray.add(binding.imageView3)
        imageArray.add(binding.imageView4)
        imageArray.add(binding.imageView5)
        imageArray.add(binding.imageView6)
        imageArray.add(binding.imageView7)
        imageArray.add(binding.imageView8)
        imageArray.add(binding.imageView9)

        gizle()

        //CountDownTimer
        object : CountDownTimer(15500,1000) {
            override fun onTick(p0: Long) {
                binding.timeText.text = "Time: ${p0/1000}"
            }

            override fun onFinish() {
                binding.timeText.text = "Time: 0" //Süreyi bitir
                handler.removeCallbacks(runnable) //Runnable dursun kenny dursun

                for ( image in imageArray) { //Tüm kennyleri saklıyoruz ki bug olmasın
                    image.visibility = View.INVISIBLE
                }

                //Alert Dialog
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over")
                alert.setMessage("Restart The Game?")

                alert.setPositiveButton("Yes",DialogInterface.OnClickListener{ dialogInterface, i ->
                    //Restart
                    val intentFromMain = intent
                    finish()
                    startActivity(intentFromMain)
                })


                alert.setNegativeButton("No",DialogInterface.OnClickListener{ dialogInterface, i ->
                    Toast.makeText(this@MainActivity,"Game Over!",Toast.LENGTH_LONG).show()
                })

                alert.show()
            }
        }.start()

    }

    fun gizle() {  //View.GONE, View.INVISIBLE vs View.VISIBLE

        runnable = object : Runnable {
            override fun run() {
                for (image in imageArray) {
                    image.visibility = View.INVISIBLE
                }
                val random = Random
                val randomIndex = random.nextInt(9)
                imageArray[randomIndex].visibility = View.VISIBLE

                handler.postDelayed(runnable,300) //yarım saniyede değişiyor
            }
        }
        handler.post(runnable)
    }


    fun skor(view: View) {
        score++
        binding.scoreText.text= "Score: ${score}"
        if(score > highScore) { //Alınan skor kaydettiğim yüksek skordan fazla mı kontrol et fazla ise yüksek skoru güncelle
            sharedPref.edit().putInt("score",score).apply() //Skoru kaydediyorum
        }
    }

}