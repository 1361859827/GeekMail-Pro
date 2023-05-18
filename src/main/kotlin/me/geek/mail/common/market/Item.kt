package me.geek.mail.common.market

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.geek.mail.api.hook.HookPlugin
import me.geek.mail.scheduler.Exclude
import me.geek.mail.settings.SetTings
import me.geek.mail.utils.serializeItemStacks
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * 作者: 老廖
 * 时间: 2022/10/24
 *
 **/
data class Item (
    override val packUid: UUID = UUID.randomUUID(),
    override val user: UUID, // 卖家
    override val time: Long,

    override val points: Int = 0,
    override val money: Double = 0.0,
    @Expose
    override val item: ItemStack
): MarketPlaceholder() {

    override val expire: Long = time+SetTings.ExpiryTime

    val itemString: String = item.serializeItemStacks()



    override fun addToMarket() {
        Market.addMarketItem(this)
        Market.insertItem(this) //上库
    }
    fun condition(player: Player): Boolean {
        return HookPlugin.money.hasMoney(player, money) && HookPlugin.points.hasPoints(player.uniqueId, points)
    }
    fun runCondition(player: Player) {
        HookPlugin.money.takeMoney(player, money)
        HookPlugin.points.takePoints(player.uniqueId, points)
    }
    fun toByteArray(): ByteArray {
        return GsonBuilder()
            .setExclusionStrategies(Exclude())
            .create().toJson(this).toByteArray(charset = Charsets.UTF_8)
    }



}
