package com.pikycz.mobplugin.entities.spawners.animal;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import com.pikycz.mobplugin.task.AutoSpawnTask;
import com.pikycz.mobplugin.FileLogger;
import com.pikycz.mobplugin.entities.animal.walking.Pig;
import com.pikycz.mobplugin.entities.autospawn.SpawnResult;
import com.pikycz.mobplugin.entities.spawners.BaseSpawner;

/**
 * Each entity get it's own spawner class.
 *
 * @author <a href="mailto:kniffman@googlemail.com">Michael Gertz</a>
 */
public class PigSpawner extends BaseSpawner {

    /**
     * @param spawnTask
     * @param config
     */
    public PigSpawner(AutoSpawnTask spawnTask, Config config) {
        super(spawnTask, config);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (blockId != Block.GRASS) { // only spawns on gras
            result = SpawnResult.WRONG_BLOCK;
        } else if (blockLightLevel < 9) {
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
            result = SpawnResult.POSITION_MISMATCH;
        } else { // creeper is spawned
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 1.9, 0));
        }

        FileLogger.info(String.format("[%s] spawn for %s at %s,%s,%s with lightlevel %s and blockId %s, result: %s", getLogprefix(), iPlayer.getName(), pos.x, pos.y, pos.z, blockLightLevel, blockId, result));

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Pig.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Pig";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
