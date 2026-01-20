package dev.vyklade.events;

import com.hypixel.hytale.builtin.deployables.component.DeployableComponent;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import dev.vyklade.VykladeSpellbooks;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

public class VykladeDamageEvent extends EntityEventSystem<EntityStore, Damage> {
    public VykladeDamageEvent() {
        super(Damage.class);
    }

    @Override
    public void handle(int index, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl Damage damage) {
        Ref<EntityStore> entityRef = archetypeChunk.getReferenceTo(index);
        Player playerComponent = store.getComponent(entityRef, Player.getComponentType());

        if (damage.getSource() instanceof Damage.EntitySource sourceEntity) {
            Ref<EntityStore> sourceRef = sourceEntity.getRef();
            DeployableComponent deployableComponent = store.getComponent(sourceRef, DeployableComponent.getComponentType());

            if (deployableComponent != null) {
                if (!deployableComponent.getConfig().getId().equals("SpellbookSignatureTrap")) {
                    return;
                }

                if (playerComponent != null) {
                    damage.setAmount(0);
                    damage.setCancelled(true);
                    VykladeSpellbooks.LOGGER.atInfo().log("Cancelled Deployable Player Damage Event!");
                } else {
                    EffectControllerComponent effectControllerComponent = store.getComponent(entityRef, EffectControllerComponent.getComponentType());
                    assert effectControllerComponent != null;

                    EntityEffect SlowEffect = EntityEffect.getAssetMap().getAsset("Spellbook_Slow");

                    assert SlowEffect != null;

                    effectControllerComponent.addEffect(entityRef, SlowEffect, commandBuffer);
                }
            }
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Archetype.empty();
    }

    @NullableDecl
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }
}
