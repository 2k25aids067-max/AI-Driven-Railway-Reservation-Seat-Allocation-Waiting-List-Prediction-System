package com.example.engine

import com.example.data.model.DrawbackMitigation
import com.example.data.model.FairnessGuardrailRule
import com.example.data.model.HumanOverrideRecord
import com.example.data.model.WhatIfScenarioType
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object ResilienceEngine {

    fun getAllDrawbackMitigations(): List<DrawbackMitigation> = listOf(
        DrawbackMitigation(
            id = "MIT-01",
            title = "Uncertainty & No-Guarantee Guardrail",
            category = "Uncertainty & ML",
            drawbackDescription = "Predictions cannot be guaranteed due to sudden cancellations, coach changes, and unexpected disruptions.",
            engineeringSolution = "Brier-calibrated confidence intervals (e.g. 78% ± 6%) with prominent 'Not a Guarantee' labeling. Automatic recommendations for backup trains and Tatkal reminders.",
            verificationTag = "Calibrated Bands Enforced"
        ),
        DrawbackMitigation(
            id = "MIT-02",
            title = "Data Latency & Offline Fallback",
            category = "Operations & Data",
            drawbackDescription = "System depends on continuous real-time PRS data; delayed or incomplete data can corrupt predictions.",
            engineeringSolution = "Local SQLite Room database caches historical velocity curves. If live PRS sync drops, system enters Graceful Cache Fallback mode with stale-data confidence margin expansion (±12%).",
            verificationTag = "100% Offline Resilient"
        ),
        DrawbackMitigation(
            id = "MIT-03",
            title = "Legacy CRIS / Mainframe Integration",
            category = "Operations & Data",
            drawbackDescription = "Integrating AI with 40-year-old railway PRS mainframes is high-risk, expensive, and fragile.",
            engineeringSolution = "Non-intrusive Read-Replica Sidecar Architecture. AI sits downstream of the transaction pipeline, reading PRS event streams via Kafka/REST without modifying core ticketing mainframes.",
            verificationTag = "Zero-Risk Sidecar Adapter"
        ),
        DrawbackMitigation(
            id = "MIT-04",
            title = "Tatkal Peak-Load Sub-15ms Latency",
            category = "Operations & Data",
            drawbackDescription = "Millions access the system simultaneously during 10 AM / 11 AM Tatkal rush; server failure is catastrophic.",
            engineeringSolution = "Pre-computed vectorized probability matrices cached in high-speed in-memory layers. Decision latency is bounded to <15ms with zero heavy online model inference during peak lockups.",
            verificationTag = "Sub-15ms Inference"
        ),
        DrawbackMitigation(
            id = "MIT-05",
            title = "Dynamic Operations (Extra Coach & Diversions)",
            category = "Operations & Data",
            drawbackDescription = "Rake configurations, extra coaches, and platforms change unexpectedly, invalidating static historical models.",
            engineeringSolution = "Real-time 'What-If Operational Adapter'. Detects rake augmentation (e.g. Coach B3 added) and triggers instantaneous queue promotion and berth map redistribution.",
            verificationTag = "Dynamic Rake Re-calculator"
        ),
        DrawbackMitigation(
            id = "MIT-06",
            title = "Statutory Fairness & Quota Guardrails",
            category = "Fairness & Security",
            drawbackDescription = "AI could make discriminatory or illegal seat allocations violating senior-citizen or disability laws.",
            engineeringSolution = "Hard deterministic policy rules supersede AI optimization. Senior Citizens (60+) and Divyangjan receive mandatory lower berths; Ladies Quotas stay in dedicated bays with zero AI override allowed.",
            verificationTag = "Statutory Quota Locked"
        ),
        DrawbackMitigation(
            id = "MIT-07",
            title = "Zero-PII Privacy & Data Protection",
            category = "Fairness & Security",
            drawbackDescription = "Handling sensitive passenger travel histories, identity, and payment details risks massive data leaks.",
            engineeringSolution = "Complies with DPDP Act 2023. PNRs and names are cryptographically pseudonymized (e.g. 2847***048, R**** S****) before entering AI inference pipelines. No raw PII stored in model state.",
            verificationTag = "DPDP Act Anonymized"
        ),
        DrawbackMitigation(
            id = "MIT-08",
            title = "Model Drift & Dataset Bias Mitigation",
            category = "Uncertainty & ML",
            drawbackDescription = "Historical bias leads to inaccurate predictions on unusual routes, off-peak trains, or demographic segments.",
            engineeringSolution = "Continuous PSI (Population Stability Index) drift monitor. Trains on route-specific stratified samples with synthetic noise injection to prevent cold-start route neglect.",
            verificationTag = "Drift Monitored"
        ),
        DrawbackMitigation(
            id = "MIT-09",
            title = "Black-Swan & Weather Event Resilience",
            category = "Uncertainty & ML",
            drawbackDescription = "Strikes, severe fog, floods, or sudden cancellations cannot be anticipated by historical AI.",
            engineeringSolution = "External Disruption Event feed. When railway operations trigger Fog/Delay status, the model instantly dampens confirmation assumptions and flags high uncertainty.",
            verificationTag = "Disruption Alert Mode"
        ),
        DrawbackMitigation(
            id = "MIT-10",
            title = "Plain-Language Explainability (XAI)",
            category = "Uncertainty & ML",
            drawbackDescription = "Passengers don't trust complex mathematical scores; asking 'Why is my chance only 40%?' causes frustration.",
            engineeringSolution = "Rule-based natural language generator translates feature weights into 3 plain English explanations (Queue rank, Days left, and Route turnover rate).",
            verificationTag = "Plain-English Explainer"
        ),
        DrawbackMitigation(
            id = "MIT-11",
            title = "Payment Failure Seat-Hold Buffer",
            category = "Operations & Data",
            drawbackDescription = "Payment gateway timeouts cause passengers to lose their allocated seats or waitlist queue spots.",
            engineeringSolution = "5-Minute Cryptographic Seat-Hold Token. Seat is held in reserved transient state during payment processing; automatic retry without queue priority loss.",
            verificationTag = "5-Min Hold Recovery"
        ),
        DrawbackMitigation(
            id = "MIT-12",
            title = "Human-in-the-Loop Override Console",
            category = "Human-in-the-Loop",
            drawbackDescription = "System cannot be completely autonomous; Station Masters & TTEs must manually intervene for emergencies.",
            engineeringSolution = "Emergency Human Override Console. Authorized Station Masters and TTEs can veto and reassign berths with an immutable cryptographic audit log.",
            verificationTag = "Station Master Veto Enabled"
        )
    )

    fun getFairnessGuardrailRules(): List<FairnessGuardrailRule> = listOf(
        FairnessGuardrailRule(
            ruleName = "Senior Citizen Lower Berth Mandate",
            beneficiary = "Male 60+ / Female 45+ traveling solo",
            quotaPolicy = "Mandatory Lower Berth assignment; AI prohibited from allocating Upper/Middle berths.",
            aiOverrideAllowed = false
        ),
        FairnessGuardrailRule(
            ruleName = "Divyangjan (Disability) Priority",
            beneficiary = "Persons with physical disabilities",
            quotaPolicy = "Dedicated 4 berths in SL/3A coaches near doors with wide toilet access.",
            aiOverrideAllowed = false
        ),
        FairnessGuardrailRule(
            ruleName = "Ladies Quota Bay Isolation",
            beneficiary = "Solo female travelers & children (<12)",
            quotaPolicy = "6 berths per sleeper coach clustered exclusively in same bay for personal safety.",
            aiOverrideAllowed = false
        ),
        FairnessGuardrailRule(
            ruleName = "Defense & Emergency VIP Lock",
            beneficiary = "Armed Forces personnel & Medical emergencies",
            quotaPolicy = "Reserved until 4 hours before departure; released strictly to GNWL queue.",
            aiOverrideAllowed = false
        )
    )

    // Evaluate What-If Operational Events
    fun evaluateWhatIf(
        baseProbability: Float,
        currentWlPosition: Int,
        scenario: WhatIfScenarioType
    ): Pair<Float, String> {
        val updatedProb = (baseProbability + scenario.probabilityShift).coerceIn(0.04f, 0.98f)
        val explanation = when (scenario) {
            WhatIfScenarioType.COACH_AUGMENTATION ->
                "Adding coach B3 (+72 seats) creates an immediate promotion wave! Your queue position clears ~${min(currentWlPosition, 45)} spots higher."
            WhatIfScenarioType.FESTIVAL_RUSH_SURGE ->
                "Festival surge drops cancellation rate to 4%. Ticket holders rarely cancel; confirmation drops by 22%."
            WhatIfScenarioType.SPECIAL_TRAIN_ANNOUNCED ->
                "Clone Special Train relieves 400 waitlist passengers. Confirmation probability surges to ${(updatedProb * 100).toInt()}%."
            WhatIfScenarioType.OPERATIONAL_DIVERSION ->
                "Operational diversion reduces rake turnaround. Probability dampened by 10% to protect against denied boarding."
        }
        return Pair(updatedProb, explanation)
    }

    // Natural Language Plain English Explainer
    fun generatePlainEnglishExplanation(
        probability: Float,
        position: Int,
        days: Int,
        quota: String,
        isWeekend: Boolean
    ): List<String> {
        val reasons = mutableListOf<String>()

        if (position <= 10) {
            reasons.add("Your position ($quota-$position) is very close to the top of the queue; typical chart preparation promotes 12-18 seats.")
        } else if (position <= 25) {
            reasons.add("Position $position is in the mid-tier queue. Confirmation requires steady regular cancellations over the next few days.")
        } else {
            reasons.add("Position $position is deep in the waiting list. Standard cancellations alone are usually insufficient without extra coaches.")
        }

        if (days >= 14) {
            reasons.add("You have $days days remaining before departure, giving a large cancellation window for tickets to free up.")
        } else if (days >= 4) {
            reasons.add("With $days days remaining, cancellation activity is beginning to peak as business passengers finalize plans.")
        } else {
            reasons.add("Departure is in only $days days; short lead time significantly limits cancellation turnover.")
        }

        if (quota == "GNWL") {
            reasons.add("You hold a General Waiting List (GNWL) ticket, which receives the highest statutory priority when unutilized quotas release.")
        } else if (quota == "TQWL") {
            reasons.add("Tatkal (TQWL) tickets have high cancellation penalties, so passengers rarely cancel them, causing lower confirmation rates.")
        } else {
            reasons.add("Your quota pool ($quota) is dedicated to intermediate stations with limited reallocated berths.")
        }

        return reasons
    }

    // PII Masking Utility for Data Privacy
    fun maskPnr(pnr: String): String {
        return if (pnr.length >= 6) {
            pnr.take(4) + "***" + pnr.takeLast(3)
        } else pnr
    }

    fun maskName(name: String): String {
        val parts = name.split(" ")
        return parts.joinToString(" ") { part ->
            if (part.length > 2) part.take(1) + "*".repeat(part.length - 2) + part.takeLast(1)
            else part
        }
    }
}
