import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isRepeating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music)

        // Tải trạng thái phát lại từ SharedPreferences
        isRepeating = loadRepeatState()
        mediaPlayer.isLooping = isRepeating

        // Cập nhật nút bấm theo trạng thái phát lại
        val btnToggleRepeat: Button = findViewById(R.id.btnToggleRepeat)
        btnToggleRepeat.text = if (isRepeating) "Repeat On" else "Repeat Off"

        // Liên kết chức năng với nút bấm
        btnToggleRepeat.setOnClickListener {
            toggleRepeat()
            btnToggleRepeat.text = if (isRepeating) "Repeat On" else "Repeat Off"
        }
    }

    private fun saveRepeatState(isRepeating: Boolean) {
        val sharedPref = getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("repeatState", isRepeating)
            apply()
        }
    }

    private fun loadRepeatState(): Boolean {
        val sharedPref = getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("repeatState", false)
    }

    private fun toggleRepeat() {
        isRepeating = !isRepeating
        mediaPlayer.isLooping = isRepeating
        saveRepeatState(isRepeating)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}
