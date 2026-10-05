package com.example.data.model

data class DrawbackMitigation(
    val id: String,
    val title: String,
    val category: String, // "Uncertainty & ML", "Operations & Data", "Fairness & Security", "Human-in-the-Loop"
    val drawbackDescription: String,
    val engineeringSolution: String,
    val verificationTag: String,
    val isVerified: Boolean = true
)

enum class WhatIfScenarioType(
    val title: String,
    val iconName: String,
    val description: String,
    val probabilityShift: Float,
    val capacityShift: Int
) {
    COACH_AUGMENTATION(
        title = "Extra 3A Coach Added (+72 Berths)",
        iconName = "AddBox",
        description = "Railway administration attaches an extra coach (B3) due to persistent waitlist backlog.",
        probabilityShift = +0.28f,
        capacityShift = +72
    ),
    FESTIVAL_RUSH_SURGE(
        title = "Diwali / Chhath Holiday Surge",
        iconName = "TrendingUp",
        description = "Demand spikes by 350%; historical cancellation rate drops from 18% to 4%.",
        probabilityShift = -0.22f,
        capacityShift = 0
    ),
    SPECIAL_TRAIN_ANNOUNCED(
        title = "Suvidha Special Train Introduced",
        iconName = "Train",
        description = "Clone train runs 40 mins behind main train, absorbing 400 waitlist passengers.",
        probabilityShift = +0.35f,
        capacityShift = +120
    ),
    OPERATIONAL_DIVERSION(
        title = "Fog / Weather Speed Restriction",
        iconName = "Warning",
        description = "Turnaround delays cause scheduled train to run with revised rake composition.",
        probabilityShift = -0.10f,
        capacityShift = -24
    )
}

data class FairnessGuardrailRule(
    val ruleName: String,
    val beneficiary: String,
    val quotaPolicy: String,
    val aiOverrideAllowed: Boolean, // ALWAYS FALSE: AI cannot override statutory quotas
    val complianceStatus: String = "100% Enforced"
)

data class HumanOverrideRecord(
    val id: Long = System.currentTimeMillis(),
    val authorityRole: String, // "Station Master", "Chief Commercial Manager", "TTE"
    val reason: String, // "Medical Emergency", "VIP Movement", "Disabled Passenger Accommodation"
    val berthNumber: String,
    val actionTaken: String,
    val timestamp: Long = System.currentTimeMillis()
)
