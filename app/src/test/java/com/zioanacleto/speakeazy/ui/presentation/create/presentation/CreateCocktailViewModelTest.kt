package com.zioanacleto.speakeazy.ui.presentation.create.presentation

import com.zioanacleto.buffa.coroutines.DefaultDispatcherProvider
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.events.Resource
import com.zioanacleto.speakeazy.TestDispatcherProvider
import com.zioanacleto.speakeazy.assertAllTrue
import com.zioanacleto.speakeazy.core.domain.create.CreateCocktailRepository
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.detail.model.IngredientsModel
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.Language
import com.zioanacleto.speakeazy.core.domain.user.model.UserModel
import com.zioanacleto.speakeazy.testResourceFlow
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class CreateCocktailViewModelTest {

    private lateinit var repository: CreateCocktailRepository
    private lateinit var userRepository: UserRepository
    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        userRepository = mockk(relaxed = true)
        dispatcherProvider = DefaultDispatcherProvider()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `ingredientsUiState - when Repository returns Success uiState is Loading then Success`() =
        runBlocking {
            // given
            every { repository.getIngredients() } returns flowOf(
                Resource.Success(
                    IngredientsModel(listOf())
                )
            )
            val sut = createSut()

            // when
            val (resultLoading, result) = sut.ingredientsUiState.testResourceFlow()

            // then
            assertAllTrue(
                resultLoading is CreateCocktailIngredientsUiState.Loading,
                result is CreateCocktailIngredientsUiState.Success
            )
        }

    @Test
    fun `ingredientsUiState - when Repository returns Error uiState is Loading then Error`() =
        runBlocking {
            // given
            every { repository.getIngredients() } returns flowOf(
                Resource.Error(Exception("testException"))
            )
            val sut = createSut()

            // when
            val (resultLoading, result) = sut.ingredientsUiState.testResourceFlow()

            // then
            assertAllTrue(
                resultLoading is CreateCocktailIngredientsUiState.Loading,
                result is CreateCocktailIngredientsUiState.Error
            )
        }

    @Test
    fun `createNewWizard - createCocktailUiState is SuccessSingle with currentStep = First`() {
        // given
        val sut = createSut()

        // when
        sut.createNewWizard()
        val result = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            result is CreateCocktailUiState.SuccessSingle,
            (result as CreateCocktailUiState.SuccessSingle).createCocktail.currentStep == 0,
            result.createCocktail.cocktailName?.isEmpty() == true,
            !result.createCocktail.isAlcoholic
        )
    }

    @Test
    fun `selectCreateCocktail - createCocktailUiState correspond to given wizard`() {
        // given
        val givenWizard = CreateCocktailModel(
            id = 2502,
            currentStep = CreateWizardStepData.First.order,
            cocktailName = "test",
            isAlcoholic = true,
            createdTime = Date(),
            lastUpdateTime = Date(),
        )
        val sut = createSut()

        // when
        sut.selectCreateCocktail(givenWizard)
        val result = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            result is CreateCocktailUiState.SuccessSingle,
            (result as CreateCocktailUiState.SuccessSingle).createCocktail.id == givenWizard.id
        )
    }

    @Test
    fun `saveFirstStep - updates uiState with saved cocktail`() = runTest {
        // given
        val name = "cocktailName"
        val isAlcoholic = false
        val sut = createSutForSaveStepTest()

        // when
        sut.saveFirstStep(name, isAlcoholic)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.cocktailName == name,
            currentCocktail.createCocktail.isAlcoholic == isAlcoholic,
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Second.order
        )
    }

    @Test
    fun `saveSecondStep - updates uiState with saved cocktail`() = runTest {
        // given
        val type = "testType"
        val method = "testMethod"
        val glass = "testGlass"
        val sut = createSutForSaveStepTest()

        // when
        sut.saveSecondStep(type, method, glass)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.type == type,
            currentCocktail.createCocktail.method == method,
            currentCocktail.createCocktail.glass == glass,
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Third.order
        )
    }

    @Test
    fun `saveThirdStep - updates uiState with saved cocktail`() = runTest {
        // given
        val ingredients = mapOf("testIngredient" to "1cl")
        val sut = createSutForSaveStepTest()

        // when
        sut.saveThirdStep(ingredients)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.ingredients.isNotEmpty(),
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Fourth.order
        )
    }

    @Test
    fun `saveFourthStep - when selected language is ENGLISH, instructionsIt is empty`() = runTest {
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        // given
        every { repository.getCreateCocktail() } returns flowOf(
            Resource.Error(Exception("testException"))
        )
        every { userRepository.getUser() } returns flowOf(
            Resource.Success(
                UserModel(
                    name = "testName",
                    email = "testEmail",
                    selectedLanguage = Language.ENGLISH
                )
            )
        )
        val instructions = "testInstructions"
        val sut = createSutForSaveStepTest()
        // user flow is still Loading, we need to advance until idle to obtain Success
        advanceUntilIdle()

        // when
        sut.saveFourthStep(instructions)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.instructions == instructions,
            currentCocktail.createCocktail.instructionsIt == "",
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Fifth.order
        )
    }

    @Test
    fun `saveFourthStep - when selected language is ITALIAN, instructions is empty`() = runTest {
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        // given
        every { repository.getCreateCocktail() } returns flowOf(
            Resource.Error(Exception("testException"))
        )
        every { userRepository.getUser() } returns flowOf(
            Resource.Success(
                UserModel(
                    name = "testName",
                    email = "testEmail",
                    selectedLanguage = Language.ITALIAN
                )
            )
        )
        val instructions = "testInstructions"
        val sut = createSutForSaveStepTest()
        // user flow is still Loading, we need to advance until idle to obtain Success
        advanceUntilIdle()

        // when
        sut.saveFourthStep(instructions)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.instructionsIt == instructions,
            currentCocktail.createCocktail.instructions == "",
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Fifth.order
        )
    }

    @Test
    fun `saveFifthStep - updates uiState with saved cocktail`() = runTest {
        // given
        val imageUrl = "testImageUrl"
        val sut = createSutForSaveStepTest()

        // when
        sut.saveFifthStep(imageUrl)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.imageUrl == imageUrl,
            currentCocktail.createCocktail.currentStep == CreateWizardStepData.Uploading.order
        )
    }

    @Test
    fun `saveSuccessStep - updates uiState with saved cocktail`() = runTest {
        // given
        val sut = createSutForSaveStepTest()

        // when
        sut.saveSuccessStep()
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.currentStep == CreateWizardStepData.Success.order
        )
    }

    @Test
    fun `toPreviousStep - updates uiState with given step`() = runTest {
        // given
        val destinationStep = CreateWizardStepData.Second
        val sut = createSutForSaveStepTest()

        // when
        sut.toPreviousStep(destinationStep)
        advanceUntilIdle()
        val currentCocktail = sut.createCocktailUiState.value

        // then
        assertAllTrue(
            currentCocktail is CreateCocktailUiState.SuccessSingle,
            (currentCocktail as CreateCocktailUiState.SuccessSingle).createCocktail.currentStep == destinationStep.order
        )
    }

    @Test
    fun `deleteCreateCocktail - createCocktailDeleted is true`() = runTest {
        // given
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        coEvery { repository.deleteCreateCocktail(any()) } returns true
        val sut = createSut()

        // when
        sut.deleteCreateCocktail(2502)
        advanceUntilIdle()
        val result = sut.createCocktailDeleted.value

        // then
        assert(result)
    }

    @Test
    fun `setCreateCocktailToBeDeleted - createCocktailToBeDeleted corresponds to given wizard`() = runTest {
        // given
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        val givenWizard = CreateCocktailModel(
            id = 2502,
            currentStep = CreateWizardStepData.First.order,
            cocktailName = "test",
            isAlcoholic = true,
            createdTime = Date(),
            lastUpdateTime = Date(),
        )
        val sut = createSut()

        // when
        sut.setCreateCocktailToBeDeleted(givenWizard)
        val result = sut.createCocktailToBeDeleted.value

        // then
        assert(result == givenWizard)
    }

    @Test
    fun `abortDeleteCreateCocktail - createCocktailToBeDeleted is null`() {
        // given
        val sut = createSut()

        // when
        sut.abortDeleteCreateCocktail()
        val result = sut.createCocktailToBeDeleted.value

        // then
        assert(result == null)
    }

    private fun createSut() =
        CreateCocktailViewModel(repository, userRepository, dispatcherProvider)

    private fun TestScope.createSutForSaveStepTest() = run {
        dispatcherProvider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        val givenWizard = CreateCocktailModel(
            id = 2502,
            currentStep = CreateWizardStepData.First.order,
            cocktailName = "test",
            isAlcoholic = true,
            createdTime = Date(),
            lastUpdateTime = Date(),
        )
        val sut = createSut()
        sut.selectCreateCocktail(givenWizard)

        sut
    }
}