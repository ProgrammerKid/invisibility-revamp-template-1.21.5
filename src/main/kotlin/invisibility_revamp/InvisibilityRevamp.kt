package invisibility_revamp

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ConsumableComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.item.PotionItem
import net.minecraft.potion.Potion
import net.minecraft.item.Items
import net.minecraft.item.Item.Settings
import net.minecraft.item.consume.ApplyEffectsConsumeEffect
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.item.ItemGroups
import org.slf4j.LoggerFactory
import kotlin.random.Random



object InvisibilityRevamp : ModInitializer {
    private val logger = LoggerFactory.getLogger("invisibility-revamp")

    override fun onInitialize() {
    logger.info("Registering InvisiblityRevamp...")

    val id= Identifier.of("invisibility-revamp", "sharded_carrot")
    val key = RegistryKey.of(RegistryKeys.ITEM, id);
    val settings = Item.Settings()
        .registryKey(key).
        food(
            FoodComponent.Builder()
            .alwaysEdible()
            .build()
        ).maxCount(16)
    val SHARDED_CARROT = Registry.register(
       Registries.ITEM,
       key,
       Item(settings)
    )
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register { group ->
        group.add(SHARDED_CARROT)
    }

    DefaultItemComponentEvents.MODIFY.register( { ctx ->
        ctx.modify(SHARDED_CARROT) { builder ->

            builder.add(
                DataComponentTypes.CONSUMABLE,
                ConsumableComponent.builder()
                    .consumeEffect(
                        ApplyEffectsConsumeEffect(
                            listOf(
                                StatusEffectInstance(StatusEffects.INVISIBILITY, 20 * 60),
                                StatusEffectInstance(StatusEffects.SLOWNESS, 20 * 10),
                                StatusEffectInstance(StatusEffects.DARKNESS, 20 * 10),

                            )
                        )
                    )
                    .build()
            )
            builder.add(DataComponentTypes.FOOD, 
                FoodComponent.Builder()
                    .nutrition(2)
                    .saturationModifier(0.6f)
                    .alwaysEdible()
                    .build()
            )
        }

    })

    // val POTION_OF_BLINDNESS =  Registry.register(
    //     Registries.POTION,
    //     Identifier.of("invisibility-revamp", "potion_of_blindness"), 
    //     Potion(
    //         "Blindness",
    //         StatusEffectInstance(StatusEffects.BLINDNESS, 800),
    //     ),
    // )

}
}
    
