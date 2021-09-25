package ru.ircover.socialmobile.utils

interface UserSessionWorker {
    fun setUserId(userId: Int)
}

class UserSessionWorkerImpl : UserSessionWorker {
    private var userId: Int? = null

    override fun setUserId(userId: Int) {
        this.userId = userId
    }
}