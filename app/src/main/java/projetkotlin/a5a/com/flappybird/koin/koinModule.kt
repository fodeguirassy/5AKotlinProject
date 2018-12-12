package projetkotlin.a5a.com.flappybird.koin

import org.koin.dsl.module.module
import projetkotlin.a5a.com.flappybird.feat.play.PlayFragment
import projetkotlin.a5a.com.flappybird.feat.play.PlayPresenter
import projetkotlin.a5a.com.flappybird.feat.start.StartFragment
import projetkotlin.a5a.com.flappybird.feat.start.StartPresenter
import projetkotlin.a5a.com.flappybird.mvp.AbstractMVPFragment

val koinModule = module {

    factory { PlayPresenter(get() as PlayFragment) }
    factory { PlayFragment() }
    factory { StartPresenter() }
    factory { StartFragment() }
    factory { AbstractMVPFragment() }
}