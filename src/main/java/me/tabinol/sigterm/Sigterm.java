/*
 Sigterm: Override SIGTERM for graceful shutdown
 Copyright (C) 2019 Tabinol
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.sigterm;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import sun.misc.Signal;

public final class Sigterm extends JavaPlugin {

    @Override
    public void onEnable() {
        final Logger logger = getLogger();

        // Check if it is Linux
        if (!System.getProperty("os.name").toLowerCase().contains("linux")) {
            logger.warning("This plugin is only compatible with Linux (or Docker Linux)!");
            logger.warning("No SIGTERM action will be added!");
            return;
        }

        // Register SIGTERM
        try {
            final Signal signal = new Signal("TERM");
            Signal.handle(signal, Signal -> {
                if (signal.getName().equals("TERM")) {
                    logger.info("Catching SIG" + signal.getName() + "! Shutdown gracefully!");
                    // Call back "/stop" command in main thread.
                    Bukkit.getScheduler().callSyncMethod(this,
                            () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop"));
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
