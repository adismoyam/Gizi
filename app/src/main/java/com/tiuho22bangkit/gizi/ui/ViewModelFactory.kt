package com.tiuho22bangkit.gizi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.Injection
import com.tiuho22bangkit.gizi.ui.profile.ProfileViewModel

class ViewModelFactory private constructor(
//    private val mApplication: Application,
    private val giziRepository: GiziRepository,

    ) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
//            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
//                HomeViewModel(eventRepository) as T
//            }

//            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> {
//                NotificationsViewModel(eventRepository) as T
//            }

//            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
//                DashboardViewModel(eventRepository) as T
//            }

//            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
//                DetailViewModel(eventRepository) as T
//            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(giziRepository) as T
            }

//            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
//                SettingViewModel(settingPreferences) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            val repository = Injection.provideRepository(context)
//            val preferences = SettingPreferences.getInstance(context.dataStore)
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository,
//                    preferences
                ).also { instance = it }
            }
        }
    }
}