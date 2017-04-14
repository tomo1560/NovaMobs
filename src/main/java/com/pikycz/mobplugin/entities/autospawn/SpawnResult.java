/**
 * SpawnResult.java
 * <p>
 * Created on 17:15:22
 */
package com.pikycz.mobplugin.entities.autospawn;


/**
 * Describes spawn results for Spawner task (which is used only internally)
 *
 * @author <a href="mailto:kniffman@googlemail.com">Michael Gertz</a>
 *
 */
public enum SpawnResult {

    MAX_SPAWN_REACHED, WRONG_BLOCK, WRONG_LIGHTLEVEL, POSITION_MISMATCH, OK, SPAWN_DENIED;

}