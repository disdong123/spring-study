package kr.disdong.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> Clogger(): Logger = LoggerFactory.getLogger(T::class.java)
