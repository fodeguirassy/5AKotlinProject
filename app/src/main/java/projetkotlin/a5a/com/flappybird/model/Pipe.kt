package projetkotlin.a5a.com.flappybird.model

import projetkotlin.a5a.com.flappybird.R
import projetkotlin.a5a.com.flappybird.utils.AppConstants
import java.util.Random


data class Pipe(val type: String,
        val drawable: PipeDrawable) {

    var currentXPosition: Int = 0
    var rightPosition: Int = 0
    var pipeSharedId: Long = 0L

    var height: Float = 0f
        get() = Random().nextFloat() * (AppConstants.PIPE_RANDOM_HEIGHT_MAX -
                AppConstants.PIPE_RANDOM_HEIGHT_MIN) + AppConstants.PIPE_RANDOM_HEIGHT_MIN
}

enum class PipeDrawable(var drawable: Int) {
    TOP(R.drawable.pipe_reversed),
    BOTTOM(R.drawable.pipe);
}

