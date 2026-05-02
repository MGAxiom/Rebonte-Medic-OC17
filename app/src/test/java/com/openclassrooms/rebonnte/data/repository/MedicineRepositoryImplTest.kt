package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.openclassrooms.rebonnte.domain.model.Medicine
import com.openclassrooms.rebonnte.domain.model.SortType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MedicineRepositoryImplTest {

    private lateinit var repository: MedicineRepositoryImpl
    private val context: Context = mockk(relaxed = true)
    private val firestore: FirebaseFirestore = mockk()
    private val collectionReference: CollectionReference = mockk()

    @Before
    fun setup() {
        every { firestore.collection("medicines") } returns collectionReference
        repository = MedicineRepositoryImpl(context, firestore)
    }

    @Test
    fun addMedicine_success() = runTest {
        val medicine = Medicine(name = "Aspirin", stock = 10)
        val documentReference: DocumentReference = mockk()
        val setTask: Task<Void> = mockk()

        every { collectionReference.document() } returns documentReference
        every { documentReference.id } returns "id123"
        every { documentReference.set(any()) } returns setTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { setTask.await() } returns mockk()

        val result = repository.addMedicine(medicine)
        assertTrue(result.isSuccess)
    }

    @Test
    fun addMedicine_failure() = runTest {
        val medicine = Medicine(name = "Aspirin", stock = 10)
        val documentReference: DocumentReference = mockk()
        val setTask: Task<Void> = mockk()

        every { context.getString(any()) } returns "Error"
        every { collectionReference.document() } returns documentReference
        every { documentReference.set(any()) } returns setTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { setTask.await() } throws Exception("Firestore error")

        val result = repository.addMedicine(medicine)
        assertTrue(result.isFailure)
    }

    @Test
    fun removeMedicine_success() = runTest {
        val medicineName = "Aspirin"
        val query: Query = mockk()
        val getTask: Task<QuerySnapshot> = mockk()
        val querySnapshot: QuerySnapshot = mockk()
        val documentSnapshot: com.google.firebase.firestore.DocumentSnapshot = mockk()
        val documentReference: DocumentReference = mockk()
        val deleteTask: Task<Void> = mockk()

        every { collectionReference.whereEqualTo("name", medicineName) } returns query
        every { query.get() } returns getTask
        every { querySnapshot.documents } returns listOf(documentSnapshot)
        every { documentSnapshot.reference } returns documentReference
        every { documentReference.delete() } returns deleteTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { getTask.await() } returns querySnapshot
        coEvery { deleteTask.await() } returns mockk()

        val result = repository.removeMedicine(medicineName)
        assertTrue(result.isSuccess)
    }

    @Test
    fun removeMedicine_failure() = runTest {
        val medicineName = "Aspirin"
        val query: Query = mockk()
        val getTask: Task<QuerySnapshot> = mockk()

        every { context.getString(any()) } returns "Error"
        every { collectionReference.whereEqualTo("name", medicineName) } returns query
        every { query.get() } returns getTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { getTask.await() } throws Exception("Firestore error")

        val result = repository.removeMedicine(medicineName)
        assertTrue(result.isFailure)
    }

    @Test
    fun updateStock_success() = runTest {
        val medicineName = "Aspirin"
        val mockUser: FirebaseUser = mockk()
        val query: Query = mockk()
        val getTask: Task<QuerySnapshot> = mockk()
        val querySnapshot: QuerySnapshot = mockk()
        val documentSnapshot: com.google.firebase.firestore.DocumentSnapshot = mockk()
        val documentReference: DocumentReference = mockk()
        val updateTask: Task<Void> = mockk()

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().currentUser } returns mockUser
        every { mockUser.displayName } returns "Test User"

        every { collectionReference.whereEqualTo("name", medicineName) } returns query
        every { query.get() } returns getTask
        every { querySnapshot.documents } returns listOf(documentSnapshot)
        every { documentSnapshot.toObject(Medicine::class.java) } returns Medicine(name = "Aspirin", stock = 10)
        every { documentSnapshot.reference } returns documentReference
        
        every { documentReference.update(any<String>(), any(), any<String>(), any()) } returns updateTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { getTask.await() } returns querySnapshot
        coEvery { updateTask.await() } returns mockk()

        val result = repository.updateStock(medicineName, true)
        assertTrue(result.isSuccess)
    }

    @Test
    fun updateStock_decrement_success() = runTest {
        val medicineName = "Aspirin"
        val mockUser: FirebaseUser = mockk()
        val query: Query = mockk()
        val getTask: Task<QuerySnapshot> = mockk()
        val querySnapshot: QuerySnapshot = mockk()
        val documentSnapshot: com.google.firebase.firestore.DocumentSnapshot = mockk()
        val documentReference: DocumentReference = mockk()
        val updateTask: Task<Void> = mockk()

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().currentUser } returns mockUser
        every { mockUser.displayName } returns "Test User"

        every { collectionReference.whereEqualTo("name", medicineName) } returns query
        every { query.get() } returns getTask
        every { querySnapshot.documents } returns listOf(documentSnapshot)
        every { documentSnapshot.toObject(Medicine::class.java) } returns Medicine(name = "Aspirin", stock = 10)
        every { documentSnapshot.reference } returns documentReference
        
        every { documentReference.update(any<String>(), any(), any<String>(), any()) } returns updateTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { getTask.await() } returns querySnapshot
        coEvery { updateTask.await() } returns mockk()

        val result = repository.updateStock(medicineName, false)
        assertTrue(result.isSuccess)
    }

    @Test
    fun updateStock_failure() = runTest {
        val medicineName = "Aspirin"

        every { context.getString(any()) } returns "Error"
        every { collectionReference.whereEqualTo("name", medicineName) } throws Exception("Firestore error")

        val result = repository.updateStock(medicineName, true)
        assertTrue(result.isFailure)
    }

    @Test
    fun getMedicines_callsCollection() = runTest {
        repository.getMedicines(SortType.NONE, "")
        verify { firestore.collection("medicines") }
    }

    @Test
    fun getMedicines_withSearch_callsWhere() = runTest {
        val query: Query = mockk(relaxed = true)
        every { collectionReference.whereGreaterThanOrEqualTo(any<String>(), any()) } returns query
        repository.getMedicines(SortType.NONE, "Aspi")
        verify { collectionReference.whereGreaterThanOrEqualTo("name", "Aspi") }
    }

    @Test
    fun getMedicines_sortByName_callsOrderBy() = runTest {
        val query: Query = mockk(relaxed = true)
        every { collectionReference.orderBy("name") } returns query
        repository.getMedicines(SortType.NAME, "")
        verify { collectionReference.orderBy("name") }
    }

    @Test
    fun getMedicines_sortByStock_callsOrderBy() = runTest {
        val query: Query = mockk(relaxed = true)
        every { collectionReference.orderBy("stock") } returns query
        repository.getMedicines(SortType.STOCK, "")
        verify { collectionReference.orderBy("stock") }
    }
}
