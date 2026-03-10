package net.azisaba.packed.dsl

import net.azisaba.packed.PackedComponent
import net.azisaba.packed.equipment.PackEquipmentModel
import net.azisaba.packed.equipmentModel
import net.azisaba.packed.font
import net.azisaba.packed.font.PackFont
import net.azisaba.packed.itemModel
import net.azisaba.packed.items.PackItemModel
import net.azisaba.packed.lang.PackLanguage
import net.azisaba.packed.language
import net.azisaba.packed.model
import net.azisaba.packed.models.PackModel
import net.azisaba.packed.soundEvent
import net.azisaba.packed.sounds.PackSoundEvent

fun PackedBuilder.equipment(block: ComponentScope<PackEquipmentModel>.() -> Unit) =
    component(PackedComponent.Companion::equipmentModel, block)

fun PackedBuilder.font(block: ComponentScope<PackFont>.() -> Unit) =
    component(PackedComponent.Companion::font, block)

fun PackedBuilder.items(block: ComponentScope<PackItemModel>.() -> Unit) =
    component(PackedComponent.Companion::itemModel, block)

fun PackedBuilder.lang(block: ComponentScope<PackLanguage>.() -> Unit) =
    component(PackedComponent.Companion::language, block)

fun PackedBuilder.models(block: ComponentScope<PackModel>.() -> Unit) =
    component(PackedComponent.Companion::model, block)

fun PackedBuilder.sounds(block: ComponentScope<PackSoundEvent>.() -> Unit) =
    component(PackedComponent.Companion::soundEvent, block)
