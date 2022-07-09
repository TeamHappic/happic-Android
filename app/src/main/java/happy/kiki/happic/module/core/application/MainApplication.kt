package happy.kiki.happic.module.core.application

import android.app.Application
import happy.kiki.happic.module.core.util.setUpFlipper

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        setUpFlipper()
    }
}