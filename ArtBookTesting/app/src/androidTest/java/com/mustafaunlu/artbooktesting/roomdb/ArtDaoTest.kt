package com.mustafaunlu.artbooktesting.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.mustafaunlu.artbooktesting.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get : Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase
    private lateinit var dao : ArtDao


    @Before
    fun setup(){
    /*
    //inMemory burada database i geçiçi olarak yani ramda oluşturur. Test için uygun kullanım
    database= Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),ArtDatabase::class.java)
         .allowMainThreadQueries().build()
     dao=database.artDao()
     */
        hiltRule.inject()
        dao=database.artDao()
    }

    @After
    fun teardown(){
     database.close()
    }

    @Test
    fun insertArtTesting() = runBlockingTest {
     val exampleArt=Art("Mona Lisa","Da Vinci",1700,"test.com",1)
     dao.insertArt(exampleArt)

     val list =dao.observeArts().getOrAwaitValue()

     assertThat(list).contains(exampleArt)
    }

    @Test
    fun deleteArtTesting() = runBlockingTest {
     val exampleArtTwo=Art("Lalo","Salamanca",1900,"test.com",2)
     dao.insertArt(exampleArtTwo)
     dao.deleteArt(exampleArtTwo)

     val list=dao.observeArts().getOrAwaitValue()
     assertThat(list).doesNotContain(exampleArtTwo)

    }


    }