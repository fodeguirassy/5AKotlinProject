package projetkotlin.a5a.com.flappybird.feat.play

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import android.view.SurfaceView
import projetkotlin.a5a.com.flappybird.MainActivity
import projetkotlin.a5a.com.flappybird.R
import java.util.Timer
import java.util.TimerTask
import java.util.logging.Handler
import kotlin.properties.Delegates


class GameView(private val appContext: Context) : SurfaceView(appContext), SurfaceHolder.Callback {

    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var canvas: Canvas

    private var backgroundBitmap = BitmapFactory.decodeResource(resources, R.drawable.flappy_bg)
    private var scaledBackgroundBitmap : Bitmap

    private var screenWidth: Int
    private var screenHeight: Int
    private var screenRect: Rect
    private var screenDisplayMetrics: DisplayMetrics

    private var singlePipe = BitmapFactory.decodeResource(resources, R.drawable.pipe)
    private var bottomPipeLeft = 500f
    private var bottomPipeTop = -410f
    private var isPipeUp = true


    init {

        holder.addCallback(this)
        screenDisplayMetrics = DisplayMetrics()
        (appContext as MainActivity).windowManager.defaultDisplay.getMetrics(screenDisplayMetrics)

        screenWidth = screenDisplayMetrics.widthPixels
        screenHeight = screenDisplayMetrics.heightPixels
        scaledBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, screenWidth, screenHeight, true)

        screenRect = Rect(0, 0, screenWidth, screenHeight)

    }

    private fun launchTimer() {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            override fun run() {
                var currentCanvas = surfaceHolder.lockCanvas()
                bottomPipeLeft -= 50

                if(isPipeUp) {
                    isPipeUp = false
                    bottomPipeTop += 20
                } else {
                    isPipeUp = true
                    bottomPipeTop = -410f
                }

                invalidate()
                draw(currentCanvas)
                surfaceHolder.unlockCanvasAndPost(currentCanvas)
            }
        }

        timer.schedule(timerTask, 0L, 500)

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
       // invalidate()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        holder?.let {
            surfaceHolder = it
            launchTimer()
        }
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.let {
            it.drawBitmap(scaledBackgroundBitmap, screenRect, screenRect, null)
            it.drawBitmap(singlePipe, bottomPipeLeft, bottomPipeTop,  Paint(Paint.ANTI_ALIAS_FLAG))
           // invalidate()
        }

        //ObjectAnimator.ofFloat(singlePipe, "translationX", bottomPipeLeft)
    }

}