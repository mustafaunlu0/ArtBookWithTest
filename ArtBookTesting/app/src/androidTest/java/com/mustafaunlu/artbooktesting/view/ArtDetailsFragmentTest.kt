package com.mustafaunlu.artbooktesting.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.mustafaunlu.artbooktesting.R
import com.mustafaunlu.artbooktesting.getOrAwaitValue
import com.mustafaunlu.artbooktesting.launchFragmentInHiltContainer
import com.mustafaunlu.artbooktesting.repo.FakeArtRepositoryTest
import com.mustafaunlu.artbooktesting.roomdb.Art
import com.mustafaunlu.artbooktesting.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {


    @get : Rule
    val hiltRule=HiltAndroidRule(this)

    @get : Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory



    @Before
    fun setup(){
        hiltRule.inject()
    }

    @Test
    fun testNavigationFromArtDetailsToImageApi(){

        val navController=Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }
        //Espresso -> UI testlerle alakalıdır
        Espresso.onView(ViewMatchers.withId(R.id.artDetailsImageView)).perform(click())

        Mockito.verify(navController).navigate(ArtDetailsFragmentDirections.toImageApiFragment())
    }

    @Test
    fun testOnBackPressed(){

        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(factory = fragmentFactory){
            Navigation.setViewNavController(requireView(),navController)
        }

        Espresso.pressBack()

        Mockito.verify(navController).popBackStack()

    }

    @Test
    fun testSave(){
        val testViewModel=ArtViewModel(FakeArtRepositoryTest())

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory
        ){
            viewModel=testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.nameText)).perform(replaceText("Mona Lisa"))
        Espresso.onView(ViewMatchers.withId(R.id.artistTextView)).perform(replaceText("Da Vinci"))
        Espresso.onView(ViewMatchers.withId(R.id.yearTextView)).perform(replaceText("1700"))
        Espresso.onView(ViewMatchers.withId(R.id.saveButton)).perform(click())


        assertThat(testViewModel.artList.getOrAwaitValue()).contains(Art("Mona Lisa","Da Vinci",1700,""))
    }
}