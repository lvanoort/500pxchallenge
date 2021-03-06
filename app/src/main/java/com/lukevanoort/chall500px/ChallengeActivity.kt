package com.lukevanoort.chall500px

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lukevanoort.chall500px.databinding.ActivityChallengeBinding
import com.lukevanoort.chall500px.gallery.GalleryViewModel
import com.lukevanoort.chall500px.navigation.BackHandler
import com.lukevanoort.chall500px.navigation.NavigationViewModel
import javax.inject.Inject


class ChallengeActivity : AppCompatActivity(), ActivityComponentProvider {
    private lateinit var component: ActivityComponent

    private lateinit var binding: ActivityChallengeBinding

    @Inject
    lateinit var vm: NavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent()
            .plusActivity(ActivityModule(this))
            .also { it.inject(this) }
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nvRootNavigation.attachViewModel(vm)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun provideActivityComponent(): ActivityComponent {
        return component
    }

    override fun onBackPressed() {
        binding.nvRootNavigation.handleBack()
    }
}


