package org.arend.io;

import org.arend.ext.ArendPrelude;
import org.arend.ext.DefaultArendExtension;
import org.arend.ext.DefinitionContributor;
import org.arend.ext.concrete.ConcreteFactory;
import org.arend.ext.core.definition.CoreConstructor;
import org.arend.ext.core.definition.CoreDataDefinition;
import org.arend.ext.core.definition.CoreFunctionDefinition;
import org.arend.ext.dependency.Dependency;
import org.arend.ext.module.LongName;
import org.arend.ext.module.ModulePath;
import org.arend.ext.reference.Precedence;
import org.jetbrains.annotations.NotNull;

public class IOExtension extends DefaultArendExtension {
  public ArendPrelude prelude;
  public ConcreteFactory factory;

  @Dependency(module = "IO.Base", name = "List.nil")
  public CoreConstructor nil;
  @Dependency(module = "IO.Base", name = "List.::")
  public CoreConstructor cons;
  @Dependency(module = "IO.Base", name = "String")
  public CoreFunctionDefinition string;
  @Dependency(module = "IO.Base", name = "IO")
  public CoreDataDefinition io;

  @Override
  public void setPrelude(@NotNull ArendPrelude prelude) {
    this.prelude = prelude;
  }

  @Override
  public void setConcreteFactory(@NotNull ConcreteFactory factory) {
    this.factory = factory;
  }

  @Override
  public void declareDefinitions(@NotNull DefinitionContributor contributor) {
    var module = ModulePath.fromString("IO.Meta");
    contributor.declare(module, new LongName("arendYAML"),
        "The path of file `arend.yaml`.",
        Precedence.DEFAULT, new StringMeta(this, "C:\\Users\\ice10\\git-repos\\Arend\\arend-io\\arend.yaml"));
    contributor.declare(module, new LongName("arendIoIml"),
        "The path of file `arend-io.iml`.",
        Precedence.DEFAULT, new StringMeta(this, "C:\\Users\\ice10\\git-repos\\Arend\\arend-io\\arend-io.iml"));
    contributor.declare(module, new LongName("performIO"),
        "Execute an IO action.",
        Precedence.DEFAULT, new PerformIOMeta(this));
  }
}
