package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.InitialData
import com.example.data.model.BerthInfo
import com.example.data.model.BerthStatus
import com.example.data.model.ModelBenchmark
import com.example.data.model.PredictionHistoryEntity
import com.example.data.model.PredictionRequest
import com.example.data.model.PredictionResult
import com.example.data.model.ReallocationAuditEntity
import com.example.data.model.SimulationComparison
import com.example.data.model.SimulationEntity
import com.example.data.model.Train
import com.example.data.model.WaitingPassenger
import com.example.data.repository.RailwayRepository
import com.example.engine.NoShowManager
import com.example.engine.SimulationEngine
import com.example.engine.WaitingListPredictor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val label: String, val shortTitle: String) {
    HOME("Overview", "Overview"),
    SEARCH_BOOK("Search & Book", "Book"),
    PREDICTION("WL Predictor", "Predictor"),
    SIMULATION("Simulation", "Simulator"),
    NO_SHOW_DEMO("No-Show Realloc", "Live Demo"),
    RESILIENCE("System Resilience", "Resilience"),
    EVALUATION("Model & AI", "ML Model")
}

class RailReserveViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RailwayRepository(AppDatabase.getInstance(application))

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _navigationStack = mutableListOf(AppScreen.HOME)

    // Demo script modal
    private val _showPitchScript = MutableStateFlow(false)
    val showPitchScript: StateFlow<Boolean> = _showPitchScript.asStateFlow()

    // Trains list from DB
    val trainsList: StateFlow<List<Train>> = repository.trains
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.TRAINS)

    val predictionHistory: StateFlow<List<PredictionHistoryEntity>> = repository.predictionHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditHistory: StateFlow<List<ReallocationAuditEntity>> = repository.auditHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Passenger WL Predictor Form State
    private val _pnrInput = MutableStateFlow("2847291048")
    val pnrInput: StateFlow<String> = _pnrInput.asStateFlow()

    private val _selectedTrainId = MutableStateFlow("TR-12302")
    val selectedTrainId: StateFlow<String> = _selectedTrainId.asStateFlow()

    private val _selectedClass = MutableStateFlow("3A")
    val selectedClass: StateFlow<String> = _selectedClass.asStateFlow()

    private val _selectedQuotaCode = MutableStateFlow("GNWL")
    val selectedQuotaCode: StateFlow<String> = _selectedQuotaCode.asStateFlow()

    private val _selectedQuota = MutableStateFlow("General")
    val selectedQuota: StateFlow<String> = _selectedQuota.asStateFlow()

    private val _statusType = MutableStateFlow("WL") // WL or RAC
    val statusType: StateFlow<String> = _statusType.asStateFlow()

    private val _positionInput = MutableStateFlow(14)
    val positionInput: StateFlow<Int> = _positionInput.asStateFlow()

    private val _daysRemaining = MutableStateFlow(18)
    val daysRemaining: StateFlow<Int> = _daysRemaining.asStateFlow()

    private val _travelDate = MutableStateFlow("2026-10-23")
    val travelDate: StateFlow<String> = _travelDate.asStateFlow()

    private val _selectedModel = MutableStateFlow("Gradient Boosting (Recommended)")
    val selectedModel: StateFlow<String> = _selectedModel.asStateFlow()

    // Prediction Result
    private val _predictionResult = MutableStateFlow<PredictionResult?>(null)
    val predictionResult: StateFlow<PredictionResult?> = _predictionResult.asStateFlow()

    private val _isPredicting = MutableStateFlow(false)
    val isPredicting: StateFlow<Boolean> = _isPredicting.asStateFlow()

    // Admin Simulation State
    private val _simSelectedTrainId = MutableStateFlow("TR-12302")
    val simSelectedTrainId: StateFlow<String> = _simSelectedTrainId.asStateFlow()

    private val _simCapacity = MutableStateFlow(720)
    val simCapacity: StateFlow<Int> = _simCapacity.asStateFlow()

    private val _simComparison = MutableStateFlow<SimulationComparison?>(null)
    val simComparison: StateFlow<SimulationComparison?> = _simComparison.asStateFlow()

    private val _isSimulating = MutableStateFlow(false)
    val isSimulating: StateFlow<Boolean> = _isSimulating.asStateFlow()

    // Journey 3: Live Coach & No-Show Reallocation Demo State
    private val _coachBerths = MutableStateFlow<List<BerthInfo>>(emptyList())
    val coachBerths: StateFlow<List<BerthInfo>> = _coachBerths.asStateFlow()

    private val _waitingQueue = MutableStateFlow<List<WaitingPassenger>>(emptyList())
    val waitingQueue: StateFlow<List<WaitingPassenger>> = _waitingQueue.asStateFlow()

    private val _lastReallocationEvent = MutableStateFlow<String?>(null)
    val lastReallocationEvent: StateFlow<String?> = _lastReallocationEvent.asStateFlow()

    // Search & Book Hub State (From AI-based-Railway-Reservation-System)
    private val _searchFrom = MutableStateFlow("NDLS")
    val searchFrom: StateFlow<String> = _searchFrom.asStateFlow()

    private val _searchTo = MutableStateFlow("HWH")
    val searchTo: StateFlow<String> = _searchTo.asStateFlow()

    private val _bookingNotice = MutableStateFlow<String?>(null)
    val bookingNotice: StateFlow<String?> = _bookingNotice.asStateFlow()

    val modelBenchmarks: List<ModelBenchmark> = WaitingListPredictor.getModelBenchmarks()

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            _coachBerths.value = NoShowManager.generateInitialCoachBerths("B2")
            _waitingQueue.value = NoShowManager.getEligibleWaitingPassengers()
            calculatePrediction()
            executeSimulation()
        }
    }

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _navigationStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_navigationStack.size > 1) {
            _navigationStack.removeAt(_navigationStack.lastIndex)
            _currentScreen.value = _navigationStack.last()
            return true
        }
        return false
    }

    fun togglePitchScript(show: Boolean) {
        _showPitchScript.value = show
    }

    fun setSearchFrom(from: String) { _searchFrom.value = from.uppercase() }
    fun setSearchTo(to: String) { _searchTo.value = to.uppercase() }

    fun setPnrInput(pnr: String) {
        _pnrInput.value = pnr
        InitialData.SAMPLE_PNRS.find { it.first == pnr }?.let { (_, pair) ->
            _selectedTrainId.value = pair.first
            val parts = pair.second.split(" ")
            if (parts.size == 2) {
                _statusType.value = parts[0]
                _positionInput.value = parts[1].toIntOrNull() ?: 10
            }
        }
    }

    fun setSelectedTrainId(trainId: String) {
        _selectedTrainId.value = trainId
    }

    fun setSelectedClass(cls: String) {
        _selectedClass.value = cls
    }

    fun setSelectedQuotaCode(code: String) {
        _selectedQuotaCode.value = code
        _selectedQuota.value = when (code) {
            "TQWL" -> "Tatkal"
            else -> "General"
        }
    }

    fun setStatusType(type: String) {
        _statusType.value = type
        if (type == "RAC") {
            _selectedQuotaCode.value = "RAC"
        } else if (_selectedQuotaCode.value == "RAC") {
            _selectedQuotaCode.value = "GNWL"
        }
    }

    fun setPositionInput(pos: Int) {
        _positionInput.value = pos.coerceAtLeast(1)
    }

    fun setDaysRemaining(days: Int) {
        _daysRemaining.value = days.coerceAtLeast(0)
    }

    fun setSelectedModel(model: String) {
        _selectedModel.value = model
        calculatePrediction()
    }

    fun calculatePrediction() {
        viewModelScope.launch(Dispatchers.Default) {
            _isPredicting.value = true
            delay(300)

            val train = trainsList.value.find { it.trainId == _selectedTrainId.value }
                ?: InitialData.TRAINS.first()

            val request = PredictionRequest(
                pnr = _pnrInput.value.ifBlank { "2847291048" },
                trainId = train.trainId,
                origin = train.sourceCode,
                destination = train.destinationCode,
                travelDate = _travelDate.value,
                travelClass = _selectedClass.value,
                quotaCode = _selectedQuotaCode.value,
                quota = _selectedQuota.value,
                currentStatusType = _statusType.value,
                currentPosition = _positionInput.value,
                daysRemaining = _daysRemaining.value,
                selectedModelType = _selectedModel.value
            )

            val result = WaitingListPredictor.predict(request, train)
            _predictionResult.value = result

            val historyEntity = PredictionHistoryEntity(
                pnr = result.pnr,
                trainNumber = result.trainNumber,
                trainName = result.trainName,
                origin = result.origin,
                destination = result.destination,
                travelDate = result.travelDate,
                travelClass = result.travelClass,
                quota = result.quotaCode,
                initialPosition = result.initialPosition,
                daysRemaining = result.daysRemaining,
                probability = result.confirmationProbability,
                riskLevel = result.riskLevel.label,
                estimatedMovement = result.estimatedPositionMovement,
                recommendation = result.recommendation
            )
            repository.savePrediction(historyEntity)
            _isPredicting.value = false
        }
    }

    // Chart preparation simulation (T-4 Hours)
    fun simulateChartPreparation() {
        viewModelScope.launch(Dispatchers.Default) {
            _daysRemaining.value = 0
            _lastReallocationEvent.value = "Chart-1 Finalized (T-4 Hours): Released 24 unutilized quota berths to GNWL and promoted 18 WL passengers!"
            calculatePrediction()
        }
    }

    fun setSimTrainId(trainId: String) {
        _simSelectedTrainId.value = trainId
        val t = trainsList.value.find { it.trainId == trainId } ?: InitialData.TRAINS.first()
        _simCapacity.value = t.totalSeats
    }

    fun executeSimulation(capacity: Int = _simCapacity.value) {
        viewModelScope.launch(Dispatchers.Default) {
            _isSimulating.value = true
            delay(400)

            val train = trainsList.value.find { it.trainId == _simSelectedTrainId.value }
                ?: InitialData.TRAINS.first()

            val comparison = SimulationEngine.runSimulation(
                train = train,
                travelDate = "2026-10-23",
                capacity = capacity
            )

            _simComparison.value = comparison

            val entity = SimulationEntity(
                trainId = comparison.trainId,
                trainName = comparison.trainName,
                travelDate = comparison.travelDate,
                totalCapacity = comparison.totalCapacity,
                fcfsOccupancy = comparison.fcfs.occupancyRate,
                aiOccupancy = comparison.ai.occupancyRate,
                fcfsWlConversion = comparison.fcfs.wlConversionRate,
                aiWlConversion = comparison.ai.wlConversionRate,
                fcfsUnusedSeats = comparison.fcfs.unusedSeats,
                aiUnusedSeats = comparison.ai.unusedSeats,
                fcfsRevenue = comparison.fcfs.totalRevenue,
                aiRevenue = comparison.ai.totalRevenue
            )
            repository.saveSimulation(entity)
            _isSimulating.value = false
        }
    }

    fun triggerNoShowReallocation(berthNumber: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val currentBerths = _coachBerths.value.toMutableList()
            val targetIdx = currentBerths.indexOfFirst { it.berthNumber == berthNumber }
            if (targetIdx == -1) return@launch

            val targetBerth = currentBerths[targetIdx]
            val currentQueue = _waitingQueue.value

            if (currentQueue.isEmpty()) {
                _lastReallocationEvent.value = "No eligible waitlisted passengers available for this segment."
                return@launch
            }

            targetBerth.status = BerthStatus.NO_SHOW

            val (updatedBerth, audit) = NoShowManager.reassignNoShow(
                berth = targetBerth,
                waitingQueue = currentQueue,
                currentSegment = "CNB"
            )

            currentBerths[targetIdx] = updatedBerth
            _coachBerths.value = currentBerths
            _waitingQueue.value = currentQueue.drop(1)

            if (audit != null) {
                repository.saveAudit(audit)
                _lastReallocationEvent.value =
                    "Berth ${updatedBerth.coach}-${updatedBerth.berthNumber} reallocated to ${audit.reallocatedPassenger} (${audit.originalWaitingStatus}) on CNB->HWH. Recovered 680 Seat-KM!"
            }
        }
    }

    fun bookTrainSeat(trainNumber: String, travelClass: String, status: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val pnr = "2847" + (100000..999999).random()
            _bookingNotice.value = "Successfully booked PNR $pnr on Train $trainNumber ($travelClass)! Status: $status"
            _pnrInput.value = pnr
            _selectedClass.value = travelClass
            calculatePrediction()
            delay(4000)
            _bookingNotice.value = null
        }
    }

    fun resetCoachDemo() {
        _coachBerths.value = NoShowManager.generateInitialCoachBerths("B2")
        _waitingQueue.value = NoShowManager.getEligibleWaitingPassengers()
        _lastReallocationEvent.value = "Demo reset: Fresh coach seating and waitlist queue loaded."
    }
}
