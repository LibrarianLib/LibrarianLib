package com.teamwizardry.librarianlib

import com.teamwizardry.librarianlib.core.util.kotlin.unmodifiableView
import org.apache.logging.log4j.LogManager

object LibrarianLib {
    val logger = LogManager.getLogger("LibrarianLib")

    internal val _modules = mutableMapOf<String, LibrarianLibModule?>()
    val modules: Map<String, LibrarianLibModule?> = _modules.unmodifiableView()
}