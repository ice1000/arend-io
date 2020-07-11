package org.arend.io;

import org.arend.ext.ArendPrelude;
import org.arend.ext.DefaultArendExtension;
import org.arend.ext.DefinitionContributor;
import org.arend.ext.StringTypechecker;
import org.arend.ext.concrete.ConcreteFactory;
import org.arend.ext.core.definition.CoreConstructor;
import org.arend.ext.core.definition.CoreDataDefinition;
import org.arend.ext.core.definition.CoreFunctionDefinition;
import org.arend.ext.dependency.Dependency;
import org.arend.ext.module.LongName;
import org.arend.ext.module.ModulePath;
import org.arend.ext.reference.Precedence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class IOExtension extends DefaultArendExtension {
  public ArendPrelude prelude;
  public ConcreteFactory factory;

  @Dependency(module = "IO.FakeString", name = "List.nil")
  public CoreConstructor nil;
  @Dependency(module = "IO.FakeString", name = "List.::")
  public CoreConstructor cons;
  @Dependency(module = "IO.FakeString", name = "String")
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
  public @Nullable StringTypechecker getStringTypechecker() {
    return new FakeStringTypechecker(this);
  }

  @Override
  public void declareDefinitions(@NotNull DefinitionContributor contributor) {
    var module = ModulePath.fromString("IO.Meta");
    var stringsModule = ModulePath.fromString("IO.Meta.Strings");
    contributor.declare(stringsModule, new LongName("projectRoot"),
        "The path to the project root.",
        Precedence.DEFAULT, new StringMeta(this, Generated.ROOT));
    contributor.declare(stringsModule, new LongName("pathSeparator"),
        "The path separator, works differently on each platform.",
        Precedence.DEFAULT, new StringMeta(this, File.separator));
    contributor.declare(module, new LongName("unsafePerformIO"),
        "Execute an IO action.",
        Precedence.DEFAULT, new PerformIOMeta(this));
  }
}
