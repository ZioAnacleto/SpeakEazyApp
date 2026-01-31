package com.zioanacleto.speakeazy.core.data

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.zioanacleto.buffa.arch.ArchitectureRules
import org.junit.Before
import org.junit.Test

class ArchUnitTests {

    private lateinit var classes: JavaClasses

    @Before
    fun setUp() {
        classes = ClassFileImporter()
            .importPackages("com.zioanacleto.speakeazy.core.data")
    }

    @Test
    fun `data layer must not depend on Android`() = ArchitectureRules
        .dataLayer()
        .mustNotDependOnAndroid()
        .check(classes)
}