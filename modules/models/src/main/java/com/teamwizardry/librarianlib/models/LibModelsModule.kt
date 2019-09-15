package com.teamwizardry.librarianlib.models

import com.teamwizardry.librarianlib.core.LibrarianLibModule
import net.minecraft.block.Block
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod("librarianlib-models")
class LibModelsModule : LibrarianLibModule("models", logger) {
}

internal val logger = LogManager.getLogger("LibrarianLib/Models")
