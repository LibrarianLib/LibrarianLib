package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.LibrarianLibModule
import org.apache.logging.log4j.LogManager

object LibModelsModule : LibrarianLibModule("models", logger)
internal val logger = LogManager.getLogger("LibrarianLib: Models")
