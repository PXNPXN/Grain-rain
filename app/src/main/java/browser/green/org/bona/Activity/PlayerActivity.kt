package browser.green.org.bona.Activity

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.renderscript.Sampler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import browser.green.org.bona.BuildConfig
import browser.green.org.bona.R
import com.bumptech.glide.Glide
import fm.qingting.player.controller.PlaybackState
import fm.qingting.player.exception.PlaybackException
import fm.qingting.player.utils.parsePlaybackTime
import fm.qingting.qtsdk.QTException
import fm.qingting.qtsdk.player.QTPlayerManager
import fm.qingting.qtsdk.player.QTPlayer
import fm.qingting.qtsdk.player.listener.QTPlaybackListener
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*



/**
 * Created by lee on 2018/1/23.
 */

class PlayerActivity : AppCompatActivity() {
    private var player: QTPlayer? = null
    private val playbackListener: QTPlaybackListener = object : QTPlaybackListener() {
        override fun onPrepareUrlFail() {
        }

        override fun onPlaybackStateChanged(playbackState: PlaybackState) {
            tv_state.text = "当前状态:${playbackState.value}"
            // 播放完成，自动播放下一集
            if (playbackState == PlaybackState.ENDED) {
                next()
            }
        }

        override fun onPlaybackProgressChanged(currentPositionMS: Long, bufferedPositionMS: Long, durationMS: Long) {
            playback_durations.text = parsePlaybackTime(durationMS)
            playback_progress_bar.max = durationMS.toInt()
            playback_current_progress.text = parsePlaybackTime(currentPositionMS)
            if (!isSeeking) {
                playback_progress_bar.progress = currentPositionMS.toInt()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
        }
    }
    private var isSeeking = false
    private var channelId: Int? = null
    private var programIds: ArrayList<Int>? = null
    private var image: String?=null
    private var curIndex = 0



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        channelId = intent.getSerializableExtra("channelId") as? Int
        image=intent.getSerializableExtra("image") as? String
        programIds = @Suppress("UNCHECKED_CAST")(intent.getSerializableExtra("programIds") as? ArrayList<Int>)
        curIndex = intent.getIntExtra("currentProgramIdIndex", 0)
        Glide.with(this).load(image).into(iv_play_image)
        val rotateAnimator=ObjectAnimator.ofFloat(iv_play_image,"rotation",0f,360f)
        var lin=LinearInterpolator()
        rotateAnimator.setDuration(8000)
        rotateAnimator.repeatCount=-1
        rotateAnimator.setInterpolator(lin)
        rotateAnimator.start()





        QTPlayerManager.obtainPlayer(object : QTPlayerManager.Connect2PlayerCallback {
            override fun onConnected(player: QTPlayer) {
                this@PlayerActivity.player = player.also {
                    it.addPlaybackListener(playbackListener)
                    if (BuildConfig.DEBUG) {
                        it.startDebug()
                    }
                }
                prepare2Play()
            }

            override fun onDisconnected() {
                player = null
            }

            override fun onFair(e: QTException) {
                e.printStackTrace()
                player = null
            }
        })

        playback_progress_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                isSeeking = false
                player?.seekTo(seekBar.progress)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // 这里的列表自动播放都是通过programIds和播放回调状态为ended来控制的，会导致列表播放失效
        player?.removePlaybackListener(playbackListener)
        player = null
//        // 停止播放器服务，同时会释放播放器资源
//        // 使用场景不需要播放器进行工作
//        QTPlayerManager.release()

    }


    fun onClickPlayControl(v: View) {

        when (v) {

            btn_play ->{
                play()
                View.VISIBLE
                btn_play.visibility=View.GONE
                btn_pause.visibility=View.VISIBLE
            }
            btn_pause -> {
                pause()
                btn_play.visibility=View.VISIBLE
                btn_pause.visibility=View.GONE
            }
            btn_pre -> {
                previous()
                btn_pause.visibility=View.VISIBLE
                btn_play.visibility=View.GONE
            }
            btn_next -> {
                next()
                btn_pause.visibility=View.VISIBLE
                btn_play.visibility=View.GONE
            }

        }
    }

    private fun prepare2Play() =
            channelId?.let {
                if (programIds != null) {
                    programIds?.getOrNull(curIndex)
                            ?.apply {
                                tv_program_index.text = "当前播放:节目${curIndex + 1}"
                                player?.prepare(it, this)
                            }
                } else {
                    player?.prepare(it)
                }
            }

    private fun play() {
        when (player?.playbackState) {
            PlaybackState.PLAYING, PlaybackState.PAUSE, PlaybackState.ENDED -> player?.play()
            else -> prepare2Play()
        }
    }

    private fun pause() {
        player?.pause()
    }

    private fun next() {
        programIds?.takeIf {
            !it.isEmpty() && curIndex < it.size - 1
        }?.let {
            ++curIndex
            prepare2Play()
        }
    }

    private fun previous() {
        programIds?.takeIf {
            !it.isEmpty() && curIndex > 0
        }?.let {
            --curIndex
            prepare2Play()
        }
    }

    companion object {
        //播放专辑节目需要专辑id跟节目ID，如果播放的是广播，只需要channelId programIs为空就好
        @JvmOverloads
        fun start(context: Context, channelId: Int, programIds: ArrayList<Int>?,image: String, currentProgramIdIndex: Int? = 0) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("channelId", channelId)
            intent.putExtra("image",image)
            if (programIds != null && programIds.size != 0) {
                intent.putExtra("programIds", programIds)
                intent.putExtra("currentProgramIdIndex", currentProgramIdIndex)
            }
            context.startActivity(intent)
        }
    }
}


