package pl.kiszkiel.realpay.paper;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

public class RealPayBootstrapper implements PluginBootstrap {

    private BootstrapContext ctx;

    @Override
    public void bootstrap(@NotNull BootstrapContext ctx) {
        this.ctx = ctx;
        registerCommands();
    }

    public void registerCommands() {
        this.ctx.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS,
                event -> {

        });
    }
}
