package com.tiuho22bangkit.gizi.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiuho22bangkit.gizi.data.GiziRepository
import com.tiuho22bangkit.gizi.data.Injection
import com.tiuho22bangkit.gizi.data.UserRepository
import com.tiuho22bangkit.gizi.ui.analysis.AnalysisViewModel
import com.tiuho22bangkit.gizi.ui.profile.ProfileViewModel
import com.tiuho22bangkit.gizi.ui.auth.UserViewModel
import com.tiuho22bangkit.gizi.ui.home.HomeViewModel
import com.tiuho22bangkit.gizi.ui.medrec.MedrecViewModel

class ViewModelFactory private constructor(
//    private val mApplication: Application,
    private val giziRepository: GiziRepository,
    private val userRepository: UserRepository,

    ) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(giziRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(giziRepository) as T
            }

            modelClass.isAssignableFrom(MedrecViewModel::class.java) -> {
                MedrecViewModel(giziRepository) as T
            }

            modelClass.isAssignableFrom(AnalysisViewModel::class.java) -> {
                AnalysisViewModel(giziRepository) as T
            }

            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(userRepository) as T
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
            val repository = Injection.provideGiziRepository(context)
            val repositoryUser = Injection.provideUserRepository(context)
//            val preferences = SettingPreferences.getInstance(context.dataStore)
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(repository, repositoryUser
//                    preferences
                ).also { instance = it }
            }
        }
    }
}