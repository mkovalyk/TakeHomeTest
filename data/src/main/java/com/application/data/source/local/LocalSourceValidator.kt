package com.application.data.source.local

interface LocalSourceValidator {
    /**
     * Indicator whether or not data in local storage is valid.
     */
    var isValid: Boolean
}