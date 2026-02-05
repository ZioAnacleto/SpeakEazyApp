package com.zioanacleto.speakeazy.ui.presentation.create.presentation

import com.zioanacleto.buffa.base.BaseViewModel
import com.zioanacleto.buffa.coroutines.DispatcherProvider
import com.zioanacleto.buffa.default
import com.zioanacleto.speakeazy.core.domain.create.CreateCocktailRepository
import com.zioanacleto.speakeazy.core.domain.create.model.CreateCocktailModel
import com.zioanacleto.speakeazy.core.domain.user.FirebaseAuthRepository
import com.zioanacleto.speakeazy.core.domain.user.UserRepository
import com.zioanacleto.speakeazy.core.domain.user.model.Language
import com.zioanacleto.speakeazy.ui.presentation.components.CreateWizardStepData
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.UserUiState
import com.zioanacleto.speakeazy.ui.presentation.user.presentation.mapResourceAsUserUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class CreateCocktailViewModel(
    private val repository: CreateCocktailRepository,
    private val userRepository: UserRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    // main screen ui state
    private val _createCocktailUiState =
        MutableStateFlow<CreateCocktailUiState>(CreateCocktailUiState.Loading)
    val createCocktailUiState: StateFlow<CreateCocktailUiState> =
        _createCocktailUiState.asStateFlow()

    // user info to be used for uploading
    private val _userInfo = MutableStateFlow<UserUiState>(UserUiState.Loading)

    // variable to show confirm deletion popup
    private val _createCocktailToBeDeleted = MutableStateFlow<CreateCocktailModel?>(null)
    val createCocktailToBeDeleted: StateFlow<CreateCocktailModel?> =
        _createCocktailToBeDeleted.asStateFlow()

    // variable to show snackbar to confirm deletion
    private val _createCocktailDeleted = MutableStateFlow(false)
    val createCocktailDeleted: StateFlow<Boolean> = _createCocktailDeleted.asStateFlow()

    // variable to tell user that cocktail is uploaded correctly or not
    private val _createCocktailUploaded = MutableStateFlow(false)
    val createCocktailUploaded: StateFlow<Boolean> = _createCocktailUploaded.asStateFlow()

    // variable to retrieve ingredients for third step
    val ingredientsUiState: StateFlow<CreateCocktailIngredientsUiState> =
        repository.getIngredients()
            .mapResourceAsCreateCocktailIngredientsUiState()
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CreateCocktailIngredientsUiState.Loading
            )

    // useful Pair to get current cocktail and new date
    private val currentUiState: () -> Pair<CreateCocktailModel, Date> = {
        val currentCreateCocktail =
            (_createCocktailUiState.value as CreateCocktailUiState.SuccessSingle).createCocktail
        val currentDate = Date()

        currentCreateCocktail to currentDate
    }

    private fun CreateCocktailModel.saveCocktail() {
        coroutineScope.launch {
            val savedId = withContext(dispatcherProvider.io()) {
                repository.saveCreateCocktail(this@saveCocktail)
            }

            _createCocktailUiState.value =
                CreateCocktailUiState.SuccessSingle(copy(id = savedId))
        }
    }

    init {
        coroutineScope.launch {
            combine(
                repository.getCreateCocktail().mapResourceAsCreateCocktailUiState(),
                userRepository.getUser().mapResourceAsUserUiState()
            ) { createCocktailUiState, userUiState ->
                if (userUiState is UserUiState.Success) {
                    _createCocktailUiState.value = createCocktailUiState
                    _userInfo.value = userUiState
                } else {
                    _createCocktailUiState.value = CreateCocktailUiState.Error(
                        (userUiState as UserUiState.Error).exception
                    )
                }
            }
                .distinctUntilChanged()
                .collect()
        }
    }

    fun createNewWizard() {
        _createCocktailUiState.value = CreateCocktailUiState.SuccessSingle(
            CreateCocktailModel(
                currentStep = CreateWizardStepData.First.order,
                cocktailName = "",
                createdTime = Date(),
                lastUpdateTime = Date(),
                isAlcoholic = false
            )
        )
    }

    fun selectCreateCocktail(createCocktailModel: CreateCocktailModel) {
        _createCocktailUiState.value = CreateCocktailUiState.SuccessSingle(createCocktailModel)
    }

    fun saveFirstStep(cocktailName: String, isAlcoholic: Boolean) {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Second.order,
            cocktailName = cocktailName,
            lastUpdateTime = currentDate,
            isAlcoholic = isAlcoholic
        )

        currentCreateCocktail.saveCocktail()
    }

    fun saveSecondStep(type: String, method: String, glass: String) {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Third.order,
            type = type,
            method = method,
            glass = glass,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun saveThirdStep(ingredients: Map<String, String>) {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Fourth.order,
            ingredients = ingredients,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun saveFourthStep(instructions: String) {
        var (currentCreateCocktail, currentDate) = currentUiState()
        val currentLanguage = (_userInfo.value as UserUiState.Success).user.selectedLanguage

        fun Language.assignInstructions(currentLanguage: Language) =
            if (this == currentLanguage) instructions else ""

        val instructionsEn = currentLanguage.assignInstructions(Language.ENGLISH)
        val instructionsIt = currentLanguage.assignInstructions(Language.ITALIAN)

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Fifth.order,
            instructions = instructionsEn,
            instructionsIt = instructionsIt,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun saveFifthStep(imageUrl: String) {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Uploading.order,
            imageUrl = imageUrl,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun saveSuccessStep() {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = CreateWizardStepData.Success.order,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun toPreviousStep(step: CreateWizardStepData) {
        var (currentCreateCocktail, currentDate) = currentUiState()

        currentCreateCocktail = currentCreateCocktail.copy(
            currentStep = step.order,
            lastUpdateTime = currentDate
        )

        currentCreateCocktail.saveCocktail()
    }

    fun deleteCreateCocktail(uniqueId: Int? = null) {
        val id = uniqueId ?: (_createCocktailUiState.value as CreateCocktailUiState.SuccessSingle)
            .createCocktail
            .id

        coroutineScope.launch(dispatcherProvider.io()) {
            val result = repository.deleteCreateCocktail(id)

            withContext(dispatcherProvider.main()) {
                _createCocktailDeleted.value = result
            }
        }
    }

    fun setCreateCocktailToBeDeleted(createCocktailModel: CreateCocktailModel) {
        _createCocktailToBeDeleted.value = createCocktailModel
    }

    fun abortDeleteCreateCocktail() {
        _createCocktailToBeDeleted.value = null
    }

    fun uploadCurrentCocktail() {
        val currentUserId = firebaseAuthRepository.currentUserId.default()
        val currentUsername = (_userInfo.value as UserUiState.Success).user.name

        var createCocktailModel =
            (_createCocktailUiState.value as CreateCocktailUiState.SuccessSingle).createCocktail
        createCocktailModel = createCocktailModel.copy(
            userId = currentUserId,
            username = currentUsername
        )

        coroutineScope.launch {
            val result = repository.uploadCocktail(createCocktailModel)

            withContext(dispatcherProvider.main()) {
                _createCocktailUploaded.value = result
            }
        }
    }
}