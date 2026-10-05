package com.example.data.local

import com.example.data.model.Train

object InitialData {
    val TRAINS = listOf(
        Train(
            trainId = "TR-12302",
            trainNumber = "12302",
            trainName = "Howrah Rajdhani Express",
            sourceStation = "New Delhi",
            sourceCode = "NDLS",
            destinationStation = "Howrah Jn",
            destinationCode = "HWH",
            intermediateStations = "NDLS, CNB, PRYJ, DDU, HWH",
            trainType = "Rajdhani Express",
            departureTime = "16:55",
            arrivalTime = "09:55 (+1)",
            availableClasses = "1A, 2A, 3A, 3E",
            totalSeats = 720,
            baseFare = 2450.0,
            historicalConfirmationRate = 0.74f,
            avgCancellationRate = 0.18f,
            popularityScore = 0.95f
        ),
        Train(
            trainId = "TR-12004",
            trainNumber = "12004",
            trainName = "Lucknow Shatabdi Express",
            sourceStation = "New Delhi",
            sourceCode = "NDLS",
            destinationStation = "Lucknow Charbagh",
            destinationCode = "LKO",
            intermediateStations = "NDLS, GZB, CNB, LKO",
            trainType = "Shatabdi Express",
            departureTime = "06:10",
            arrivalTime = "12:40",
            availableClasses = "CC, EC",
            totalSeats = 480,
            baseFare = 1165.0,
            historicalConfirmationRate = 0.81f,
            avgCancellationRate = 0.14f,
            popularityScore = 0.91f
        ),
        Train(
            trainId = "TR-12952",
            trainNumber = "12952",
            trainName = "Mumbai Tejas Rajdhani",
            sourceStation = "New Delhi",
            sourceCode = "NDLS",
            destinationStation = "Mumbai Central",
            destinationCode = "MMCT",
            intermediateStations = "NDLS, KOTA, RTM, BRC, BVI, MMCT",
            trainType = "Tejas Rajdhani",
            departureTime = "16:55",
            arrivalTime = "08:35 (+1)",
            availableClasses = "1A, 2A, 3A",
            totalSeats = 680,
            baseFare = 2890.0,
            historicalConfirmationRate = 0.68f,
            avgCancellationRate = 0.16f,
            popularityScore = 0.98f
        ),
        Train(
            trainId = "TR-22436",
            trainNumber = "22436",
            trainName = "Varanasi Vande Bharat Express",
            sourceStation = "New Delhi",
            sourceCode = "NDLS",
            destinationStation = "Varanasi Jn",
            destinationCode = "BSB",
            intermediateStations = "NDLS, CNB, PRYJ, BSB",
            trainType = "Vande Bharat",
            departureTime = "06:00",
            arrivalTime = "14:00",
            availableClasses = "CC, EC",
            totalSeats = 530,
            baseFare = 1750.0,
            historicalConfirmationRate = 0.85f,
            avgCancellationRate = 0.12f,
            popularityScore = 0.97f
        ),
        Train(
            trainId = "TR-12622",
            trainNumber = "12622",
            trainName = "Tamil Nadu Express",
            sourceStation = "New Delhi",
            sourceCode = "NDLS",
            destinationStation = "Chennai Central",
            destinationCode = "MAS",
            intermediateStations = "NDLS, AGC, BPL, NGP, BZA, MAS",
            trainType = "Superfast",
            departureTime = "21:05",
            arrivalTime = "06:15 (+2)",
            availableClasses = "1A, 2A, 3A, SL",
            totalSeats = 840,
            baseFare = 2100.0,
            historicalConfirmationRate = 0.62f,
            avgCancellationRate = 0.22f,
            popularityScore = 0.88f
        )
    )

    val SAMPLE_PNRS = listOf(
        "2847291048" to ("TR-12302" to "WL 14"),
        "4928174029" to ("TR-12004" to "RAC 8"),
        "8391028471" to ("TR-12952" to "WL 45"),
        "6194820193" to ("TR-22436" to "WL 3")
    )
}
