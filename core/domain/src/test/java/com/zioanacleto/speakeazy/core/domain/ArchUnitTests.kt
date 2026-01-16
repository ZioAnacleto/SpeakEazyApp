package com.zioanacleto.speakeazy.core.domain

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
            .importPackages("com.zioanacleto.speakeazy.core.domain")
    }

    @Test
    fun `domain layer must not depend on data`() = ArchitectureRules
        .domainLayer()
        .mustNotDependOnData()
        .check(classes)
}