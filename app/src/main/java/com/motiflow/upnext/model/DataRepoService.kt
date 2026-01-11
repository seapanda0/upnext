package com.motiflow.upnext.model

import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.motiflow.upnext.AcceptanceStatus
import com.motiflow.upnext.Todo
import com.motiflow.upnext.TodoStatus
import com.motiflow.upnext.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

object COLLECTIONS {
    const val USER = "user"
    const val TODO = "todo"
}
object TODO_DOCUMENT_FIELDS{
    const val assignmentUID = "assignedToUid"
}

object USER_DOCUMENT_FIELDS{
    const val UID = "user"
}

object DataRepoService {
    val todos: Flow<List<Todo>>
        get() =
            AccountService.currentUser.flatMapLatest { user ->
                Firebase.firestore
                    .collection(COLLECTIONS.TODO)
                    .whereEqualTo(TODO_DOCUMENT_FIELDS.assignmentUID, user?.uid)
                    .dataObjects()
            }
    suspend fun addUser(user: User){
        Firebase.firestore
            .collection(COLLECTIONS.USER)
            .document(user.uid)
            .set(user)
            .await()
    }
    suspend fun workerAddTodo(todo: Todo){
        val todoWithMetaData = todo.copy(
            createdByUid = AccountService.currentUserId,
            assignedToUid = AccountService.currentUserId,

            // Dummy section below, change later
            title = "SE Assignment Chart",
            description = "Gantt chart, UML Remember Draw",
            scheduledAt = Timestamp.now(),
            deadlineAt = Timestamp.now(),
            createdByName = "PLACEHOLDER NAME",
            acceptance = AcceptanceStatus.NOT_APPLICABLE,
            status = TodoStatus.NOT_STARTED,
            createdAt = Timestamp.now(),
            updateAt = Timestamp.now()
        )
        Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .add(todoWithMetaData)
            .await()
    }
}