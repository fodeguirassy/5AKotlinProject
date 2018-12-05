package projetkotlin.a5a.com.flappybird.koin

import org.koin.dsl.module.module
import projetkotlin.a5a.com.flappybird.feat.play.PlayFragment
import projetkotlin.a5a.com.flappybird.feat.play.PlayPresenter
import projetkotlin.a5a.com.flappybird.feat.start.StartFragment
import projetkotlin.a5a.com.flappybird.feat.start.StartPresenter

val koinModule = module {

    factory { PlayPresenter() }
    factory { PlayFragment() }
    factory { StartPresenter() }
    factory { StartFragment() }

}