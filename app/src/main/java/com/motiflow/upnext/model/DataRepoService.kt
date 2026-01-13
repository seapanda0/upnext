package com.motiflow.upnext.model

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
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
    suspend fun workerCreateTodo(todo: Todo){
        val todoWithMetaData = todo.copy(
            createdByUid = AccountService.currentUserId,
            assignedToUid = AccountService.currentUserId,

            // Undecided yet
            createdByName = "PLACEHOLDER NAME",
            status = TodoStatus.NOT_STARTED,
            createdAt = Timestamp.now(),
            updateAt = Timestamp.now()
        )
        Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .add(todoWithMetaData)
            .await()
    }

    suspend fun readTodo(todoId: String): Todo? {
        return Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .document(todoId).get().await().toObject()
    }
    suspend fun updateTodo(todo: Todo) {
        val updatedTodo = todo.copy(
            updateAt = Timestamp.now()
        )
        Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .document(updatedTodo.id!!).set(updatedTodo).await()
    }
    suspend fun deleteTodo(todoId: String) {
        Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .document(todoId).delete().await()
    }

}