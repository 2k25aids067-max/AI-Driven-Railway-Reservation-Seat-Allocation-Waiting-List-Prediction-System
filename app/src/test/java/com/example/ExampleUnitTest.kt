package com.example

import com.example.data.local.InitialData
import com.example.data.model.BerthStatus
import com.example.data.model.PredictionRequest
import com.example.engine.NoShowManager
import com.example.engine.SimulationEngine
import com.example.engine.WaitingListPredictor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testWaitingListPredictionEngine() {
        val train = InitialData.TRAINS.first()
        val request = PredictionRequest(
            pnr = "2847291048",
            trainId = train.trainId,
            origin = "NDLS",
            destination = "HWH",
            travelDate = "2026-10-23",
            travelClass = "3A",
            quotaCode = "GNWL",
            quota = "General",
            currentStatusType = "WL",
            currentPosition = 14,
            daysRemaining = 18
        )

        val result = WaitingListPredictor.predict(request, train)

        assertTrue(result.confirmationProbability in 0.05f..0.99f)
        assertTrue(result.factors.isNotEmpty())
        assertTrue(result.estimatedPositionMovement > 0)
        assertNotNull(result.recommendation)
        assertNotNull(result.estimatedFinalPosition)
        assertNotNull(result.cancellationPolicy)
        assertTrue(result.splitJourneyOptions.isNotEmpty())
    }

    @Test
    fun testQuotaSensitivityDifference() {
        val train = InitialData.TRAINS.first()
        val gnwlRequest = PredictionRequest(
            trainId = train.trainId,
            origin = "NDLS",
            destination = "HWH",
            travelDate = "2026-10-23",
            travelClass = "3A",
            quotaCode = "GNWL",
            quota = "General",
            currentStatusType = "WL",
            currentPosition = 15,
            daysRemaining = 10
        )

        val tqwlRequest = gnwlRequest.copy(quotaCode = "TQWL", quota = "Tatkal")

        val gnwlResult = WaitingListPredictor.predict(gnwlRequest, train)
        val tqwlResult = WaitingListPredictor.predict(tqwlRequest, train)

        // GNWL should have higher confirmation probability than Tatkal WL (TQWL)
        assertTrue(gnwlResult.confirmationProbability > tqwlResult.confirmationProbability)
    }

    @Test
    fun testModelBenchmarksComparison() {
        val benchmarks = WaitingListPredictor.getModelBenchmarks()
        assertEquals(3, benchmarks.size)
        val gradientBoosting = benchmarks.first { it.modelName.contains("Gradient") }
        val logistic = benchmarks.first { it.modelName.contains("Logistic") }
        assertTrue(gradientBoosting.rocAuc > logistic.rocAuc)
        assertTrue(gradientBoosting.accuracy > logistic.accuracy)
    }

    @Test
    fun testSimulationEngineComparison() {
        val train = InitialData.TRAINS.first()
        val comparison = SimulationEngine.runSimulation(
            train = train,
            travelDate = "2026-10-23",
            capacity = 720,
            seed = 42L
        )

        assertTrue(comparison.ai.occupancyRate >= comparison.fcfs.occupancyRate)
        assertTrue(comparison.ai.unusedSeats <= comparison.fcfs.unusedSeats)
        assertTrue(comparison.unusedSeatsReduction > 0f)
    }

    @Test
    fun testNoShowReallocationWorkflow() {
        val initialBerths = NoShowManager.generateInitialCoachBerths("B2")
        val waitingQueue = NoShowManager.getEligibleWaitingPassengers()

        val targetBerth = initialBerths.first { it.status == BerthStatus.CONFIRMED }
        val (reassignedBerth, audit) = NoShowManager.reassignNoShow(
            berth = targetBerth,
            waitingQueue = waitingQueue,
            currentSegment = "CNB"
        )

        assertEquals(BerthStatus.REALLOCATED, reassignedBerth.status)
        assertNotNull(reassignedBerth.reassignedPassengerName)
        assertEquals("Amitabh Sengupta", reassignedBerth.reassignedPassengerName)
        assertNotNull(audit)
        assertEquals(targetBerth.passengerName, audit?.noShowPassenger)
        assertEquals("Amitabh Sengupta", audit?.reallocatedPassenger)
    }

    @Test
    fun testResilienceMitigationsAndGuardrails() {
        val mitigations = com.example.engine.ResilienceEngine.getAllDrawbackMitigations()
        assertEquals(12, mitigations.size)

        val fairnessRules = com.example.engine.ResilienceEngine.getFairnessGuardrailRules()
        assertTrue(fairnessRules.isNotEmpty())
        // Statutory policies must NEVER allow AI override
        assertTrue(fairnessRules.all { !it.aiOverrideAllowed })

        // Test What-If operational scenarios
        val (coachAddedProb, _) = com.example.engine.ResilienceEngine.evaluateWhatIf(
            baseProbability = 0.50f,
            currentWlPosition = 20,
            scenario = com.example.data.model.WhatIfScenarioType.COACH_AUGMENTATION
        )
        assertTrue(coachAddedProb > 0.50f)

        val (festivalProb, _) = com.example.engine.ResilienceEngine.evaluateWhatIf(
            baseProbability = 0.50f,
            currentWlPosition = 20,
            scenario = com.example.data.model.WhatIfScenarioType.FESTIVAL_RUSH_SURGE
        )
        assertTrue(festivalProb < 0.50f)

        // Test Zero-PII privacy masking
        val maskedPnr = com.example.engine.ResilienceEngine.maskPnr("2847291048")
        assertEquals("2847***048", maskedPnr)

        val maskedName = com.example.engine.ResilienceEngine.maskName("Rajesh Sharma")
        assertEquals("R****h S****a", maskedName)
    }
}
