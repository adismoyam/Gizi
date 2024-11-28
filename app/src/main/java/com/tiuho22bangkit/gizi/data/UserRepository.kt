package com.tiuho22bangkit.gizi.data

import com.tiuho22bangkit.gizi.data.local.dao.UserDao
import com.tiuho22bangkit.gizi.data.local.entity.UserEntity

class UserRepository(private val userDao: UserDao, private val appExecutors: AppExecutors) {

    suspend fun registerUser(user: UserEntity): Result<String> {
        val existingUser = userDao.getUserByEmail(user.email)
        return if (existingUser == null) {
            userDao.registerUser(user)
            Result.success("Registrasi berhasil")
        } else {
            Result.failure(Exception("Email sudah digunakan"))
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserEntity> {
        val user = userDao.loginUser(email, password)
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Email atau password salah"))
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userDao: UserDao,
            appExecutors: AppExecutors,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao, appExecutors)
            }.also { instance = it }
    }
}
