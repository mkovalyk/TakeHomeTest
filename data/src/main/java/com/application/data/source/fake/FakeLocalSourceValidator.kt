package com.application.data.source.fake

import com.application.data.source.local.LocalSourceValidator

/**
 * Just in-memory tracker of the data validity.
 */
class FakeLocalSourceValidator(override var isValid: Boolean) : LocalSourceValidator