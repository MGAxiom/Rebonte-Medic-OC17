package com.openclassrooms.rebonnte.data.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AisleRepositoryImplTest {

    private lateinit var repository: AisleRepositoryImpl
    private val context: Context = mockk()
    private val firestore: FirebaseFirestore = mockk()
    private val collectionReference: CollectionReference = mockk()

    @Before
    fun setup() {
        every { firestore.collection("aisles") } returns collectionReference
        repository = AisleRepositoryImpl(context, firestore)
    }

    @Test
    fun testAddRandomAisle() = runTest {
        val querySnapshot: QuerySnapshot = mockk()
        val getTask: Task<QuerySnapshot> = mockk()
        val documentReference: DocumentReference = mockk()
        val setTask: Task<Void> = mockk()

        every { collectionReference.get() } returns getTask
        every { querySnapshot.size() } returns 5
        every { collectionReference.document() } returns documentReference
        every { documentReference.id } returns "id"
        every { documentReference.set(any()) } returns setTask
        
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        coEvery { getTask.await() } returns querySnapshot
        coEvery { setTask.await() } returns mockk()

        val result = repository.addRandomAisle()
        assertTrue(result.isSuccess)
    }
}
