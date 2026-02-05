package com.zioanacleto.speakeazy.di

import org.koin.core.qualifier.StringQualifier
import org.koin.core.qualifier.named

inline fun <reified T> getNamedClass(): StringQualifier = named(T::class.java.simpleName)