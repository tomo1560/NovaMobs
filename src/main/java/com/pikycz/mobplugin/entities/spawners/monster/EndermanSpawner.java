package com.pikycz.mobplugin.entities.spawners.monster;

import cn.nukkit.IPlayer;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import com.pikycz.mobplugin.task.AutoSpawnTask;
import com.pikycz.mobplugin.FileLogger;
import com.pikycz.mobplugin.entities.autospawn.SpawnResult;
import com.pikycz.mobplugin.entities.monster.walking.Enderman;
import com.pikycz.mobplugin.entities.spawners.BaseSpawner;
import com.pikycz.mobplugin.entities.utils.Utils;

/**
 * Each entity get it's own spawner class.
 *
 * @author <a href="mailto:kniffman@googlemail.com">Michael Gertz</a>
 */
public class EndermanSpawner extends BaseSpawner {

    /**
     * @param spawnTask
     * @param config
     */
    public EndermanSpawner(AutoSpawnTask spawnTask, Config config) {
        super(spawnTask, config);
    }

    public SpawnResult spawn(IPlayer iPlayer, Position pos, Level level) {
        SpawnResult result = SpawnResult.OK;

        // as enderman spawn very seldom, we need another random spawn here ...
        if (Utils.rand(0, 3) > 0) { // spawn with a 1/3 chance (it's possible that they also spawn in overworld!)
            return SpawnResult.SPAWN_DENIED;
        }

        int blockId = level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z);
        int blockLightLevel = level.getBlockLightAt((int) pos.x, (int) pos.y, (int) pos.z);

        if (!Block.solid[blockId]) { // only spawns on solid blocks
            result = SpawnResult.WRONG_BLOCK;
        } else if (blockLightLevel > 7 || (level.getTime() < Level.TIME_NIGHT && level.canBlockSeeSky(pos))) { // lightlevel not working for now, but as lightlevel is always zero that should work
            result = SpawnResult.WRONG_LIGHTLEVEL;
        } else if (pos.y > 127 || pos.y < 1 || level.getBlockIdAt((int) pos.x, (int) pos.y, (int) pos.z) == Block.AIR) { // cannot spawn on AIR block
            result = SpawnResult.POSITION_MISMATCH;
        } else {
            this.spawnTask.createEntity(getEntityName(), pos.add(0, 3.8, 0));
        }

        FileLogger.info(String.format("[%s] spawn for %s at %s,%s,%s with lightlevel %s and blockId %s, result: %s", getLogprefix(), iPlayer.getName(), pos.x, pos.y, pos.z, blockLightLevel, blockId, result));

        return result;
    }

    @Override
    public int getEntityNetworkId() {
        return Enderman.NETWORK_ID;
    }

    @Override
    public String getEntityName() {
        return "Enderman";
    }

    @Override
    protected String getLogprefix() {
        return this.getClass().getSimpleName();
    }

}
