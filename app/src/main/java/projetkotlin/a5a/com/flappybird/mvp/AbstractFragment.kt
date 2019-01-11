package projetkotlin.a5a.com.flappybird.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class AbstractFragment : Fragment() {

    abstract val defaultLayout: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(defaultLayout, container, false)
    }

}