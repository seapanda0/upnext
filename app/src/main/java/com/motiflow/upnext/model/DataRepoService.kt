package com.motiflow.upnext.model

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.motiflow.upnext.AcceptanceStatus
import com.motiflow.upnext.AccountType
import com.motiflow.upnext.Todo
import com.motiflow.upnext.TodoStatus
import com.motiflow.upnext.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

object COLLECTIONS {
    const val USER = "user"
    const val TODO = "todo"
}
object TODO_DOCUMENT_FIELDS{
    const val assignmentUID = "assignedToUid"
}

object USER_DOCUMENT_FIELDS{
    const val UID = "uid"
    const val ACCOUNT_TYPE = "accountType"
}
object DataRepoService {
    var currentUserType: AccountType? = null
    var currentUserName: String = ""

    suspend fun accountInitialCheck () {
        val firebaseUser = Firebase.auth.currentUser ?: run{
            currentUserType = null
            return
        }
        try{
            val snapshot = Firebase.firestore
                .collection(COLLECTIONS.USER)
                .document(firebaseUser.uid)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
            currentUserType = user?.accountType
            currentUserName = user?.username!!
        }catch (e: Exception){
            e.printStackTrace()
            currentUserType = null
        }
    }

    suspend fun todosForWorker(workerId: String): Flow<List<Todo>>{
        return Firebase.firestore
            .collection(COLLECTIONS.TODO)
            .whereEqualTo(TODO_DOCUMENT_FIELDS.assignmentUID, workerId)
            .dataObjects()
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val todos: Flow<List<Todo>>
        get() =
            AccountService.currentUser.flatMapLatest { user ->
                Firebase.firestore
                    .collection(COLLECTIONS.TODO)
                    .whereEqualTo(TODO_DOCUMENT_FIELDS.assignmentUID, user?.uid)
                    .dataObjects()
            }
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentUser: Flow<User?>
        get() =
            AccountService.currentUser.flatMapLatest { firebaseuser ->
                if (firebaseuser == null){
                    flowOf(null)
                }else{
                    Firebase.firestore
                        .collection(COLLECTIONS.USER)
                        .document(firebaseuser.uid)
                        .snapshots()
                        .map{it.toObject(User::class.java)}
                }
            }
    val allWorkers: Flow<List<User>>
        get() =
            Firebase.firestore
                .collection(COLLECTIONS.USER)
                .whereEqualTo(USER_DOCUMENT_FIELDS.ACCOUNT_TYPE, AccountType.WORKER.name)
                .dataObjects()
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
            createdByName = currentUserName,
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

    suspend fun managerCreateTodo(todo: Todo){
        val todoWithMetaData = todo.copy(
            createdByUid = AccountService.currentUserId,

            // Undecided yet
            createdByName = currentUserName,
            acceptance = AcceptanceStatus.NO_RESPONSE,
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