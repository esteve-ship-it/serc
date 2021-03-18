package edu.mtu.sercsoundsampler.model

class SERCSoundEntry(val name: String) {
    var countMarkedSamples = 0
    var countPotentialDetectedSamples = 0
    var countConfirmedDetectedSamples = 0
    var countConfirmedIncorrectSamples = 0
}