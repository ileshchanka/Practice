package info.igorek.practice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.util.Util
import info.igorek.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            buttonStart.setOnClickListener {
                Util.startForegroundService(
                    this@MainActivity,
                    Intent(this@MainActivity, ForegroundService::class.java)
                )
            }
            buttonStop.setOnClickListener {

            }
        }
    }
}