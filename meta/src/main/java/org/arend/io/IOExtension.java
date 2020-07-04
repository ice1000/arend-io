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

import java.io.File;
import java.nio.file.Paths;

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
  public void declareDefinitions(@NotNull DefinitionContributor contributor) {
    var module = ModulePath.fromString("IO.Meta");
    var pathsModule = ModulePath.fromString("IO.Meta.Paths");
    var stringsModule = ModulePath.fromString("IO.Meta.Strings");
    var arendIoIml = "arend-io.iml";
    var arendYaml = "arend.yaml";
    contributor.declare(pathsModule, new LongName("arendYAML"),
        "The path of file `arend.yaml`.",
        Precedence.DEFAULT, new StringMeta(this, Generated.ROOT_PATH.resolve(arendYaml).toString()));
    contributor.declare(pathsModule, new LongName("arendIoIml"),
        "The path of file `arend-io.iml`.",
        Precedence.DEFAULT, new StringMeta(this, Generated.ROOT_PATH.resolve(arendIoIml).toString()));
    contributor.declare(stringsModule, new LongName("arendYamlStr"),
        "The string `arend.yaml`.",
        Precedence.DEFAULT, new StringMeta(this, arendYaml));
    contributor.declare(stringsModule, new LongName("arendIoImlStr"),
        "The string `arend-io.iml`.",
        Precedence.DEFAULT, new StringMeta(this, arendIoIml));
    contributor.declare(stringsModule, new LongName("java"),
        "The string `java`.",
        Precedence.DEFAULT, new StringMeta(this, "java"));
    contributor.declare(stringsModule, new LongName("--version"),
        "The string `--version`.",
        Precedence.DEFAULT, new StringMeta(this, "--version"));
    contributor.declare(stringsModule, new LongName("space"),
        "The string ` `.",
        Precedence.DEFAULT, new StringMeta(this, " "));
    contributor.declare(pathsModule, new LongName("projectRoot"),
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
