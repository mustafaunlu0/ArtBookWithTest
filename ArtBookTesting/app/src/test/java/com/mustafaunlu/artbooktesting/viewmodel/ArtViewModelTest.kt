package com.mustafaunlu.artbooktesting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.mustafaunlu.artbooktesting.MainCoroutineRule
import com.mustafaunlu.artbooktesting.repo.FakeArtRepository
import com.mustafaunlu.artbooktesting.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    //Testler main threadte çalışsın istenir bu yüzden birkaç hazır fonksiyon kullandık alttaki buna örnek
    @get:Rule
    var instantTasExecutorRule = InstantTaskExecutorRule()

    //MainCoroutineRule kullanma sebebimiz testlerimiz amülatorda değil şu anda
    //bu fonksiyonla emülatörde yani main threadte çalışıyormuş gibi olacak
    @get: Rule
    var mainCoroutineRule=MainCoroutineRule()


    private lateinit var viewModel: ArtViewModel

    //onCreate gibi düşünebilirsin
    @Before
    fun setup(){
        viewModel= ArtViewModel(FakeArtRepository())

    }


    //LiveData asenkron çalışır. Testlerde bu istenmez
    @Test
    fun `ìnsert art without year returns error`(){
        viewModel.makeArt("Mona Lisa","Da vinci","")

        // getOrAwaitvValueTest -> burada veriyi liveDatadan çıkarır.
        val value=viewModel.insertArtMessage.getOrAwaitValueTest()

        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`(){
        viewModel.makeArt("","Da vinci","2000")

        // getOrAwaitvValueTest -> burada veriyi liveDatadan çıkarır.
        val value=viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }
    @Test
    fun `insert art without artistName returns error`(){
        viewModel.makeArt("Mona Lisa","","2000")

        val value=viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }


}