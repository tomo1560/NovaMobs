/**
 * CreeperSpawner.java
 * <p>
 * Created on 10:39:49
 */
package creeperCZ.mobplugin.entities.spawners;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import creeperCZ.mobplugin.AutoSpawnTask;
import creeperCZ.mobplugin.FileLogger;
import creeperCZ.mobplugin.entities.autospawn.AbstractEntitySpawner;
import creeperCZ.mobplugin.entities.autospawn.SpawnResult;
import creeperCZ.mobplugin.entities.monster.walking.Enderman;
import creeperCZ.mobplugin.entities.utils.Utils;

/**
 * Each entity get it's own spawner class.
 *
 * @author <a href="mailto:kniffman@googlemail.com">Michael Gertz</a>
 */
public class EndermanSpawner extends AbstractEntitySpawner {

    /**
     * @param spawnTask
     */
    public EndermanSpawner(AutoSpawnTask spawnTask, Config pluginConfig) {
        super(spawnTask, pluginConfig);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        // as enderman spawn very seldom, we need another random spawn here ...
        if (Utils.rand(0, 3) > 0) { // spawn with a 1/3 chance (it's possible that they also spawn in overworld!)
            return SpawnResult.SPAWN_DENIED;
        }

        if (level.getTime() > Level.TIME_NIGHT) {
            int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
            int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);

            if (!Block.solid[blockId]) { // only spawns on solid blocks
                result = SpawnResult.WRONG_BLOCK;
            } else if (blockLightLevel > 7) { // lightlevel not working for now, but as lightlevel is always zero that should work
                result = SpawnResult.WRONG_LIGHTLEVEL;
            } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
                result = SpawnResult.POSITION_MISMATCH;
            } else {
                this.spawnTask.createEntity(getEntityName(), pos.add(0, 3.8, 0));
            }

            FileLogger.info(String.format("[%s] spawn for %s at %s,%s,%s with lightlevel %s and blockId %s, result: %s", getLogprefix(), iPlayer.getName(), pos.x, pos.y, pos.z, blockLightLevel, blockId, result));
        } else {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        }

        return result;
    }

    /* (@Override)
     * @see cn.nukkit.entity.ai.IEntitySpawner#getEntityNetworkId()
     */
    @Override
    public int getEntityNetworkId() {
        return Enderman.NETWORK_ID;
    }

    /* (@Override)
     * @see cn.nukkit.entity.ai.IEntitySpawner#getEntityName()
     */
    @Override
    public String getEntityName() {
        return "Enderman";
    }

    /* (@Override)
     * @see de.kniffo80.mobplugin.entities.autospawn.AbstractEntitySpawner#getLogprefix()
     */
    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
