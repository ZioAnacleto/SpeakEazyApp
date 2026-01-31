package com.zioanacleto.speakeazy.arch_tests

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.zioanacleto.buffa.arch.ArchitectureRules
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before
import org.junit.Test

class ArchUnitTests {

    private lateinit var classes: JavaClasses

    @Before
    fun setUp() {
        classes = ClassFileImporter()
            .importPackages("com.zioanacleto.speakeazy")
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `domain layer must not depend on data`() = ArchitectureRules
        .domainLayer()
        .mustNotDependOnData()
        .check(classes)
}