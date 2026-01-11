package com.motiflow.upnext

import com.google.firebase.Timestamp

enum class AccountType {
    WORKER,
    MANAGER
}

data class User(
    var uid: String = "",
    var email: String = "",
    var username: String = "",
    var accountType: AccountType = AccountType.WORKER,
    var createdAt: Timestamp = Timestamp.now()
)
enum class AcceptanceStatus{
    NOT_APPLICABLE,
    NO_RESPONSE,
    ACCEPTED,
    REJECTED
}
enum class TodoStatus{
    NOT_STARTED,
    DROPPED,
    IN_PROGRESS,
    COMPLETED
}

data class Todo(
    val id: String,
    val title: String,
    val description: String,

    val scheduledAt: Timestamp? = null,
    val deadlineAt : Timestamp? = null,

    val createdByUid : String = "",
    val createdByName: String = "",
    val assignedToUid: String = "",

    val acceptance: AcceptanceStatus = AcceptanceStatus.NOT_APPLICABLE,
    val status: TodoStatus = TodoStatus.NOT_STARTED,

    val createdAt: Timestamp = Timestamp.now(),
    val updateAt: Timestamp = Timestamp.now()
)