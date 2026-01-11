package com.motiflow.upnext.model

import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.motiflow.upnext.Todo
import com.motiflow.upnext.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

object COLLECTIONS {
    const val USER = "user"
    const val TODO = "todo"
}
object USER_FIELDS{
    const val UID = "user"
}

object DataRepoService {

    val notes: Flow<List<Todo>>
        get() =
            AccountService.currentUser.flatMapLatest { user ->
                Firebase.firestore
                    .collection(COLLECTIONS.USER)
                    .whereEqualTo(USER_FIELDS.UID, user?.uid)
                    .dataObjects()
            }
    suspend fun addUser(user: User){
        Firebase.firestore
            .collection(COLLECTIONS.USER)
            .document(user.uid)
            .set(user)
            .await()
    }
}