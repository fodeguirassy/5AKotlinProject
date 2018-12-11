package projetkotlin.a5a.com.flappybird

import android.app.Application
import org.koin.android.ext.android.startKoin
import projetkotlin.a5a.com.flappybird.koin.koinModule

class FlappyBirdApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(koinModule))
    }
}