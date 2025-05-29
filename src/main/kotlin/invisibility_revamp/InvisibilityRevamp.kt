package invisibility-revamp

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ConsumableComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.UseCooldownComponent
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
import net.minecraft.world.World
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.ActionResult
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.minecraft.item.ItemGroups
import org.slf4j.LoggerFactory
import kotlin.random.Random
import com.mojang.datafixers.kinds.Applicative


class InvisShield(settings: Settings) : Item(settings) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): ActionResult {
        if (world.isClient) {
            return ActionResult.PASS
        }
        // user.addStatusEffect(StatusEffects.INVISIBILITY, 100, 0)
        return ActionResult.SUCCESS
    }

}


object InvisibilityRevamp : ModInitializer {
    private val logger = LoggerFactory.getLogger("invisibility-revamp")

    override fun onInitialize() {

    val id_invis_shield= Identifier.of("invisibility-revamp", "invis_shield")
    val key_invis_shield = RegistryKey.of(RegistryKeys.ITEM, id_invis_shield);
    val settings_invis_shield = Item.Settings().registryKey(key_invis_shield)

    val invis_shield = Registry.register(
    Registries.ITEM,
    key_invis_shield,
    InvisShield(settings_invis_shield)
    )
    ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register { group ->
        group.add(invis_shield)
    }

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

    // invis shield
    // val id2= Identifier.of("invisibility-revamp", "invis_shield")
    // val key2 = RegistryKey.of(RegistryKeys.ITEM, id2);
    // val settings2 = Item.Settings()
    //     .registryKey(key2)
    //     .maxCount(1)
    // val INVIS_SHIELD = Registry.register(
    //    Registries.ITEM,
    //    key2,
    //    Item(settings2)
    // //    .use(world:World, user:PlayerEntity, hand:hand)
    // )
    // ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register { group ->
    //     group.add(INVIS_SHIELD)
    // }
    // DefaultItemComponentEvents.MODIFY.register( { ctx ->
    //     ctx.modify(SHARDED_CARROT) { builder ->

    //         builder.add(
    //             DataComponentTypes.USE_COOLDOWN,
    //             UseCooldownComponent(120)
    //             .build()
    //         )

            // builder.add(
            //     DataComponentTypes.,
            //     UseCooldownComponent(120)
            //     .build()
            // )
        }

    


}


