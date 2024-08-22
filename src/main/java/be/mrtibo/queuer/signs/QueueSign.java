package be.mrtibo.queuer.signs;

import be.mrtibo.queuer.Queuer;
import be.mrtibo.queuer.queues.Queue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.configuration.MemorySection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class QueueSign {

    private String parentQueueName;
    private Location location;
    private int taskId = -1;

    public QueueSign(String parentQueue, Sign sign) {
        this.parentQueueName = parentQueue;
        this.location = sign.getLocation();
        sign.setWaxed(true);
        sign.update();
    }

    public Queue getParentQueue() {
        return Queuer.getManager().getQueue(parentQueueName);
    }

    public void update() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Queuer.getInstance(), () -> {
            Sign sign = getState();
            SignSide side = sign.getSide(Side.FRONT);
            side.line(0, Component.text("QUEUE").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#47b4ff")));
            side.line(1, Component.text(parentQueueName));
            side.line(
                    3,
                    Component.text(getParentQueue().getMemberAmount()).decorate(TextDecoration.BOLD)
                            .append(Component.text(" players waiting").decoration(TextDecoration.BOLD, false))
            );
            sign.update();
        }, 0);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> res = new HashMap<>();
        res.put("signLocation", location.serialize());
        return res;
    }

    public static Sign deserialize(Map<String, Object> map) {
        Location loc = Location.deserialize(((MemorySection)map.get("signLocation")).getValues(false));
        return (Sign) loc.getBlock().getState();
    }

    public Sign getState() {
        return (Sign) location.getBlock().getState();
    }
}
